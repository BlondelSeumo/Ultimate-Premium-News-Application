package com.virmana.news_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.virmana.news_app.R;

/**
 * Created by hsn on 14/02/2017.
 */
public class Weather {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("main")
    @Expose
    private String main;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("name")
    @Expose
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getIconLocal(){
        int ic=R.drawable.ic_weather_26;
        switch (this.icon){
            case "01d" :
               ic=R.drawable.ic_weather_1;
                break;
            case "02d" :
                ic=R.drawable.ic_weather_4;
                break;
            case "03d" :
                ic=R.drawable.ic_weather_22;
                break;
            case "04d" :
                ic=R.drawable.ic_weather_25;
                break;
            case "09d" :
                ic=R.drawable.ic_weather_3;
                break;
            case "10d" :
                ic=R.drawable.ic_weather_2;
                break;
            case "11d" :
                ic=R.drawable.ic_weather_5;
                break;
            case "13d" :
                ic=R.drawable.ic_weather_11;
                break;
            case "01n" :
                ic=R.drawable.ic_weather_6;
                break;
            case "02n" :
                ic=R.drawable.ic_weather_7;
                break;
            case "03n" :
                ic=R.drawable.ic_weather_22;
                break;
            case "04n" :
                ic=R.drawable.ic_weather_25;
                break;
            case "09n" :
                ic=R.drawable.ic_weather_3;
                break;
            case "10n" :
                ic=R.drawable.ic_weather_8;
                break;
            case "11n" :
                ic=R.drawable.ic_weather_5;
                break;
            case "13n" :
                ic=R.drawable.ic_weather_11;
                break;
            case "50n" :
                ic=R.drawable.ic_weather_21;
                break;
        }
        return ic;
    }

}