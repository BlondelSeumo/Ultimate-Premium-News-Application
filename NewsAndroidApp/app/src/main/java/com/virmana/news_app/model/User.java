package com.virmana.news_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Tamim on 17/01/2018.
 */


public class User {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("thum")
    @Expose
    private String thum;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("trusted")
    @Expose
    private String trusted;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setThum(String thum) {
        this.thum = thum;
    }

    public String getThum() {
        return thum;
    }

    public void setTrusted(String trusted) {
        this.trusted = trusted;
    }

    public String getTrusted() {
        return trusted;
    }
}