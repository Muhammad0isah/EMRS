package com.mtech.envirotrack.weather;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class HourlyForecastResponse {

    @SerializedName("hourly")
    private List<HourlyForecast> hourly;

    public List<HourlyForecast> getHourly() {
        return hourly;
    }

    public static class HourlyForecast {

        @SerializedName("temp")
        private float temp;

        @SerializedName("humidity")
        private int humidity;

        @SerializedName("wind_speed")
        private double windSpeed;

        @SerializedName("wind_deg")
        private int windDeg;

        public float getTemp() {
            return temp;
        }

        public int getHumidity() {
            return humidity;
        }

        public double getWindSpeed() {
            return windSpeed;
        }

        public int getWindDeg() {
            return windDeg;
        }
    }
}