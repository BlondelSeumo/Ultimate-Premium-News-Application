package com.virmana.news_app.api.weather;


import com.virmana.news_app.model.Weather;
import com.virmana.news_app.model.weatherResponse;
import com.virmana.news_app.model.weatherResponseCurrent;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by hsn on 14/02/2017.
 */

public interface apiWeather {
    @GET("weather")
    Call<weatherResponseCurrent> getCurrentWeather(@Query("lat") String lat, @Query("lon") String lon, @Query("mode") String mode, @Query("appid") String appid, @Query("units") String units);

    @GET("forecast/daily")
    Call<weatherResponse> getFiveDayWeather(@Query("lat") String lat,@Query("lon") String lon,@Query("mode") String mode,@Query("appid") String appid,@Query("units") String units);

}

