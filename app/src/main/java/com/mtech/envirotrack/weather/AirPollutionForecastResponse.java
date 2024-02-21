package com.mtech.envirotrack.weather;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AirPollutionForecastResponse {

    @SerializedName("list")
    private List<AirPollutionForecast> list;

    public List<AirPollutionForecast> getList() {
        return list;
    }

    public static class AirPollutionForecast {

        @SerializedName("main")
        private Main main;

        @SerializedName("components")
        private Components components;

        @SerializedName("dt")
        private long dt;

        public Main getMain() {
            return main;
        }

        public Components getComponents() {
            return components;
        }

        public long getDt() {
            return dt;
        }

        public static class Main {

            @SerializedName("aqi")
            private int aqi;

            public int getAqi() {
                return aqi;
            }
        }

        public static class Components {

            @SerializedName("co")
            private double co;

            @SerializedName("no")
            private double no;

            @SerializedName("no2")
            private double no2;

            @SerializedName("o3")
            private double o3;

            @SerializedName("so2")
            private double so2;

            @SerializedName("pm2_5")
            private double pm2_5;

            @SerializedName("pm10")
            private double pm10;

            @SerializedName("nh3")
            private double nh3;

            public double getCo() {
                return co;
            }

            public double getNo() {
                return no;
            }

            public double getNo2() {
                return no2;
            }

            public double getO3() {
                return o3;
            }

            public double getSo2() {
                return so2;
            }

            public double getPm2_5() {
                return pm2_5;
            }

            public double getPm10() {
                return pm10;
            }

            public double getNh3() {
                return nh3;
            }
        }
    }
}