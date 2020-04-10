package com.virmana.news_app.model;

/**
 * Created by Tamim on 08/10/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Post {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("review")
    @Expose
    private Boolean review;
    @SerializedName("trusted")
    @Expose
    private Boolean trusted;
    @SerializedName("comment")
    @Expose
    private Boolean comment;
    @SerializedName("comments")
    @Expose
    private Integer comments;
    @SerializedName("views")
    @Expose
    private Integer views;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("userid")
    @Expose
    private Integer userid;
    @SerializedName("userimage")
    @Expose
    private String userimage;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("original")
    @Expose
    private String original;
    @SerializedName("video")
    @Expose
    private String video;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("tags")
    @Expose
    private Object tags;
    @SerializedName("shares")
    @Expose
    private Integer shares;



    private Integer viewType = 1;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getReview() {
        return review;
    }

    public void setReview(Boolean review) {
        this.review = review;
    }

    public Boolean getComment() {
        return comment;
    }

    public void setComment(Boolean comment) {
        this.comment = comment;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Object getTags() {
        return tags;
    }

    public void setTags(Object tags) {
        this.tags = tags;
    }

    public Integer getShares() {
        return shares;
    }

    public void setShares(Integer shares) {
        this.shares = shares;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getType() {
        return type;
    }

    public String getVideo() {
        return video;
    }

    public Post setViewType(Integer viewType) {
        this.viewType = viewType;
        return this;
    }

    public void setTrusted(Boolean trusted) {
        this.trusted = trusted;
    }

    public Boolean getTrusted() {
        return trusted;
    }

    public Integer getViewType() {
        return viewType;
    }


    public Post() {
    }

    public Post(Integer id, String title, String content, Boolean review, Boolean trusted, Boolean comment, Integer comments, Integer views, String user, Integer userid, String userimage, String thumbnail, String original, String created, Object tags, Integer shares) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.review = review;
        this.trusted = trusted;
        this.comment = comment;
        this.comments = comments;
        this.views = views;
        this.user = user;
        this.userid = userid;
        this.userimage = userimage;
        this.thumbnail = thumbnail;
        this.original = original;
        this.created = created;
        this.tags = tags;
        this.shares = shares;
    }
}