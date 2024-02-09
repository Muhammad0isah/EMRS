package com.mtech.envirotrack.weather;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class HourlyForecastResponse {

    @SerializedName("list")
    private List<HourlyForecast> list;

    public List<HourlyForecast> getList() {
        return list;
    }

    public static class HourlyForecast {

        @SerializedName("main")
        private Main main;

        public Main getMain() {
            return main;
        }

        public static class Main {

            @SerializedName("temp")
            private float temp;

            public float getTemp() {
                return temp;
            }
        }
    }
}