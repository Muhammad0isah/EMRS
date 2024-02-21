package com.mtech.envirotrack.weather;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class HourlyForecastResponse {

    @SerializedName("hourly")
    private List<Hourly> hourly;

    public List<Hourly> getHourly() {
        return hourly;
    }

    public static class Hourly {

        @SerializedName("dt")
        private long dt;

        @SerializedName("temp")
        private double temp;

        @SerializedName("feels_like")
        private double feels_like;

        @SerializedName("pressure")
        private int pressure;

        @SerializedName("humidity")
        private int humidity;

        @SerializedName("dew_point")
        private double dew_point;

        @SerializedName("uvi")
        private double uvi;

        @SerializedName("clouds")
        private int clouds;

        @SerializedName("visibility")
        private int visibility;

        @SerializedName("wind_speed")
        private double wind_speed;

        @SerializedName("wind_gust")
        private double wind_gust;

        @SerializedName("wind_deg")
        private int wind_deg;

        @SerializedName("pop")
        private double pop;

        @SerializedName("rain")
        private Rain rain;

        @SerializedName("snow")
        private Snow snow;

        @SerializedName("weather")
        private List<Weather> weather;

        public long getDt() {
            return dt;
        }

        public double getTemp() {
            return temp;
        }

        public double getFeels_like() {
            return feels_like;
        }

        public int getPressure() {
            return pressure;
        }

        public int getHumidity() {
            return humidity;
        }

        public double getDew_point() {
            return dew_point;
        }

        public double getUvi() {
            return uvi;
        }

        public int getClouds() {
            return clouds;
        }

        public int getVisibility() {
            return visibility;
        }

        public double getWind_speed() {
            return wind_speed;
        }

        public double getWind_gust() {
            return wind_gust;
        }

        public int getWind_deg() {
            return wind_deg;
        }

        public double getPop() {
            return pop;
        }

        public Rain getRain() {
            return rain;
        }

        public Snow getSnow() {
            return snow;
        }

        public List<Weather> getWeather() {
            return weather;
        }
    }

    public static class Rain {
        @SerializedName("1h")
        private double oneHour;

        public double getOneHour() {
            return oneHour;
        }
    }

    public static class Snow {
        @SerializedName("1h")
        private double oneHour;

        public double getOneHour() {
            return oneHour;
        }
    }

    public static class Weather {
        @SerializedName("id")
        private int id;

        @SerializedName("main")
        private String main;

        @SerializedName("description")
        private String description;

        @SerializedName("icon")
        private String icon;

        public int getId() {
            return id;
        }

        public String getMain() {
            return main;
        }

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }
    }
}