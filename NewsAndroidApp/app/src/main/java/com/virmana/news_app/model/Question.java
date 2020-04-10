package com.virmana.news_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hsn on 31/12/2017.
 */

public class Question {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("multichoice")
    @Expose
    private Boolean multichoice;

    @SerializedName("close")
    @Expose
    private Boolean close ;

    @SerializedName("choices")
    @Expose
    private List<Choice> choices = null;

    @SerializedName("featured")
    @Expose
    private Boolean featured;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Boolean getMultichoice() {
        return multichoice;
    }

    public void setMultichoice(Boolean multichoice) {
        this.multichoice = multichoice;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public Boolean getClose() {
        return close;
    }

    public void setClose(Boolean close) {
        this.close = close;
    }


    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }
}
