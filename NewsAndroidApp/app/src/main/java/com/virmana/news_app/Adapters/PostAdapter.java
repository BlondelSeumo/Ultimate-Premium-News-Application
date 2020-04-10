package com.virmana.news_app.Adapters;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdListener;
import com.github.vivchar.viewpagerindicator.ViewPagerIndicator;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.facebook.ads.AdChoicesView;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.orhanobut.hawk.Hawk;
import com.squareup.picasso.Picasso;
import com.virmana.news_app.Provider.GPSTracker;
import com.virmana.news_app.R;
import com.virmana.news_app.api.apiClient;
import com.virmana.news_app.api.apiRest;
import com.virmana.news_app.api.weather.apiWeather;
import com.virmana.news_app.api.weather.weatherClient;
import com.virmana.news_app.config.Global;
import com.virmana.news_app.model.Category;
import com.virmana.news_app.model.Keyword;
import com.virmana.news_app.model.Post;
import com.virmana.news_app.model.Question;
import com.virmana.news_app.model.Slide;
import com.virmana.news_app.model.User;
import com.virmana.news_app.Provider.PrefManager;
import com.virmana.news_app.model.Weather;
import com.virmana.news_app.model.weatherResponse;
import com.virmana.news_app.model.weatherResponseCurrent;
import com.virmana.news_app.ui.Activities.PermissionActivity;
import com.virmana.news_app.ui.Activities.PostActivity;
import com.virmana.news_app.ui.Activities.UserActivity;
import com.virmana.news_app.ui.Activities.VideoActivity;
import com.virmana.news_app.ui.Activities.YoutubeActivity;
import com.virmana.news_app.ui.view.ClickableViewPager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Tamim on 08/10/2017.
 */

public class PostAdapter extends RecyclerView.Adapter {
    private  ProgressDialog register_progress;
    String[] days ={"Sun","Mon", "Tue", "Wed", "Thu", "Fri","Sat"};

    private Boolean downloads = false;
    private Boolean favorites = false;
    private Boolean loadedWeather = false;
    private Boolean loadedWeatherFiveDay = false;
    private String oldlatitude = "null";
    private String oldlongitude = "null";
    private String oldunit = "null";
    private weatherResponseCurrent weather ;
    private weatherResponse _weatherResponse ;
    private List<Post> postList = new ArrayList<>();
    private List<Category> categoryList = new ArrayList<>();
    private Activity activity;
    private static final String WHATSAPP_ID = "com.whatsapp";
    private static final String FACEBOOK_ID = "com.facebook.katana";
    private static final String MESSENGER_ID = "com.facebook.orca";
    private static final String INSTAGRAM_ID = "com.instagram.android";
    private static final String SHARE_ID = "com.android.all";
    private InterstitialAd mInterstitialAd;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayoutManager linearLayoutManagerKeywords;


    private SubscribeAdapter subscribeAdapter;
    private List<User> userList;
    private List<Slide> slideList = new ArrayList<>();
    private SlideAdapter slide_adapter;

    private List<Keyword> keywordList = new ArrayList<>();
    public TagAdapter tagAdapter;

    public QuestionAdapter questionAdapter;
    private List<Question> questionList = new ArrayList<>();

    public PostAdapter(final List<Post> postList, List<Category> categoryList, final Activity activity) {
        this.postList = postList;
        this.categoryList = categoryList;
        this.activity = activity;
        mInterstitialAd = new InterstitialAd(activity.getApplication());
        mInterstitialAd.setAdUnitId(activity.getString(R.string.ad_unit_id_interstitial));
        requestNewInterstitial();

    }


    public PostAdapter(final List<Post> postList, List<Category> categoryList, List<User> userList_, List<Slide> slideList_, final Activity activity, Boolean favorites_, Boolean downloads_) {
        this(postList, categoryList, activity);
        this.favorites = favorites_;
        this.downloads = downloads_;
        this.userList = userList_;
        this.slideList = slideList_;
    }

    public PostAdapter(final List<Post> postList, List<Category> categoryList, List<Question> questionList_, List<Slide> slideList_, final Activity activity) {
        this(postList, categoryList, activity);
        this.questionList = questionList_;
        this.slideList = slideList_;
    }

    public PostAdapter(final List<Post> postList, List<Category> categoryList, List<User> userList_, List<Slide> slideList_, List<Keyword> keywordList_, final Activity activity, Boolean favorites_, Boolean downloads_) {
        this(postList, categoryList, activity);
        this.favorites = favorites_;
        this.downloads = downloads_;
        this.userList = userList_;
        this.slideList = slideList_;
        this.keywordList = keywordList_;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {

            case 0: {
                View v0 = inflater.inflate(R.layout.item_empty, parent, false);
                viewHolder = new PostAdapter.EmptyHolder(v0);
                break;
            }
            case 1: {
                View v1 = inflater.inflate(R.layout.item_post, parent, false);
                viewHolder = new PostAdapter.PostHolder(v1);
                break;
            }
            case 2: {
                View v2 = inflater.inflate(R.layout.item_categories, parent, false);
                viewHolder = new PostAdapter.CategoriesHolder(v2);
                break;
            }

            case 3: {
                View v3 = inflater.inflate(R.layout.item_slide, parent, false);
                viewHolder = new SlideHolder(v3);
                break;
            }
            case 4: {
                View v4 = inflater.inflate(R.layout.item_facebook_ads, parent, false);
                viewHolder = new FacebookNativeHolder(v4);
                break;
            }
            case 5: {
                View v5 = inflater.inflate(R.layout.item_subscriptions, parent, false);
                viewHolder = new SubscriptionHolder(v5);
                break;
            }
            case 6: {
                View v6 = inflater.inflate(R.layout.item_tags, parent, false);
                viewHolder = new KeywordHolder(v6);
                break;
            }
            case 7: {
                View v7 = inflater.inflate(R.layout.item_questions, parent, false);
                viewHolder = new QuestionsHolder(v7);
                break;
            }
            case 8: {
                View v8 = inflater.inflate(R.layout.item_admob_native_ads, parent, false);
                viewHolder = new AdmobNativeHolder(v8);
                break;
            }
            case 9: {
                View v9 = inflater.inflate(R.layout.item_weather, parent, false);
                viewHolder = new WeatherHolder(v9);
                break;
            }
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder_parent, final int position) {
        switch (getItemViewType(position)) {
            case 0: {
                break;
            }
            case 1: {
                final PostAdapter.PostHolder holder = (PostAdapter.PostHolder) holder_parent;
                switch (postList.get(position).getType()) {
                    case "video":
                        holder.image_view_item_post_type.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_videocam));

                        break;
                    case "post":
                        holder.image_view_item_post_type.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_library_books));

                        break;
                    case "youtube":
                        holder.image_view_item_post_type.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_youtube));

                        break;
                }
                Picasso.with(activity).load(postList.get(position).getThumbnail()).placeholder(activity.getResources().getDrawable(R.drawable.placeholder)).error(activity.getResources().getDrawable(R.drawable.error_placeholder)).into(holder.image_view_post_item_image);
                Picasso.with(activity).load(postList.get(position).getUserimage()).placeholder(activity.getResources().getDrawable(R.drawable.profile)).placeholder(activity.getResources().getDrawable(R.drawable.profile)).into(holder.circle_image_view_item_post_user_image);
                if (postList.get(position).getTrusted()) {
                    holder.image_view_item_post_user_badge.setVisibility(View.VISIBLE);
                } else {
                    holder.image_view_item_post_user_badge.setVisibility(View.GONE);
                }
                if (postList.get(position).getReview()) {
                    holder.relative_layout_item_post_review.setVisibility(View.VISIBLE);
                    holder.relative_layout_item_post_delete.setVisibility(View.VISIBLE);
                } else {
                    holder.relative_layout_item_post_review.setVisibility(View.GONE);
                    holder.relative_layout_item_post_delete.setVisibility(View.GONE);
                    if (downloads) {
                        PrefManager prf = new PrefManager(activity.getApplicationContext());
                        if (prf.getString("LOGGED").toString().equals("TRUE")) {
                            String user_id = prf.getString("ID_USER");
                            if (postList.get(position).getUserid() == Integer.parseInt(user_id)) {
                                if (postList.get(position).getTrusted()) {
                                    holder.relative_layout_item_post_delete.setVisibility(View.VISIBLE);
                                } else {
                                    holder.relative_layout_item_post_delete.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                }

                holder.text_view_item_post_title.setText(postList.get(position).getTitle());
                holder.text_view_item_post_user_name.setText(postList.get(position).getUser());
                holder.text_view_item_post_comments.setText(format(postList.get(position).getComments()) + " Comments");
                holder.text_view_item_post_likes.setText(format(postList.get(position).getShares()) + " Shares");
                holder.text_view_item_post_time.setText(postList.get(position).getCreated());
                holder.text_view_item_post_views.setText(format(postList.get(position).getViews()) + " Views");
                holder.image_view_post_item_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        if (mInterstitialAd.isLoaded() && check()) {
                            mInterstitialAd.show();
                            mInterstitialAd.setAdListener(new AdListener() {
                                @Override
                                public void onAdClosed() {
                                    super.onAdClosed();
                                    Intent intent = new Intent(activity.getApplicationContext(), PostActivity.class);

                                    if (postList.get(position).getType().equals("post"))
                                        intent = new Intent(activity.getApplicationContext(), PostActivity.class);
                                    else if (postList.get(position).getType().equals("video"))
                                        intent = new Intent(activity.getApplicationContext(), VideoActivity.class);
                                    else if (postList.get(position).getType().equals("youtube"))
                                        intent = new Intent(activity.getApplicationContext(), YoutubeActivity.class);


                                    intent.putExtra("id", postList.get(position).getId());
                                    intent.putExtra("title", postList.get(position).getTitle());
                                    intent.putExtra("created", postList.get(position).getCreated());
                                    intent.putExtra("content", postList.get(position).getContent());
                                    intent.putExtra("thumbnail", postList.get(position).getThumbnail());
                                    intent.putExtra("original", postList.get(position).getOriginal());
                                    intent.putExtra("comment", postList.get(position).getComment());
                                    intent.putExtra("comments", postList.get(position).getComments());
                                    intent.putExtra("review", postList.get(position).getReview());
                                    intent.putExtra("shares", postList.get(position).getShares());
                                    intent.putExtra("trusted", postList.get(position).getTrusted());
                                    intent.putExtra("user", postList.get(position).getUser());
                                    intent.putExtra("userid", postList.get(position).getUserid());
                                    intent.putExtra("userimage", postList.get(position).getUserimage());
                                    intent.putExtra("type", postList.get(position).getType());
                                    intent.putExtra("video", postList.get(position).getVideo());


                                    activity.startActivity(intent);
                                    activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                                    requestNewInterstitial();
                                }
                            });
                        } else {
                            Intent intent = new Intent(activity.getApplicationContext(), PostActivity.class);

                            if (postList.get(position).getType().equals("post"))
                                intent = new Intent(activity.getApplicationContext(), PostActivity.class);
                            else if (postList.get(position).getType().equals("video"))
                                intent = new Intent(activity.getApplicationContext(), VideoActivity.class);
                            else if (postList.get(position).getType().equals("youtube"))
                                intent = new Intent(activity.getApplicationContext(), YoutubeActivity.class);


                            intent.putExtra("id", postList.get(position).getId());
                            intent.putExtra("title", postList.get(position).getTitle());
                            intent.putExtra("created", postList.get(position).getCreated());
                            intent.putExtra("content", postList.get(position).getContent());
                            intent.putExtra("thumbnail", postList.get(position).getThumbnail());
                            intent.putExtra("original", postList.get(position).getOriginal());
                            intent.putExtra("comment", postList.get(position).getComment());
                            intent.putExtra("comments", postList.get(position).getComments());
                            intent.putExtra("review", postList.get(position).getReview());
                            intent.putExtra("shares", postList.get(position).getShares());
                            intent.putExtra("trusted", postList.get(position).getTrusted());
                            intent.putExtra("user", postList.get(position).getUser());
                            intent.putExtra("userid", postList.get(position).getUserid());
                            intent.putExtra("userimage", postList.get(position).getUserimage());
                            intent.putExtra("type", postList.get(position).getType());
                            intent.putExtra("video", postList.get(position).getVideo());


                            activity.startActivity(intent);
                            activity.overridePendingTransition(R.anim.enter, R.anim.exit);

                        }

                    }
                });
                holder.relative_layout_item_post_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new AlertDialog.Builder(activity)
                                .setTitle("Delete Post")
                                .setMessage("Do you really want to delete this Post ?")
                                .setIcon(R.drawable.ic_delete_black)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {

                                        register_progress= ProgressDialog.show(activity, null,activity.getResources().getString(R.string.operation_progress), true);

                                        final PrefManager prefManager = new PrefManager(activity);
                                        Integer id_user = 0;
                                        String key_user = "";
                                        if (prefManager.getString("LOGGED").toString().equals("TRUE")) {
                                            id_user = Integer.parseInt(prefManager.getString("ID_USER"));
                                            key_user = prefManager.getString("TOKEN_USER");
                                        }
                                        Retrofit retrofit = apiClient.getClient();
                                        apiRest service = retrofit.create(apiRest.class);
                                        Call<Integer> call = service.deletePost(postList.get(position).getId(), id_user, key_user);
                                        call.enqueue(new Callback<Integer>() {
                                            @Override
                                            public void onResponse(Call<Integer> call, retrofit2.Response<Integer> response) {
                                                register_progress.dismiss();
                                                if (response.isSuccessful()){
                                                    Toasty.success(activity,activity.getResources().getString(R.string.post_delete_success),Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(activity,UserActivity.class);
                                                    intent.putExtra("id", postList.get(position).getUserid());
                                                    intent.putExtra("name", postList.get(position).getUser());
                                                    intent.putExtra("image", postList.get(position).getUserimage());
                                                    activity.startActivity(intent);
                                                    activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                                                }else{
                                                    Toasty.error(activity,activity.getResources().getString(R.string.post_delete_failed),Toast.LENGTH_LONG).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Integer> call, Throwable t) {
                                                Toasty.error(activity,activity.getResources().getString(R.string.post_delete_failed),Toast.LENGTH_LONG).show();
                                                register_progress.dismiss();
                                            }
                                        });


                                    }
                                })
                                .setNegativeButton(android.R.string.no, null).show();

                    }
                });
                List<Post> favorites_list = Hawk.get("favorite");
                Boolean exist = false;
                if (favorites_list == null) {
                    favorites_list = new ArrayList<>();
                }

                for (int i = 0; i < favorites_list.size(); i++) {
                    if (favorites_list.get(i).getId().equals(postList.get(position).getId())) {
                        exist = true;
                    }
                }
                if (exist) {
                    holder.image_view_item_post_fav.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_favorite_black));
                } else {
                    holder.image_view_item_post_fav.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_favorite_border));

                }
                holder.image_view_item_post_fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<Post> favorites_list = Hawk.get("favorite");
                        Boolean exist = false;
                        if (favorites_list == null) {
                            favorites_list = new ArrayList<>();
                        }
                        int fav_position = -1;
                        for (int i = 0; i < favorites_list.size(); i++) {
                            if (favorites_list.get(i).getId().equals(postList.get(position).getId())) {
                                exist = true;
                                fav_position = i;
                            }
                        }
                        if (exist == false) {
                            favorites_list.add(postList.get(position));
                            Hawk.put("favorite", favorites_list);
                            holder.image_view_item_post_fav.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_favorite_black));

                        } else {
                            favorites_list.remove(fav_position);
                            Hawk.put("favorite", favorites_list);
                            holder.image_view_item_post_fav.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_favorite_border));
                            if (favorites) {
                                postList.remove(position);
                                notifyItemRemoved(position);
                                notifyDataSetChanged();
                            }

                        }

                    }
                });
                break;
            }
            case 2: {
                final PostAdapter.CategoriesHolder holder = (PostAdapter.CategoriesHolder) holder_parent;
                holder.categoryVideoAdapter.notifyDataSetChanged();
                break;
            }
            case 3: {
                final SlideHolder holder = (SlideHolder) holder_parent;
                slide_adapter = new SlideAdapter(activity, slideList);
                holder.view_pager_slide.setAdapter(slide_adapter);
                holder.view_pager_slide.setOffscreenPageLimit(1);

                holder.view_pager_slide.setClipToPadding(false);
                holder.view_pager_slide.setPageMargin(0);
                holder.view_pager_indicator.setupWithViewPager(holder.view_pager_slide);
                holder.view_pager_slide.setCurrentItem(slideList.size() / 2);
                slide_adapter.notifyDataSetChanged();
                break;
            }
            case 4: {

                break;
            }
            case 5: {
                final SubscriptionHolder holder = (SubscriptionHolder) holder_parent;
                this.linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
                this.subscribeAdapter = new SubscribeAdapter(userList, activity);
                holder.recycle_view_follow_items.setHasFixedSize(true);
                holder.recycle_view_follow_items.setAdapter(subscribeAdapter);
                holder.recycle_view_follow_items.setLayoutManager(linearLayoutManager);
                subscribeAdapter.notifyDataSetChanged();
                break;
            }
            case 6: {
                final KeywordHolder holder = (KeywordHolder) holder_parent;
                this.tagAdapter = new TagAdapter(keywordList, activity);
                this.linearLayoutManagerKeywords = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
                holder.recycle_view_tags_items.setHasFixedSize(true);
                holder.recycle_view_tags_items.setAdapter(tagAdapter);
                holder.recycle_view_tags_items.setLayoutManager(linearLayoutManagerKeywords);

                tagAdapter.notifyDataSetChanged();
                break;
            }
            case 9: {
                final WeatherHolder holder = (WeatherHolder) holder_parent;
                PrefManager prefManager= new PrefManager(activity.getApplicationContext());

                    findLocation();
                        holder.weather_widget_image_view_maker.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                oldlatitude="null";
                                oldlongitude="null";
                                loadedWeather =  false;
                                loadedWeatherFiveDay= false;
                                findLocation();
                            }
                        });
                        if (weather!=null) {
                            String units="°C";
                            PrefManager prf= new PrefManager(activity);
                            if (!prf.getString("units").equals("metric") ){
                                units="°F";
                            }
                            else if (!prf.getString("units").equals("imperial") ){
                                units="°C";
                            }
                            holder.weather_widget_text_view_city.setText(weather.getName());
                            holder.weather_widget_text_view_main.setText(weather.getWeather().get(0).getMain());
                            holder.weather_widget_image_view_icon.setImageResource(weather.getWeather().get(0).getIconLocal());
                            holder.weather_widget_text_view_temp.setText(weather.getMain().getTemp()+units);
                        }
                        if (_weatherResponse!=null){
                            Calendar calendar = Calendar.getInstance();
                            Date today = calendar.getTime();

                            calendar.add(Calendar.DAY_OF_YEAR, 1);
                            Integer DAY_1=calendar.get(Calendar.DAY_OF_WEEK);
                            holder.weather_widget_image_view_day_1.setImageResource(_weatherResponse.getList().get(1).getWeather().get(0).getIconLocal());
                            holder.weather_widget_text_view_day_1.setText(days[DAY_1-1]);
                            holder.weather_widget_text_view_max_day_1.setText(_weatherResponse.getList().get(1).getTemp().getMax()+'°');
                            holder.weather_widget_text_view_min_day_1.setText(_weatherResponse.getList().get(1).getTemp().getMin()+'°');

                            calendar.add(Calendar.DAY_OF_YEAR, 1);
                            Integer DAY_2=calendar.get(Calendar.DAY_OF_WEEK);
                            holder.weather_widget_image_view_day_2.setImageResource(_weatherResponse.getList().get(2).getWeather().get(0).getIconLocal());
                            holder.weather_widget_text_view_day_2.setText(days[DAY_2-1]);
                            holder.weather_widget_text_view_max_day_2.setText(_weatherResponse.getList().get(2).getTemp().getMax()+'°');
                            holder.weather_widget_text_view_min_day_2.setText(_weatherResponse.getList().get(2).getTemp().getMin()+'°');

                            calendar.add(Calendar.DAY_OF_YEAR, 1);
                            Integer DAY_3=calendar.get(Calendar.DAY_OF_WEEK);
                            holder.weather_widget_image_view_day_3.setImageResource(_weatherResponse.getList().get(3).getWeather().get(0).getIconLocal());
                            holder.weather_widget_text_view_day_3.setText(days[DAY_3-1]);
                            holder.weather_widget_text_view_max_day_3.setText(_weatherResponse.getList().get(3).getTemp().getMax()+'°');
                            holder.weather_widget_text_view_min_day_3.setText(_weatherResponse.getList().get(3).getTemp().getMin()+'°');

                            calendar.add(Calendar.DAY_OF_YEAR, 1);
                            Integer DAY_4=calendar.get(Calendar.DAY_OF_WEEK);
                            holder.weather_widget_image_view_day_4.setImageResource(_weatherResponse.getList().get(4).getWeather().get(0).getIconLocal());
                            holder.weather_widget_text_view_day_4.setText(days[DAY_4-1]);
                            holder.weather_widget_text_view_max_day_4.setText(_weatherResponse.getList().get(4).getTemp().getMax()+'°');
                            holder.weather_widget_text_view_min_day_4.setText(_weatherResponse.getList().get(4).getTemp().getMin()+'°');


                            calendar.add(Calendar.DAY_OF_YEAR, 1);
                            Integer DAY_5=calendar.get(Calendar.DAY_OF_WEEK);
                            holder.weather_widget_image_view_day_5.setImageResource(_weatherResponse.getList().get(5).getWeather().get(0).getIconLocal());
                            holder.weather_widget_text_view_day_5.setText(days[DAY_5-1]);
                            holder.weather_widget_text_view_max_day_5.setText(_weatherResponse.getList().get(5).getTemp().getMax()+'°');
                            holder.weather_widget_text_view_min_day_5.setText(_weatherResponse.getList().get(5).getTemp().getMin()+'°');
                        }

                break;
            }
        }


    }


    public static class PostHolder extends RecyclerView.ViewHolder {
        private final TextView text_view_item_post_title;
        private final ImageView image_view_item_post_user_badge;
        private final CircleImageView circle_image_view_item_post_user_image;
        private final TextView text_view_item_post_user_name;
        private final TextView text_view_item_post_likes;
        private final TextView text_view_item_post_time;
        private final TextView text_view_item_post_views;
        private final TextView text_view_item_post_comments;
        private final RelativeLayout relative_layout_item_post_review;
        private final ImageView image_view_post_item_image;
        private final ImageView image_view_item_post_fav;
        private final ImageView image_view_item_post_type;
        private final RelativeLayout relative_layout_item_post_delete;

        public PostHolder(View itemView) {
            super(itemView);
            this.relative_layout_item_post_delete = (RelativeLayout) itemView.findViewById(R.id.relative_layout_item_post_delete);
            this.relative_layout_item_post_review = (RelativeLayout) itemView.findViewById(R.id.relative_layout_item_post_review);
            this.text_view_item_post_comments = (TextView) itemView.findViewById(R.id.text_view_item_post_comments);
            this.text_view_item_post_views = (TextView) itemView.findViewById(R.id.text_view_item_post_views);
            this.text_view_item_post_likes = (TextView) itemView.findViewById(R.id.text_view_item_post_likes);
            this.text_view_item_post_time = (TextView) itemView.findViewById(R.id.text_view_item_post_time);
            this.text_view_item_post_title = (TextView) itemView.findViewById(R.id.text_view_item_post_title);
            this.text_view_item_post_user_name = (TextView) itemView.findViewById(R.id.text_view_item_post_user_name);
            this.image_view_item_post_fav = (ImageView) itemView.findViewById(R.id.image_view_item_post_fav);
            this.image_view_post_item_image = (ImageView) itemView.findViewById(R.id.image_view_post_item_image);
            this.image_view_item_post_user_badge = (ImageView) itemView.findViewById(R.id.image_view_item_post_user_badge);
            this.image_view_item_post_type = (ImageView) itemView.findViewById(R.id.image_view_item_post_type);
            this.circle_image_view_item_post_user_image = (CircleImageView) itemView.findViewById(R.id.circle_image_view_item_post_user_image);
        }
    }

    public class CategoriesHolder extends RecyclerView.ViewHolder {
        private final LinearLayoutManager linearLayoutManager;
        private final CategoryAdapter categoryVideoAdapter;
        public RecyclerView recycler_view_item_categories;

        public CategoriesHolder(View view) {
            super(view);
            this.recycler_view_item_categories = (RecyclerView) itemView.findViewById(R.id.recycler_view_item_categories);
            this.linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
            this.categoryVideoAdapter = new CategoryAdapter(categoryList, activity);
            recycler_view_item_categories.setHasFixedSize(true);
            recycler_view_item_categories.setAdapter(categoryVideoAdapter);
            recycler_view_item_categories.setLayoutManager(linearLayoutManager);
        }
    }

    public class EmptyHolder extends RecyclerView.ViewHolder {
        public EmptyHolder(View view) {
            super(view);
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return postList.get(position).getViewType();
    }

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();

    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public static String format(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    public boolean check() {
        PrefManager prf = new PrefManager(activity.getApplicationContext());
        if (!prf.getString("SUBSCRIBED").equals("FALSE")) {
            return false;
        }
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(c.getTime());

        if (prf.getString("LAST_DATE_ADS").equals("")) {
            prf.setString("LAST_DATE_ADS", strDate);
        } else {
            String toyBornTime = prf.getString("LAST_DATE_ADS");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            try {
                Date oldDate = dateFormat.parse(toyBornTime);
                System.out.println(oldDate);

                Date currentDate = new Date();

                long diff = currentDate.getTime() - oldDate.getTime();
                long seconds = diff / 1000;

                if (seconds > Integer.parseInt(activity.getResources().getString(R.string.AD_MOB_TIME))) {
                    prf.setString("LAST_DATE_ADS", strDate);
                    return true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public class AdmobNativeHolder extends RecyclerView.ViewHolder {
        private UnifiedNativeAd nativeAd;
        private FrameLayout frameLayout;

        public AdmobNativeHolder(@NonNull View itemView) {
            super(itemView);

            frameLayout = (FrameLayout) itemView.findViewById(R.id.fl_adplaceholder);
            AdLoader.Builder builder = new AdLoader.Builder(activity, activity.getResources().getString(R.string.ad_unit_id_native));

            builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                // OnUnifiedNativeAdLoadedListener implementation.
                @Override
                public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                    // You must call destroy on old ads when you are done with them,
                    // otherwise you will have a memory leak.
                    if (nativeAd != null) {
                        nativeAd.destroy();
                    }
                    nativeAd = unifiedNativeAd;

                    UnifiedNativeAdView adView = (UnifiedNativeAdView) activity.getLayoutInflater()
                            .inflate(R.layout.ad_unified, null);
                    populateUnifiedNativeAdView(unifiedNativeAd, adView);
                    frameLayout.removeAllViews();
                    frameLayout.addView(adView);
                }

            });

            VideoOptions videoOptions = new VideoOptions.Builder()
                    .setStartMuted(true)
                    .build();

            NativeAdOptions adOptions = new NativeAdOptions.Builder()
                    .setVideoOptions(videoOptions)
                    .build();

            builder.withNativeAdOptions(adOptions);

            AdLoader adLoader = builder.withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int errorCode) {


                }
            }).build();

            adLoader.loadAds(new AdRequest.Builder().build(), 3);
        }
    }


    /**
     * Populates a {@link UnifiedNativeAdView} object with data from a given
     * {@link UnifiedNativeAd}.
     *
     * @param nativeAd the object containing the ad's assets
     * @param adView          the view to be populated
     */
    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        // Set the media view. Media content will be automatically populated in the media view once
        // adView.setNativeAd() is called.
        com.google.android.gms.ads.formats.MediaView mediaView = adView.findViewById(R.id.ad_media);

        mediaView.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                if (child instanceof ImageView) {
                    ImageView imageView = (ImageView) child;
                    imageView.setAdjustViewBounds(true);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {
            }
        });
        adView.setMediaView(mediaView);

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline is guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad. The SDK will populate the adView's MediaView
        // with the media content from this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {
            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.
                    super.onVideoEnd();
                }
            });
        } else {

        }
    }

    public class FacebookNativeHolder extends RecyclerView.ViewHolder {
        private final String TAG = "WALLPAPERADAPTER";
        private LinearLayout nativeAdContainer;
        private LinearLayout adView;
        private NativeAd nativeAd;

        public FacebookNativeHolder(View view) {
            super(view);
            loadNativeAd(view);
        }

        private void loadNativeAd(final View view) {
            nativeAd = new NativeAd(activity, activity.getString(R.string.FACEBOOK_ADS_NATIVE_PLACEMENT_ID));
            nativeAd.setAdListener(new NativeAdListener() {
                @Override
                public void onMediaDownloaded(Ad ad) {
                    Log.e(TAG, "Native ad finished downloading all assets.");
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage());
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    Log.d(TAG, "Native ad is loaded and ready to be displayed!");
                    // Race condition, load() called again before last ad was displayed
                    if (nativeAd == null || nativeAd != ad) {
                        return;
                    }
                   /* NativeAdViewAttributes viewAttributes = new NativeAdViewAttributes()
                            .setBackgroundColor(activity.getResources().getColor(R.color.colorPrimaryDark))
                            .setTitleTextColor(Color.WHITE)
                            .setDescriptionTextColor(Color.WHITE)
                            .setButtonColor(Color.WHITE);

                    View adView = NativeAdView.render(activity, nativeAd, NativeAdView.Type.HEIGHT_300, viewAttributes);

                    LinearLayout nativeAdContainer = (LinearLayout) view.findViewById(R.id.native_ad_container);
                    nativeAdContainer.addView(adView);*/
                    // Inflate Native Ad into Container
                    inflateAd(nativeAd, view);
                }

                @Override
                public void onAdClicked(Ad ad) {
                    // Native ad clicked
                    Log.d(TAG, "Native ad clicked!");
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    // Native ad impression
                    Log.d(TAG, "Native ad impression logged!");
                }
            });

            // Request an ad
            nativeAd.loadAd();
        }

        private void inflateAd(NativeAd nativeAd, View view) {

            nativeAd.unregisterView();

            // Add the Ad view into the ad container.
            nativeAdContainer = view.findViewById(R.id.native_ad_container);
            LayoutInflater inflater = LayoutInflater.from(activity);
            // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
            adView = (LinearLayout) inflater.inflate(R.layout.native_ad_layout_1, nativeAdContainer, false);
            nativeAdContainer.addView(adView);

            // Add the AdChoices icon
            LinearLayout adChoicesContainer = view.findViewById(R.id.ad_choices_container);
            AdChoicesView adChoicesView = new AdChoicesView(activity, nativeAd, true);
            adChoicesContainer.addView(adChoicesView, 0);

            // Create native UI using the ad metadata.
            AdIconView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
            TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
            MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
            TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
            TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
            TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
            Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

            // Set the Text.
            nativeAdTitle.setText(nativeAd.getAdvertiserName());
            nativeAdBody.setText(nativeAd.getAdBodyText());
            nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
            nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
            nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
            sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

            // Create a list of clickable views
            List<View> clickableViews = new ArrayList<>();
            clickableViews.add(nativeAdTitle);
            clickableViews.add(nativeAdCallToAction);

            // Register the Title and CTA button to listen for clicks.
            nativeAd.registerViewForInteraction(
                    adView,
                    nativeAdMedia,
                    nativeAdIcon,
                    clickableViews);
        }

    }

    public static class SubscriptionHolder extends RecyclerView.ViewHolder {
        private final RecyclerView recycle_view_follow_items;

        public SubscriptionHolder(View view) {
            super(view);
            recycle_view_follow_items = (RecyclerView) itemView.findViewById(R.id.recycle_view_follow_items);
        }
    }

    private class SlideHolder extends RecyclerView.ViewHolder {
        private final ViewPagerIndicator view_pager_indicator;
        private final ClickableViewPager view_pager_slide;

        public SlideHolder(View itemView) {
            super(itemView);
            this.view_pager_indicator = (ViewPagerIndicator) itemView.findViewById(R.id.view_pager_indicator);
            this.view_pager_slide = (ClickableViewPager) itemView.findViewById(R.id.view_pager_slide);
        }
    }

    private class KeywordHolder extends RecyclerView.ViewHolder {
        private final RecyclerView recycle_view_tags_items;

        public KeywordHolder(View view) {
            super(view);
            this.recycle_view_tags_items = (RecyclerView) view.findViewById(R.id.recycle_view_tags_items);


        }
    }

    private class WeatherHolder extends RecyclerView.ViewHolder {


        private ImageView weather_widget_image_view_icon;
        private ImageView weather_widget_image_view_day_1;
        private ImageView weather_widget_image_view_day_2;
        private ImageView weather_widget_image_view_day_3;
        private ImageView weather_widget_image_view_day_4;
        private ImageView weather_widget_image_view_day_5;
        private ImageView weather_widget_image_view_maker;

        private TextView weather_widget_text_view_city;
        private TextView weather_widget_text_view_temp;
        private TextView weather_widget_text_view_main;

        private TextView weather_widget_text_view_day_1;
        private TextView weather_widget_text_view_day_3;
        private TextView weather_widget_text_view_day_2;
        private TextView weather_widget_text_view_day_4;
        private TextView weather_widget_text_view_day_5;

        private TextView weather_widget_text_view_max_day_5;
        private TextView weather_widget_text_view_max_day_4;
        private TextView weather_widget_text_view_min_day_5;
        private TextView weather_widget_text_view_min_day_4;
        private TextView weather_widget_text_view_max_day_3;
        private TextView weather_widget_text_view_min_day_3;
        private TextView weather_widget_text_view_min_day_1;
        private TextView weather_widget_text_view_max_day_2;
        private TextView weather_widget_text_view_max_day_1;
        private TextView weather_widget_text_view_min_day_2;
        private CardView card_view_weather;


        public WeatherHolder(View view) {
            super(view);

            this.card_view_weather=(CardView)  view.findViewById(R.id.card_view_weather);
            this.weather_widget_image_view_icon=(ImageView)  view.findViewById(R.id.weather_widget_image_view_icon);
            this.weather_widget_image_view_day_1=(ImageView) view.findViewById(R.id.weather_widget_image_view_day_1);
            this.weather_widget_image_view_day_2=(ImageView) view.findViewById(R.id.weather_widget_image_view_day_2);
            this.weather_widget_image_view_day_3=(ImageView) view.findViewById(R.id.weather_widget_image_view_day_3);
            this.weather_widget_image_view_day_4=(ImageView) view.findViewById(R.id.weather_widget_image_view_day_4);
            this.weather_widget_image_view_day_5=(ImageView) view.findViewById(R.id.weather_widget_image_view_day_5);
            this.weather_widget_image_view_maker=(ImageView) view.findViewById(R.id.weather_widget_image_view_maker);

            this.weather_widget_text_view_city=(TextView) view.findViewById(R.id.weather_widget_text_view_city);
            this.weather_widget_text_view_temp=(TextView) view.findViewById(R.id.weather_widget_text_view_temp);
            this.weather_widget_text_view_main=(TextView) view.findViewById(R.id.weather_widget_text_view_main);

            this.weather_widget_text_view_day_1=(TextView) view.findViewById(R.id.weather_widget_text_view_day_1);
            this.weather_widget_text_view_day_2=(TextView) view.findViewById(R.id.weather_widget_text_view_day_2);
            this.weather_widget_text_view_day_3=(TextView) view.findViewById(R.id.weather_widget_text_view_day_3);
            this.weather_widget_text_view_day_4=(TextView) view.findViewById(R.id.weather_widget_text_view_day_4);
            this.weather_widget_text_view_day_5=(TextView) view.findViewById(R.id.weather_widget_text_view_day_5);

            this.weather_widget_text_view_max_day_5=(TextView) view.findViewById(R.id.weather_widget_text_view_max_day_5);
            this.weather_widget_text_view_min_day_5=(TextView) view.findViewById(R.id.weather_widget_text_view_min_day_5);
            this.weather_widget_text_view_max_day_4=(TextView) view.findViewById(R.id.weather_widget_text_view_max_day_4);
            this.weather_widget_text_view_min_day_4=(TextView) view.findViewById(R.id.weather_widget_text_view_min_day_4);
            this.weather_widget_text_view_max_day_3=(TextView) view.findViewById(R.id.weather_widget_text_view_max_day_3);
            this.weather_widget_text_view_min_day_3=(TextView) view.findViewById(R.id.weather_widget_text_view_min_day_3);
            this.weather_widget_text_view_max_day_2=(TextView) view.findViewById(R.id.weather_widget_text_view_max_day_2);
            this.weather_widget_text_view_min_day_2=(TextView) view.findViewById(R.id.weather_widget_text_view_min_day_2);
            this.weather_widget_text_view_max_day_1=(TextView) view.findViewById(R.id.weather_widget_text_view_max_day_1);
            this.weather_widget_text_view_min_day_1=(TextView) view.findViewById(R.id.weather_widget_text_view_min_day_1);



        }
    }
   public void getWeatherFiveDay(String longitude,String latitude,String units){
        Retrofit retrofit = weatherClient.getClient();
        apiWeather service = retrofit.create(apiWeather.class);
        retrofit2.Call<weatherResponse> call = service.getFiveDayWeather(latitude,longitude,"json",Global.KEY_APP_WEATHER,units);
        call.enqueue(new Callback<weatherResponse>() {
            @Override
            public void onResponse(Call<weatherResponse> call, Response<weatherResponse> response) {
                if (response.isSuccessful()){
                    if(response.body().getCod().equals("200")){
                        _weatherResponse = response.body();
                        notifyDataSetChanged();
                        loadedWeatherFiveDay =  true;

                    }
                }
            }
            @Override
            public void onFailure(Call<weatherResponse> call, Throwable t) {
                // Toast.makeText(getApplicationContext(),t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void getCurrentWeather(final  String longitude,final String latitude,final String units){

        Retrofit retrofit = weatherClient.getClient();
        apiWeather service = retrofit.create(apiWeather.class);
        retrofit2.Call<weatherResponseCurrent> call = service.getCurrentWeather(latitude,longitude,"json", Global.KEY_APP_WEATHER,units);
        call.enqueue(new Callback<weatherResponseCurrent>() {
            @Override
            public void onResponse(Call<weatherResponseCurrent> call, Response<weatherResponseCurrent> response) {
                if (response.isSuccessful()){
                    loadedWeather =  true;
                    weather =  response.body();
                    notifyDataSetChanged();

                }else{
                    Toast.makeText(activity,"error", Toast.LENGTH_LONG).show();

                }
                if (!loadedWeatherFiveDay)
                    getWeatherFiveDay(longitude,latitude,units);
            }

            @Override
            public void onFailure(Call<weatherResponseCurrent> call, Throwable t) {
                 Toast.makeText(activity,t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void findLocation() {
        FusedLocationProviderClient fusedLocationClient;
        PrefManager prf= new PrefManager(activity);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);


        String latitude="";
        String longitude="";
        String units="metric";
        GPSTracker gps = new GPSTracker(activity);

        if(gps.canGetLocation()){
            latitude = gps.getLatitude()+"";
            longitude = gps.getLongitude()+"";
            prf.setString("latitude",latitude);
            prf.setString("longitude",longitude);
        }else{
            if (prf.getString("latitude").equals("") || prf.getString("longitude").equals("")){
                gps.showSettingsAlert();
                Toast.makeText(activity, "no gps", Toast.LENGTH_SHORT).show();
            }else{
                latitude    =  prf.getString("latitude");
                longitude   =  prf.getString("longitude");
            }
        }

        if (!prf.getString("units").equals("") ){
            units=prf.getString("units");
        }

        //Toast.makeText(activity, latitude+" "+ longitude, Toast.LENGTH_SHORT).show();
        // check if GPS enabled
        Log.v("LOCATION",oldlongitude+"!="+longitude +"||"+ oldlatitude+"!="+latitude);

        if (!oldlongitude.equals(longitude) || !oldlatitude.equals(latitude) || !units.equals(oldunit)){
            loadedWeather=false;
            loadedWeatherFiveDay=false;
            oldlongitude=longitude;
            oldlatitude=latitude;
            oldunit=units;
        }

        if (!loadedWeather)
            getCurrentWeather(longitude,latitude,units);


    }
    private class QuestionsHolder extends RecyclerView.ViewHolder {

        private final LinearLayoutManager linearLayoutManager;
        private  RecyclerView recycler_view_item_questions;

        public QuestionsHolder(View itemView) {
            super(itemView);
            questionAdapter=new QuestionAdapter(questionList,activity);
            this.recycler_view_item_questions =(RecyclerView) itemView.findViewById(R.id.recycler_view_item_questions);
            this.linearLayoutManager=  new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false);
            this.recycler_view_item_questions.setHasFixedSize(true);
            this.recycler_view_item_questions.setAdapter(questionAdapter);
            this.recycler_view_item_questions.setLayoutManager(linearLayoutManager);
        }
    }

}
