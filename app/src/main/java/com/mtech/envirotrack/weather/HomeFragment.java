package com.mtech.envirotrack.weather;

import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.mtech.envirotrack.R;
import com.mtech.envirotrack.user.Login;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private static final String API_KEY = "ccb348eb42482302b46b698521bf6336";
    // ccb348eb42482302b46b698521bf6336
    // 0d4ca4f647225fc6815e9f9a9c5fee42

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    TextView temperatureTextView, humidityTextView, windSpeedTextView, windDirectionTextView,cityNameTextView;
    private String cityName;
    private LineChart chart;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String cityName) {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        cityName = sharedPreferences.getString("cityName", null);

        if (cityName == null) {
            // cityName not found, try to get stateName
            cityName = sharedPreferences.getString("stateName", "");
        }

        // cityName from searchFragment
        if(getArguments() != null) {
            cityName = getArguments().getString("cityName");
        }
        temperatureTextView = view.findViewById(R.id.tv_temperature);
        humidityTextView = view.findViewById(R.id.tv_humidity);
        windSpeedTextView = view.findViewById(R.id.tv_wind_speed);
        windDirectionTextView = view.findViewById(R.id.tv_wind_direction);
        cityNameTextView = view.findViewById(R.id.tv_city_name);
        chart = view.findViewById(R.id.chart);

        getWeatherData();
        getHourlyForecast();

        return view;
    }

    private void getWeatherData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenWeatherMapService service = retrofit.create(OpenWeatherMapService.class);
        Call<WeatherResponse> call = service.getCurrentWeatherData(cityName, API_KEY);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                windDirectionTextView.setText(response.toString());
                if (response.code() == 200) {
                    WeatherResponse weatherResponse = response.body();
                    assert weatherResponse != null;

                    double temperatureInKelvin = weatherResponse.getMain().getTemp();
                    double temperatureInCelsius = temperatureInKelvin - 273.15;
                    int humidity = weatherResponse.getMain().getHumidity();
                    double windSpeed = weatherResponse.getWind().getSpeed();
                    int windDirection = weatherResponse.getWind().getDeg();

                    cityNameTextView.setText("City: "+cityName);
                    temperatureTextView.setText(String.format("Temperature: %.2f°C", temperatureInCelsius));
//                    humidityTextView.setText("Humidity: "+humidity + "%");
                    windSpeedTextView.setText("Wind Speed: "+windSpeed + " m/s");
//                    windDirectionTextView.setText("Wind Direction: "+windDirection + "°");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getHourlyForecast() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenWeatherMapService service = retrofit.create(OpenWeatherMapService.class);
        Call<HourlyForecastResponse> call = service.getHourlyForecast("kano", API_KEY);
        call.enqueue(new Callback<HourlyForecastResponse>() {

            @Override
            public void onResponse(Call<HourlyForecastResponse> call, Response<HourlyForecastResponse> response) {
                humidityTextView.setText(response.toString());
                if (response.code() == 200) {
                    HourlyForecastResponse hourlyForecastResponse = response.body();
                    if (hourlyForecastResponse != null && hourlyForecastResponse.getList() != null && !hourlyForecastResponse.getList().isEmpty()) {
                        List<Entry> entries = new ArrayList<>();
                        for (int i = 0; i < hourlyForecastResponse.getList().size(); i++) {
                            float temperature = hourlyForecastResponse.getList().get(i).getMain().getTemp();
                            entries.add(new Entry(i, temperature));
                        }

                        LineDataSet dataSet = new LineDataSet(entries, "Label");
                        dataSet.setLineWidth(2.5f);
                        dataSet.setCircleRadius(4.5f);

                        LineData lineData = new LineData(dataSet);
                        setupChart(chart, lineData, Color.rgb(104, 241, 175));
                    } else {
                        Toast.makeText(getContext(), "No forecast data available", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<HourlyForecastResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setupChart(LineChart chart, LineData data, int color) {
        chart.setViewPortOffsets(0, 0, 0, 0);
        chart.setBackgroundColor(color);

        chart.setData(data);

        Description desc = new Description();
        desc.setText("");

        chart.setDescription(desc);
        chart.setDrawGridBackground(false);

        chart.setPinchZoom(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setTextSize(10f);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(170f);
        leftAxis.setYOffset(-9f);
        leftAxis.setTextColor(Color.rgb(255, 192, 56));

        chart.getAxisRight().setEnabled(false);

        chart.getLegend().setEnabled(false);

        chart.animateX(2000);
        chart.invalidate();
    }
}