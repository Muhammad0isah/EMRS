package com.mtech.envirotrack.weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMapService {
    @GET("weather")
    Call<WeatherResponse> getCurrentWeatherData(@Query("q") String city, @Query("appid") String apiKey);
    @GET("forecast/hourly")
    Call<HourlyForecastResponse> getHourlyForecast(@Query("q") String cityName, @Query("appid") String apiKey);
}
