package com.mtech.envirotrack.weather;

import com.google.gson.annotations.SerializedName;

public class WeatherResponse {
    @SerializedName("current")
    private Current current;

    public Current getCurrent() {
        return current;
    }

    public static class Current {
        @SerializedName("temp")
        private double temp;

        @SerializedName("humidity")
        private int humidity;

        @SerializedName("wind_speed")
        private double windSpeed;

        @SerializedName("wind_deg")
        private int windDeg;

        public double getTemp() {
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