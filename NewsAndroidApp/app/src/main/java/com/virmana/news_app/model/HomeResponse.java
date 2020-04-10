package com.virmana.news_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeResponse {
    @SerializedName("slides")
    @Expose
    private List<Slide> slides = null;

    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;

    @SerializedName("questions")
    @Expose
    private List<Question> questions = null;

    @SerializedName("posts")
    @Expose
    private List<Post> posts = null;

    public void setSlides(List<Slide> slides) {
        this.slides = slides;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Slide> getSlides() {
        return slides;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public List<Post> getPosts() {
        return posts;
    }
}
