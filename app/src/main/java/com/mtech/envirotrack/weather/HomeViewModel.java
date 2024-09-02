package com.mtech.envirotrack.weather;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeViewModel extends ViewModel {
    private static final String WEATHER_URL = "https://api.openweathermap.org/data/3.0/";
    private static final String POLLUTION_URL = "https://api.openweathermap.org/data/2.5/";
    private static final String API_KEY = "";

    private final MutableLiveData<WeatherResponse> weatherData = new MutableLiveData<>();
    private final MutableLiveData<HourlyForecastResponse> hourlyForecastData = new MutableLiveData<>();
    private final MutableLiveData<HourlyForecastResponse> humidityForecastData = new MutableLiveData<>();
    private final MutableLiveData<AirPollutionResponse> pollutionData = new MutableLiveData<>();
    private final MutableLiveData<AirPollutionForecastResponse> pollutionForecastData = new MutableLiveData<>();

    public LiveData<WeatherResponse> getWeatherData() {
        return weatherData;
    }

    public LiveData<HourlyForecastResponse> getHourlyForecastData() {
        return hourlyForecastData;
    }

    public LiveData<HourlyForecastResponse> getHumidityForecastData() {
        return humidityForecastData;
    }

    public LiveData<AirPollutionResponse> getPollutionData() {
        return pollutionData;
    }

    public LiveData<AirPollutionForecastResponse> getPollutionForecastData() {
        return pollutionForecastData;
    }

    public void fetchWeatherData(double lat, double lon) {
        fetchWeatherDataFromApi(lat, lon, weatherData, "getCurrentWeatherData");
    }

    public void fetchHourlyForecast(double lat, double lon) {
        fetchWeatherDataFromApi(lat, lon, hourlyForecastData, "getHourlyForecast");
    }

    public void fetchHumidityForecast(double lat, double lon) {
        fetchWeatherDataFromApi(lat, lon, humidityForecastData, "getHourlyForecast");
    }

    public void fetchPollutionData(double lat, double lon) {
        fetchPollutionDataFromApi(lat, lon, pollutionData, "getAirPollutionData");
    }

    public void fetchPollutionForecast(double lat, double lon) {
        fetchPollutionDataFromApi(lat, lon, pollutionForecastData, "getAirPollutionForecast");
    }

    private <T> void fetchWeatherDataFromApi(double lat, double lon, MutableLiveData<T> liveData, String methodName) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WEATHER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenWeatherMapService service = retrofit.create(OpenWeatherMapService.class);
        Call<T> call;
        if (methodName.equals("getCurrentWeatherData")) {
            call = (Call<T>) service.getCurrentWeatherData(lat, lon, API_KEY);
        } else {
            call = (Call<T>) service.getHourlyForecast(lat, lon, API_KEY);
        }
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                // Handle the error
            }
        });
    }

    private <T> void fetchPollutionDataFromApi(double lat, double lon, MutableLiveData<T> liveData, String methodName) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(POLLUTION_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenWeatherMapService service = retrofit.create(OpenWeatherMapService.class);
        Call<T> call;
        if (methodName.equals("getAirPollutionData")) {
            call = (Call<T>) service.getAirPollutionData(lat, lon, API_KEY);
        } else {
            call = (Call<T>) service.getAirPollutionForecast(lat, lon, API_KEY);
        }
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                // Handle the error
            }
        });
    }
}