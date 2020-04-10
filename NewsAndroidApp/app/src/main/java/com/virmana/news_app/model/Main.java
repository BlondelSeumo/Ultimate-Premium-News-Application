package com.virmana.news_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hsn on 14/02/2017.
 */

public class Main {


        @SerializedName("temp")
        @Expose
        private String temp;
        @SerializedName("pressure")
        @Expose
        private String pressure;
        @SerializedName("humidity")
        @Expose
        private String humidity;
        @SerializedName("temp_min")
        @Expose
        private String tempMin;
        @SerializedName("temp_max")
        @Expose
        private String tempMax;
        @SerializedName("sea_level")
        @Expose
        private String seaLevel;
        @SerializedName("grnd_level")
        @Expose
        private String grndLevel;

        public String getTemp() {
            Double t=Double.parseDouble(this.temp);
            long m =Math.round(t);
            String tmp=m+"";
            return tmp;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }

        public String getPressure() {
            return pressure;
        }

        public void setPressure(String pressure) {
            this.pressure = pressure;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public String getTempMin() {
            return tempMin;
        }

        public void setTempMin(String tempMin) {
            this.tempMin = tempMin;
        }

        public String getTempMax() {
            return tempMax;
        }

        public void setTempMax(String tempMax) {
            this.tempMax = tempMax;
        }

        public String getSeaLevel() {
            return seaLevel;
        }

        public void setSeaLevel(String seaLevel) {
            this.seaLevel = seaLevel;
        }

        public String getGrndLevel() {
            return grndLevel;
        }

        public void setGrndLevel(String grndLevel) {
            this.grndLevel = grndLevel;
        }


}
