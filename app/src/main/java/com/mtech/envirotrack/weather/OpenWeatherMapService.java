package com.mtech.envirotrack.weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMapService {
    @GET("onecall")
    Call<WeatherResponse> getCurrentWeatherData(@Query("lat") double lat, @Query("lon") double lon, @Query("appid") String appid);

    @GET("onecall")
    Call<HourlyForecastResponse> getHourlyForecast(@Query("lat") double lat, @Query("lon") double lon, @Query("appid") String appid);

    @GET("air_pollution")
    Call<AirPollutionResponse> getAirPollutionData(@Query("lat") double lat, @Query("lon") double lon, @Query("appid") String appid);
}
