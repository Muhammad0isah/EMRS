package com.mtech.envirotrack.weather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AirPollutionResponse {

    @SerializedName("list")
    private List<Components> components;

    public List<Components> getComponents() {
        return components;
    }


    public static class Components {
        @SerializedName("aqi")
        private int aqi;

        @SerializedName("co")
        private double co;

        @SerializedName("no")
        private double no;

        @SerializedName("no2")
        private double no2;

        @SerializedName("o3")
        private double o3;

        @SerializedName("so2")
        private int so2;

        @SerializedName("pm2_5")
        private int pm2_5;

        @SerializedName("pm10")
        private int pm10;

        @SerializedName("nh3")
        private int nh3;

        public int getAqi() {
            return aqi;
        }

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

        public int getSo2() {
            return so2;
        }

        public int getPm2_5() {
            return pm2_5;
        }

        public int getPm10() {
            return pm10;
        }

        public int getNh3() {
            return nh3;
        }
    }

}