package com.mtech.envirotrack.weather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.graphics.Color;
import android.provider.Settings;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.tabs.TabLayout;
import com.mtech.envirotrack.MyApplication;
import com.mtech.envirotrack.R;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Home extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    TextView temperatureTextView, humidityTextView, windSpeedTextView, windDirectionTextView,cityNameTextView;
    private String cityName;
    private double lat = 0;
    private double lon = 0;
    private LineChart chart;

    TextView tvAqi, tvCo, tvNo, tvNo2, tvO3, tvSo2, tvPm2_5, tvPm10, tvNh3;

    private HomeViewModel homeViewModel;

    public Home() {
        // Required empty public constructor
    }

    public static Home newInstance(String cityName) {
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

        MyApplication myApplication = MyApplication.getInstance();
        List<Location> locations = myApplication.getLocations();

        // Check if locations list is not empty
        if (!locations.isEmpty()) {
            Location location = locations.get(0); // get the first location
            if (location != null) {
                lat = location.getLatitude();
                lon = location.getLongitude();
            } else {
                // Handle the situation when location is null, by prompting user to turn on location services
                Toast.makeText(getContext(), "Please turn on location services", Toast.LENGTH_SHORT).show();
            }
        }
        // Initialize homeViewModel here
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        SharedPreferences sharedPreferences = Objects.requireNonNull(requireActivity()).getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
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
        chart = view.findViewById(R.id.chart);

        tvAqi = view.findViewById(R.id.tv_aqi);
        tvCo = view.findViewById(R.id.tv_co);
        tvNo = view.findViewById(R.id.tv_no);
        tvNo2 = view.findViewById(R.id.tv_no2);
        tvO3 = view.findViewById(R.id.tv_o3);
        tvSo2 = view.findViewById(R.id.tv_so2);
        tvPm2_5 = view.findViewById(R.id.tv_pm2_5);
        tvPm10 = view.findViewById(R.id.tv_pm10);
        tvNh3 = view.findViewById(R.id.tv_nh3);

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Temperature"));
        tabLayout.addTab(tabLayout.newTab().setText("Humidity"));
        tabLayout.addTab(tabLayout.newTab().setText("AQI"));
        initializeAndFetchData(view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeAndFetchData(view);

    }


    private void setupChart(LineChart chart, LineData data, int color) {
        chart.setViewPortOffsets(0, 0, 0, 0);
        chart.setBackgroundColor(getResources().getColor(R.color.white));

        chart.setData(data);
        chart.getLegend().setEnabled(true);
        Legend legend = chart.getLegend();
        legend.setEnabled(true);
        legend.setTextColor(getResources().getColor(R.color.dark_green));
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);



        chart.setDrawGridBackground(false);

        chart.setPinchZoom(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new HourAxisValueFormatter());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setTextSize(10f);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.setTextColor(getResources().getColor(R.color.dark_green));
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f);

        YAxis leftAxis = chart.getAxisLeft();
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setTextColor(getResources().getColor(R.color.dark_green));
        yAxis.setAxisMaximum(50f);
        yAxis.setTextColor(getResources().getColor(R.color.dark_green));
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setTextColor(getResources().getColor(R.color.dark_green));
        leftAxis.setDrawGridLines(false);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setYOffset(-9f);
        leftAxis.setTextColor(getResources().getColor(R.color.dark_green));
        chart.getAxisRight().setEnabled(false);

        // Zoom in, with the center of the zoom at the middle of the chart
        float zoomLevelX = 1.4f; // Zoom level for x-axis
        float zoomLevelY = 1f; // Zoom level for y-axis
        float centerX = chart.getXChartMax() / 2f; // Center point for x-axis
        float centerY = chart.getYChartMax() / 2f; // Center point for y-axis
        chart.zoom(zoomLevelX, zoomLevelY, centerX, centerY);

        chart.animateX(1500);
        // Refresh the chart
        chart.invalidate();
    }

    @Override
    public void onStart() {
        super.onStart();
        initializeAndFetchData(getView());

    }
    public void initializeAndFetchData(View view) {
        // Get a reference to the ProgressBar
        ProgressBar loadingSpinner = view.findViewById(R.id.loading_spinner);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Fetch the data here, after the observers are set up
        MyApplication myApplication = MyApplication.getInstance();
        List<Location> locations = myApplication.getLocations();
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);

        // Check if locations list is not empty
        double lat = 0;
        double lon = 0;
        if (!locations.isEmpty()) {
            Location location = locations.get(0); // get the first location
            if (location != null) {
                lat = location.getLatitude();
                lon = location.getLongitude();
                // Show the loading spinner
                loadingSpinner.setVisibility(View.VISIBLE);
                homeViewModel.fetchWeatherData(lat, lon);
                homeViewModel.fetchPollutionData(lat, lon);
            } else {
                   // Handle the situation when location is null, by prompting user to turn on location services
                    Toast.makeText(getContext(), "Please turn on location services", Toast.LENGTH_SHORT).show();
            }
        }

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getWeatherData().observe(getViewLifecycleOwner(), new Observer<WeatherResponse>() {
            @Override
            public void onChanged(WeatherResponse weatherResponse) {
                // Hide the loading spinner
                loadingSpinner.setVisibility(View.GONE);

                if (weatherResponse != null) {
                    double temperatureInKelvin = weatherResponse.getCurrent().getTemp();
                    double temperatureInCelsius = temperatureInKelvin - 273.15;
                    int humidity = weatherResponse.getCurrent().getHumidity();
                    double windSpeed = weatherResponse.getCurrent().getWindSpeed();
                    int windDirection = weatherResponse.getCurrent().getWindDeg();

                    temperatureTextView.setText(String.format("%.2f°C", temperatureInCelsius));
                    humidityTextView.setText(humidity + "%");
                    String windSpeedText = windSpeed + " <i>ms<sup>-1</sup></i>";
                    windSpeedTextView.setText(Html.fromHtml(windSpeedText));
                    windDirectionTextView.setText(windDirection + "°");
                }
            }
        });

        homeViewModel.getPollutionData().observe(getViewLifecycleOwner(), new Observer<AirPollutionResponse>() {
            @Override
            public void onChanged(AirPollutionResponse airPollutionResponse) {
                // Hide the loading spinner
                loadingSpinner.setVisibility(View.GONE);

                if (airPollutionResponse != null) {
                    AirPollutionResponse.AirPollution.Components components = airPollutionResponse.getList().get(0).getComponents();

                    // Now you can call the methods on the Components object
                    int aqi = airPollutionResponse.getList().get(0).getMain().getAqi();
                    tvAqi.setText("AQI: " + aqi);
                    tvCo.setText(components.getCo() + "μg/m³");
                    tvNo.setText(components.getNo() + "μg/m³");
                    tvNo2.setText(components.getNo2() + "μg/m³");
                    tvO3.setText(components.getO3() + "μg/m³");
                    tvSo2.setText(components.getSo2() + "μg/m³");
                    tvPm2_5.setText(components.getPm2_5() + "μg/m³");
                    tvPm10.setText(components.getPm10() + "μg/m³");
                    tvNh3.setText(components.getNh3() + "μg/m³");
                }
            }
        });

        homeViewModel.getHourlyForecastData().observe(getViewLifecycleOwner(), new Observer<HourlyForecastResponse>() {
            @Override
            public void onChanged(HourlyForecastResponse hourlyForecastResponse) {
                // Hide the loading spinner
                loadingSpinner.setVisibility(View.GONE);

                if (hourlyForecastResponse != null) {
                    // Process the response
                    List<Entry> entries = new ArrayList<>();
                    int size = Math.min(24, hourlyForecastResponse.getHourly().size());
                    for (int i = 0; i < size; i++) {
                        float temperature = (float) (hourlyForecastResponse.getHourly().get(i).getTemp() - 273.15);
                        entries.add(new Entry(i, temperature));
                    }

                    LineDataSet dataSet = new LineDataSet(entries, "Temperature");
                    dataSet.setColor(getResources().getColor(R.color.dark_green));
                    dataSet.setLineWidth(2.5f);
                    dataSet.setCircleRadius(4.5f);

                    LineData lineData = new LineData(dataSet);
                    setupChart(chart, lineData, getResources().getColor(R.color.dark_green));
                }
            }
        });

        homeViewModel.getPollutionForecastData().observe(getViewLifecycleOwner(), new Observer<AirPollutionForecastResponse>() {
            @Override
            public void onChanged(AirPollutionForecastResponse forecastResponse) {
                // Hide the loading spinner
                loadingSpinner.setVisibility(View.GONE);

                if (forecastResponse != null) {
                    // Process the response
                    List<Entry> aqiEntries = new ArrayList<>();
                    List<Entry> pm25Entries = new ArrayList<>();
                    List<Entry> pm10Entries = new ArrayList<>();
                    List<Entry> no2Entries = new ArrayList<>();
                    int size = Math.min(24, forecastResponse.getList().size());
                    for (int i = 0; i < size; i++) {
                        float aqi = forecastResponse.getList().get(i).getMain().getAqi();
                        float pm25 = (float) forecastResponse.getList().get(i).getComponents().getPm2_5();
                        float pm10 = (float) forecastResponse.getList().get(i).getComponents().getPm10();
                        float no2 = (float) forecastResponse.getList().get(i).getComponents().getNo2();
                        aqiEntries.add(new Entry(i, aqi));
                        pm25Entries.add(new Entry(i, pm25));
                        pm10Entries.add(new Entry(i, pm10));
                        no2Entries.add(new Entry(i, no2));
                    }
                    LineDataSet aqiDataSet = new LineDataSet(aqiEntries, "AQI");
                    aqiDataSet.setColor(Color.RED);
                    aqiDataSet.setDrawValues(false);
                    aqiDataSet.setDrawIcons(false);

                    LineDataSet pm25DataSet = new LineDataSet(pm25Entries, "PM2.5");
                    pm25DataSet.setColor(Color.GREEN);
                    pm25DataSet.setDrawValues(false);
                    pm25DataSet.setDrawIcons(false);

                    LineDataSet pm10DataSet = new LineDataSet(pm10Entries, "PM10");
                    pm10DataSet.setColor(Color.BLUE);
                    pm10DataSet.setDrawValues(false);
                    pm10DataSet.setDrawIcons(false);

                    LineDataSet no2DataSet = new LineDataSet(no2Entries, "NO2");
                    no2DataSet.setColor(Color.YELLOW);
                    no2DataSet.setDrawValues(false);
                    no2DataSet.setDrawIcons(false);

                    LineData lineData = new LineData(aqiDataSet, pm25DataSet, pm10DataSet, no2DataSet);
                    setupChart(chart, lineData, getResources().getColor(R.color.dark_green));

                    // Set the Y-axis limit for the air quality forecast
                    chart.getAxisLeft().setAxisMaximum(300f); // Set the maximum value
                    chart.getAxisLeft().setAxisMinimum(0f); // Set the minimum value
                } else {
                    // The data is null. You might want to show an error message or a placeholder.
                    Toast.makeText(getContext(), "No air pollution forecast available.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        homeViewModel.getHumidityForecastData().observe(getViewLifecycleOwner(), new Observer<HourlyForecastResponse>() {
            @Override
            public void onChanged(HourlyForecastResponse hourlyForecastResponse) {
                // Hide the loading spinner
                loadingSpinner.setVisibility(View.GONE);

                if (hourlyForecastResponse != null && hourlyForecastResponse.getHourly() != null && !hourlyForecastResponse.getHourly().isEmpty()) {
                    List<Entry> humidityEntries = new ArrayList<>();
                    // Limit the loop to the first 24 entries
                    for (int i = 0; i < 24 && i < hourlyForecastResponse.getHourly().size(); i++) {
                        float humidity = (float) hourlyForecastResponse.getHourly().get(i).getHumidity();
                        humidityEntries.add(new Entry(i, humidity));
                    }

                    LineDataSet humidityDataSet = new LineDataSet(humidityEntries, "Humidity");
                    humidityDataSet.setColor(Color.BLUE);
                    humidityDataSet.setLineWidth(2.5f);
                    humidityDataSet.setCircleRadius(4.5f);

                    LineData lineData = new LineData(humidityDataSet);
                    setupChart(chart, lineData, getResources().getColor(R.color.dark_green));
                    chart.getAxisLeft().setAxisMaximum(100f); // Set the maximum value
                    chart.getAxisLeft().setAxisMinimum(0f); // Set the minimum value
                } else {
                    Toast.makeText(getContext(), "No forecast data available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        final double finalLat = lat;
        final double finalLon = lon;

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0: // Temperature tab
                        homeViewModel.fetchHourlyForecast(finalLat, finalLon);
                        break;
                    case 1: // Humidity tab
                        homeViewModel.fetchHumidityForecast(finalLat, finalLon);
                        break;
                    case 2: // AQI tab
                        // Update chart with AQI data
                        homeViewModel.fetchPollutionForecast(finalLat, finalLon);
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        homeViewModel.fetchHourlyForecast(finalLat, finalLon);
    }
}

class HourAxisValueFormatter extends ValueFormatter {
    private DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
    private SimpleDateFormat sdf;

    public HourAxisValueFormatter() {
        // Set the AM and PM strings
        symbols.setAmPmStrings(new String[] { "pm", "am" });
        // Create a new SimpleDateFormat object
        sdf = new SimpleDateFormat("ha", Locale.getDefault());
        // Set the DateFormatSymbols of the SimpleDateFormat object
        sdf.setDateFormatSymbols(symbols);
    }
    @Override
    public String getFormattedValue(float value) {
        // Convert the value (which is the hour in 24-hour format) to 12-hour format
        Date date = new Date((long) value * 3600000);
        return sdf.format(date);
    }
}