package com.virmana.news_app.api;

import com.virmana.news_app.config.Global;
import com.virmana.news_app.model.ApiResponse;
import com.virmana.news_app.model.Category;
import com.virmana.news_app.model.Comment;
import com.virmana.news_app.model.HomeResponse;
import com.virmana.news_app.model.Keyword;
import com.virmana.news_app.model.Post;
import com.virmana.news_app.model.Question;
import com.virmana.news_app.model.Language;
import com.virmana.news_app.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Tamim on 28/09/2017.
 */

public interface apiRest {

    @GET("version/check/{code}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> check(@Path("code") Integer code);




    @FormUrlEncoded
    @POST("user/register/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> register(@Field("name") String name, @Field("username") String username, @Field("password") String password, @Field("type") String type, @Field("image") String image);

    @GET("device/{tkn}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> addDevice(@Path("tkn")  String tkn);


    @GET("post/get/{id}/"+Global.SECURE_KEY+"/"+Global.ITEM_PURCHASE_CODE+"/")
    Call<Post> postById(@Path("id")  Integer id);


    @GET("post/by/query/{order}/{language}/{page}/{query}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<List<Post>> searchImage(@Path("order")  String order, @Path("language")  String language, @Path("page")  Integer page, @Path("query") String query);
    
    @GET("post/by/user/{page}/{order}/{language}/{user}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<List<Post>> userImage(@Path("order")  String order, @Path("language")  String language, @Path("user") Integer user, @Path("page") Integer page);

    @GET("post/by/follow/{page}/{language}/{user}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<List<Post>> followImage(@Path("page") Integer page, @Path("language")  String language, @Path("user") Integer user);

    @GET("post/by/me/{page}/{user}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<List<Post>> meImage(@Path("page") Integer page, @Path("user") Integer user);

    @GET("comment/list/{id}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<List<Comment>> getComments(@Path("id") Integer id);
    
    @FormUrlEncoded
    @POST("comment/add/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> addCommentImage(@Field("user")  String user,@Field("id") Integer id,@Field("comment") String comment);

    @GET("user/follow/{user}/{follower}/{key}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> follow(@Path("user") Integer user,@Path("follower") Integer follower,@Path("key") String key);


    @GET("user/followers/{user}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<List<User>> getFollowers(@Path("user") Integer user);

    @GET("user/followings/{user}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<List<User>> getFollowing(@Path("user") Integer user);


    @GET("user/followingstop/{user}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<List<User>> getFollowingTop(@Path("user") Integer user);

    @GET("user/get/{user}/{me}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> getUser(@Path("user") Integer user,@Path("me") Integer me);

    @FormUrlEncoded
    @POST("user/edit/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> editUser(@Field("user") Integer user,@Field("key") String key,@Field("name") String name,@Field("email") String email,@Field("facebook") String facebook,@Field("twitter") String twitter,@Field("instagram") String instagram);


    @GET("install/add/{id}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> addInstall(@Path("id") String id);



    @FormUrlEncoded
    @POST("support/add/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> addSupport(@Field("email") String email, @Field("name") String name , @Field("message") String message);



    @GET("category/all/{language}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<List<Category>> allCategoriesByLanguage(@Path("language")  String language);

    @GET("category/popular/{language}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<List<Category>> PopularCategoriesByLanguage(@Path("language")  String language);

    @GET("tag/popular/{page}/{language}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<List<Keyword>> PopularTagsByLanguage(@Path("page")  Integer page,@Path("language")  String language);

    @GET("language/all/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<List<Language>> languageAll();


    @GET("home/latest/{language}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<HomeResponse> home(@Path("language") String language);


    @GET("post/all/{page}/{order}/{language}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<List<Post>> imageAll(@Path("page") Integer page, @Path("order") String order, @Path("language") String language);

    @GET("post/by/category/{page}/{order}/{language}/{category}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<List<Post>> imageByCategory(@Path("page") Integer page, @Path("order") String order, @Path("language")  String language, @Path("category") Integer category);

    @GET("post/by/random/{language}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<List<Post>> ImageByRandom(@Path("language")  String language);



    @FormUrlEncoded
    @POST("post/add/share/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<Integer> addShare(@Field("id")  Integer id,@Field("user")  Integer user,@Field("key")  String key);

    @FormUrlEncoded
    @POST("post/add/view/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<Integer> addView(@Field("id")  Integer id,@Field("user")  Integer user,@Field("key")  String key);


    @FormUrlEncoded
    @POST("user/token/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> editToken(@Field("user") Integer user,@Field("key") String key,@Field("token_f") String token_f);



    @FormUrlEncoded
    @POST("user/code/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> sendRefereceCode(@Field("user") Integer user,@Field("key") String key,@Field("code") String code);



    @Multipart
    @POST("post/upload/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> uploadPost(@Part MultipartBody.Part file,@Part("id") String id, @Part("key") String key, @Part("title") String title, @Part("description") String description,@Part("language") String language,@Part("categories") String categories);

    @Multipart
    @POST("youtube/upload/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> uploadYoutube(@Part MultipartBody.Part file,@Part("id") String id, @Part("key") String key, @Part("youtube") String youtube,@Part("title") String title, @Part("description") String description,@Part("language") String language,@Part("categories") String categories);


    @Multipart
    @POST("video/upload/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> uploadVideo(@Part MultipartBody.Part file,@Part MultipartBody.Part video,@Part("id") String id, @Part("key") String key, @Part("title") String title, @Part("description") String description,@Part("language") String language,@Part("categories") String categories);



    @Multipart
    @POST("post/edit/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> editPost(@Part MultipartBody.Part file,@Part("post") String post,@Part("id") String id, @Part("key") String key, @Part("title") String title, @Part("description") String description,@Part("language") String language,@Part("categories") String categories);

    @Multipart
    @POST("youtube/edit/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> editYoutube(@Part MultipartBody.Part file,@Part("post") String post,@Part("id") String id, @Part("key") String key, @Part("title") String title, @Part("youtube") String youtube, @Part("description") String description,@Part("language") String language,@Part("categories") String categories);



    @GET("rate/add/{user}/{post}/{value}/"+Global.SECURE_KEY+"/"+Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> addRate(@Path("user")  String user,@Path("post") Integer post,@Path("value") float value);

    @GET("rate/get/{user}/{post}/"+Global.SECURE_KEY+"/"+Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> getRate(@Path("user")  String user,@Path("post") Integer post);


    @FormUrlEncoded
    @POST("post/delete/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<Integer> deletePost(@Field("id")  Integer id,@Field("user")  Integer user,@Field("key")  String key);

    @GET("language/by/post/{id}/"+Global.SECURE_KEY+"/"+Global.ITEM_PURCHASE_CODE+"/")
    Call<List<Language>>  languageByPost(@Path("id")  Integer id);



    @GET("category/by/post/{id}/"+Global.SECURE_KEY+"/"+Global.ITEM_PURCHASE_CODE+"/")
    Call<List<Category>>  CategoryByPost(@Path("id")  Integer id);

    @FormUrlEncoded
    @POST("part/delete/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<Integer> deletePart(@Field("id")  Integer id,@Field("user")  Integer user,@Field("key")  String key);

    @GET("question/all/{page}/{language}/"+Global.SECURE_KEY+"/"+Global.ITEM_PURCHASE_CODE+"/")
    Call<List<Question>> questionsAll(@Path("page") Integer page,@Path("language") Integer language);

    @GET("question/vote/{question}/{choices}/"+Global.SECURE_KEY+"/"+Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> addVote(@Path("question") Integer question,@Path("choices") String choices);

}
