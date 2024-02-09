package com.mtech.envirotrack.weather;

import com.google.gson.annotations.SerializedName;

public class WeatherResponse {
    @SerializedName("main")
    private Main main;

    @SerializedName("wind")
    private Wind wind;

    public Main getMain() {
        return main;
    }

    public Wind getWind() {
        return wind;
    }

    public class Main {
        @SerializedName("temp")
        private double temp;

        @SerializedName("humidity")
        private int humidity;

        public double getTemp() {
            return temp;
        }

        public int getHumidity() {
            return humidity;
        }
    }

    public class Wind {
        @SerializedName("speed")
        private double speed;

        @SerializedName("deg")
        private int deg;

        public double getSpeed() {
            return speed;
        }

        public int getDeg() {
            return deg;
        }
    }
}