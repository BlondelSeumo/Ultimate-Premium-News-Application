package com.virmana.news_app.ui.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.leo.simplearcloader.SimpleArcLoader;
import com.orhanobut.hawk.Hawk;
import com.squareup.picasso.Picasso;
import com.virmana.news_app.Adapters.CommentAdapter;
import com.virmana.news_app.Provider.PrefManager;
import com.virmana.news_app.R;
import com.virmana.news_app.api.apiClient;
import com.virmana.news_app.api.apiRest;
import com.virmana.news_app.config.Global;
import com.virmana.news_app.model.ApiResponse;
import com.virmana.news_app.model.Comment;
import com.virmana.news_app.model.Post;

import java.io.UnsupportedEncodingException;
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


public class VideoActivity extends AppCompatActivity {

    private ImageView image_view_post_guide_activity;
    private ImageView image_view_post_activity_comments;
    private TextView text_view_post_activity_views;
    private TextView text_view_post_activity_time;
    private TextView text_view_post_activity_title;
    private CircleImageView circle_image_view_post_activity_user;
    private ImageView image_view_status_verified;
    private TextView text_view_post_activity_name_user;
    private Button button_follow_user_post_activity;
    private WebView web_view_post_activity_content;
    private ImageView image_view_post_activity_fav;
    private ImageView image_view_post_activity_like;
    private ImageView image_view_post_activity_share;

    private InterstitialAd mInterstitialAdDownload;


    private AppCompatRatingBar rating_bar_post_main_post_activity;
    private AppCompatRatingBar rating_bar_post_value_post_activity;
    private RatingBar rating_bar_post_1_post_activity;
    private RatingBar rating_bar_post_2_post_activity;
    private RatingBar rating_bar_post_3_post_activity;
    private RatingBar rating_bar_post_4_post_activity;
    private RatingBar rating_bar_post_5_post_activity;
    private TextView text_view_rate_1_post_activity;
    private TextView text_view_rate_2_post_activity;
    private TextView text_view_rate_3_post_activity;
    private TextView text_view_rate_4_post_activity;
    private TextView text_view_rate_5_post_activity;
    private ProgressBar progress_bar_rate_1_post_activity;
    private ProgressBar progress_bar_rate_2_post_activity;
    private ProgressBar progress_bar_rate_3_post_activity;
    private ProgressBar progress_bar_rate_4_post_activity;
    private ProgressBar progress_bar_rate_5_post_activity;
    private TextView text_view_rate_main_post_activity;


    private RelativeLayout relative_layout_comment_section;
    private EditText edit_text_comment_add;
    private ProgressBar progress_bar_comment_add;
    private ProgressBar progress_bar_comment_list;
    private ImageView image_button_comment_add;
    private RecyclerView recycle_view_comment;
    private ImageView imageView_empty_comment;
    private TextView text_view_comment_box_count;
    private ImageView image_view_comment_box_close;

    private ArrayList<Comment> commentList= new ArrayList<>();
    private CommentAdapter commentAdapter;
    private LinearLayoutManager linearLayoutManagerCOmment;
    private LinearLayoutManager linearLayoutManagerParts;
    private String from;
    private Post post;
    private RelativeLayout relative_layout_post_activity_comments;
    private ScrollView scroll_view_post_activity;
    private RelativeLayout relative_layout_dialog_top;
    private ImageView image_view_post_activity_edit;
    private RelativeLayout relative_layout_rating;

    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private BandwidthMeter bandwidthMeter;
    private DataSource.Factory mediaDataSourceFactory;
    private ImageView ivHideControllerButton;
    private Timeline.Window window;
    private SimpleArcLoader simple_arc_loader_lang_player;
    private SimpleArcLoader simple_arc_loader_exo;
    private LinearLayout linear_layout_exo_replay;
    private ImageView exo_pause;
    private ImageView exo_play;
    private ImageView exo_replay;

    private boolean itsEnded = false;
    private ImageView exo_back_image_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);



        Bundle bundle = getIntent().getExtras() ;
        post =  new Post();
        post.setId(bundle.getInt("id"));
        post.setComment(bundle.getBoolean("comment"));
        post.setComments(bundle.getInt("comments"));
        post.setContent(bundle.getString("content"));
        post.setCreated(bundle.getString("created"));
        post.setShares(bundle.getInt("shares"));
        post.setOriginal(bundle.getString("original"));
        post.setReview(bundle.getBoolean("review"));
        post.setThumbnail(bundle.getString("thumbnail"));
        post.setTitle(bundle.getString("title"));
        post.setTrusted(bundle.getBoolean("trusted"));
        post.setType(bundle.getString("type"));
        post.setUser(bundle.getString("user"));
        post.setUserid(bundle.getInt("userid"));
        post.setUserimage(bundle.getString("userimage"));
        post.setVideo(bundle.getString("video"));
        post.setViews(bundle.getInt("views"));
        this.from =  bundle.getString("from");


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        initAction();



        getUser();
        getRating(post.getId());

        relative_layout_rating.setVisibility(View.VISIBLE);
        image_view_post_activity_fav.setVisibility(View.VISIBLE);
        image_view_post_activity_share.setVisibility(View.VISIBLE);
        image_view_post_activity_like.setVisibility(View.VISIBLE);
        image_view_post_activity_comments.setVisibility(View.VISIBLE);
        image_view_post_activity_edit.setVisibility(View.GONE);


        PrefManager prf= new PrefManager(VideoActivity.this.getApplicationContext());
        String user_id = prf.getString("ID_USER");
        if (prf.getString("LOGGED").toString().equals("TRUE")) {
            if (post.getUserid() == Integer.parseInt(user_id)) {
                if (post.getReview()) {
                    image_view_post_activity_edit.setVisibility(View.VISIBLE);
                }else{
                    image_view_post_activity_edit.setVisibility(View.GONE);
                }
            }
        }

        initGuide();
        if (!post.getReview()){
            addView();
        }
        showAdsBanner();
        initAds();
        initInterstitial();
    }
    @Override
    public void onBackPressed(){
        if (relative_layout_post_activity_comments.getVisibility() == View.VISIBLE){
            showCommentBox();
            return;
        }
        if (from!=null) {
            Intent intent = new Intent(VideoActivity.this,MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
            finish();
        }else{
            super.onBackPressed();
            overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
        }
        return;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                if (from!=null){
                    Intent intent = new Intent(VideoActivity.this,MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
                    finish();
                }else{
                    super.onBackPressed();
                    overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void initializePlayer() {
        if (!post.getType().equals("video"))
            return;

        exo_pause.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
        exo_play.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);




        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.video_view_activity_video);
        simpleExoPlayerView.requestFocus();
        simpleExoPlayerView.setVisibility(View.VISIBLE);
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        simpleExoPlayerView.setControllerVisibilityListener(new PlaybackControlView.VisibilityListener() {
            @Override
            public void onVisibilityChange(int visibility) {
                if(itsEnded){
                    simpleExoPlayerView.showController();
                    itsEnded =  false;
                    exo_pause.setVisibility(View.GONE);
                }
            }
        });
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        simpleExoPlayerView.setControllerShowTimeoutMs(10000);
        player = ExoPlayerFactory.newSimpleInstance(VideoActivity.this, trackSelector);

        simpleExoPlayerView.setPlayer(player);
        simpleExoPlayerView.setControllerAutoShow(false);

        player.setPlayWhenReady(shouldAutoPlay);
/*        MediaSource mediaSource = new HlsMediaSource(Uri.parse("https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8"),
                mediaDataSourceFactory, mainHandler, null);*/
        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(post.getOriginal()),
                mediaDataSourceFactory, extractorsFactory, null, null);


        player.prepare(mediaSource);
        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == ExoPlayer.STATE_READY){
                    simple_arc_loader_exo.setVisibility(View.GONE);
                    linear_layout_exo_replay.setVisibility(View.GONE);
                    simpleExoPlayerView.setControllerShowTimeoutMs(3000);

                    addView();
                }
                if (playbackState == ExoPlayer.STATE_BUFFERING){
                    simple_arc_loader_exo.setVisibility(View.VISIBLE);
                    linear_layout_exo_replay.setVisibility(View.GONE);
                    exo_pause.setVisibility(View.GONE);

                }
                if (playbackState == ExoPlayer.STATE_ENDED){
                    linear_layout_exo_replay.setVisibility(View.VISIBLE);
                    simple_arc_loader_exo.setVisibility(View.GONE);
                    simpleExoPlayerView.showController();
                    simpleExoPlayerView.setControllerAutoShow(false);
                    exo_pause.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.transparent), android.graphics.PorterDuff.Mode.SRC_IN);
                    exo_play.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.transparent), android.graphics.PorterDuff.Mode.SRC_IN);
                    simpleExoPlayerView.setControllerShowTimeoutMs(0);
                    itsEnded =  true;
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity() {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }
        });
        ivHideControllerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(VideoActivity.this,FullVideoActivity.class);
                intent.putExtra("duration", player.getContentPosition()+"");
                intent.putExtra("video",post.getOriginal());
                startActivity(intent);

            }
        });


    }
    private void releasePlayer() {
        if (player != null) {
            shouldAutoPlay = player.getPlayWhenReady();
            player.release();
            player = null;
            trackSelector = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
     /*   if (Util.SDK_INT > 23) {
            initializePlayer();
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        checkPermission();
        initializePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();

    }
    public void checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(VideoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)  != PackageManager.PERMISSION_GRANTED) {


                if (ActivityCompat.shouldShowRequestPermissionRationale(VideoActivity.this,   Manifest.permission.READ_CONTACTS)) {
                    Intent intent_image  =  new Intent(VideoActivity.this.getApplicationContext(), PermissionActivity.class);
                    startActivity(intent_image);
                    VideoActivity.this.overridePendingTransition(R.anim.enter, R.anim.exit);
                    VideoActivity.this.finish();
                } else {
                    Intent intent_image  =  new Intent(VideoActivity.this.getApplicationContext(), PermissionActivity.class);
                    startActivity(intent_image);
                    VideoActivity.this.overridePendingTransition(R.anim.enter, R.anim.exit);
                    VideoActivity.this.finish();
                }
            }

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();

    }
    private void initGuide() {

        Picasso.with(this).load(post.getThumbnail()).placeholder(getResources().getDrawable(R.drawable.placeholder)).error(getResources().getDrawable(R.drawable.error_placeholder)).into(image_view_post_guide_activity);
        Picasso.with(this).load(post.getUserimage()).placeholder(getResources().getDrawable(R.drawable.profile)).placeholder(getResources().getDrawable(R.drawable.profile)).into(circle_image_view_post_activity_user);
        if (post.getTrusted()){
            image_view_status_verified.setVisibility(View.VISIBLE);
        }else{
            image_view_status_verified.setVisibility(View.GONE);
        }
        String content_article ="<style type=\"text/css\">img{background-color:#ccc !important;min-height:100px !important;max-width:100%  !important;border-radius: 5px !important;  box-shadow: 0px 5px 5px 1px  #ccc !important;margin-bottom:10px !important;margin-top:10px !important}</style>"+ post.getContent().replace("<img","<img onClick=\"showAndroidToast(this.src)\" ")+" <script type=\"text/javascript\"> function showAndroidToast(toast) {Android.showToast(toast);}</script>";
        web_view_post_activity_content.loadData(content_article, "text/html; charset=utf-8", "utf-8");
        web_view_post_activity_content.addJavascriptInterface(new WebAppInterface(this), "Android");
        text_view_post_activity_title.setText(post.getTitle());
        text_view_post_activity_name_user.setText(post.getUser());
        //text_view_item_post_comments.setText(format(guideList.get(position).getComments())+" Comments");
        //text_view_item_post_likes.setText(format(guideList.get(position).getLike())+" Likes");
        text_view_post_activity_time.setText(post.getCreated());
        text_view_post_activity_views.setText(format(post.getViews())+" Views");
        checkFavorite();
        if (post.getComment()){
            relative_layout_comment_section.setVisibility(View.VISIBLE);
        }else{
            relative_layout_comment_section.setVisibility(View.GONE);
        }

    }
    public class WebAppInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /** Show a toast from the web page */
        @JavascriptInterface
        public void showToast(String url) {
            Intent intent = new Intent(VideoActivity.this,FullScreenActivity.class);
            intent.putExtra("url",url);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);

        }
    }
    public void follow(){

        PrefManager prf= new PrefManager(VideoActivity.this.getApplicationContext());
        if (prf.getString("LOGGED").toString().equals("TRUE")) {
            button_follow_user_post_activity.setText(getResources().getString(R.string.loading));
            button_follow_user_post_activity.setEnabled(false);
            String follower = prf.getString("ID_USER");
            String key = prf.getString("TOKEN_USER");
            Retrofit retrofit = apiClient.getClient();
            apiRest service = retrofit.create(apiRest.class);
            Call<ApiResponse> call = service.follow(post.getUserid(), Integer.parseInt(follower), key);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals(200)){
                            button_follow_user_post_activity.setText("UnFollow");
                        }else if (response.body().getCode().equals(202)) {
                            button_follow_user_post_activity.setText("Follow");

                        }
                    }
                    button_follow_user_post_activity.setEnabled(true);

                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    button_follow_user_post_activity.setEnabled(true);
                }
            });
        }else{
            Intent intent = new Intent(VideoActivity.this,LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
    }
    private void getUser() {
        PrefManager prf= new PrefManager(VideoActivity.this.getApplicationContext());
        Integer follower= -1;
        if (prf.getString("LOGGED").toString().equals("TRUE")) {
            button_follow_user_post_activity.setEnabled(false);
            follower = Integer.parseInt(prf.getString("ID_USER"));
        }
        if (follower!= post.getUserid()){
            button_follow_user_post_activity.setVisibility(View.VISIBLE);
        }
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<ApiResponse> call = service.getUser(post.getUserid(),follower);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()){

                    for (int i=0;i<response.body().getValues().size();i++){
                        if (response.body().getValues().get(i).getName().equals("trusted")) {
                            if (response.body().getValues().get(i).getValue().equals("true"))
                                image_view_status_verified.setVisibility(View.VISIBLE);
                            else
                                image_view_status_verified.setVisibility(View.GONE);
                        }
                        if (response.body().getValues().get(i).getName().equals("follow")){
                            if (response.body().getValues().get(i).getValue().equals("true"))
                                button_follow_user_post_activity.setText("UnFollow");
                            else
                                button_follow_user_post_activity.setText("Follow");
                        }
                    }

                }else{


                }
                button_follow_user_post_activity.setEnabled(true);
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                button_follow_user_post_activity.setEnabled(true);
            }
        });
    }

    private void initInterstitial() {
        mInterstitialAdDownload = new InterstitialAd(getApplication());
        mInterstitialAdDownload.setAdUnitId(getString(R.string.ad_unit_id_interstitial));
        requestNewInterstitial();
    }
    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        mInterstitialAdDownload.loadAd(adRequest);
    }

    private final String TAG = VideoActivity.class.getSimpleName();
    private RelativeLayout nativeBannerAdContainer;
    private LinearLayout adView;
    private NativeBannerAd nativeBannerAd;

    public void initAds(){
        PrefManager prefManager= new PrefManager(VideoActivity.this.getApplicationContext());

        if (prefManager.getString("SUBSCRIBED").equals("TRUE"))
            return;
        if (!getResources().getString(R.string.FACEBOOK_ADS_ENABLED_BANNER).equals("true"))
            return;
        // Instantiate a NativeBannerAd object.
        // NOTE: the placement ID will eventually identify this as your App, you can ignore it for
        // now, while you are testing and replace it later when you have signed up.
        // While you are using this temporary code you will only get test ads and if you release
        // your code like this to the Google Play your users will not receive ads (you will get a no fill error).
        nativeBannerAd = new NativeBannerAd(VideoActivity.this, getResources().getString(R.string.FACEBOOK_ADS_NATIVE_BANNER_PLACEMENT_ID));
        nativeBannerAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                // Native ad finished downloading all assets
                Log.e(TAG, "Native ad finished downloading all assets.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Native ad failed to load
                Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Race condition, load() called again before last ad was displayed
                if (nativeBannerAd == null || nativeBannerAd != ad) {
                    return;
                }
                // Inflate Native Banner Ad into Container
                inflateAd(nativeBannerAd);
                Log.d(TAG, "Native ad is loaded and ready to be displayed!");
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
        // load the ad
        nativeBannerAd.loadAd();
    }
    private void inflateAd(NativeBannerAd nativeBannerAd) {
        // Unregister last ad
        nativeBannerAd.unregisterView();

        // Add the Ad view into the ad container.
        nativeBannerAdContainer = findViewById(R.id.native_banner_ad_container);
        LayoutInflater inflater = LayoutInflater.from(VideoActivity.this);
        // Inflate the Ad view.  The layout referenced is the one you created in the last step.
        adView = (LinearLayout) inflater.inflate(R.layout.native_banner_ad_layout, nativeBannerAdContainer, false);
        nativeBannerAdContainer.addView(adView);

        // Add the AdChoices icon (NativeBannerAdActivity.this, nativeBannerAd, true);
        RelativeLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdChoicesView adChoicesView = new AdChoicesView(VideoActivity.this.getApplicationContext(),nativeBannerAd,true);
        adChoicesContainer.addView(adChoicesView, 0);

        // Create native UI using the ad metadata.
        TextView nativeAdTitle = findViewById(R.id.native_ad_title);
        TextView nativeAdSocialContext =  adView.findViewById(R.id.native_ad_social_context);
        TextView sponsoredLabel =  adView.findViewById(R.id.native_ad_sponsored_label);
        AdIconView nativeAdIconView =  adView.findViewById(R.id.native_icon_view);
        Button nativeAdCallToAction =  adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdCallToAction.setText(nativeBannerAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(
                nativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdTitle.setText(nativeBannerAd.getAdvertiserName());
        nativeAdSocialContext.setText(nativeBannerAd.getAdSocialContext());
        sponsoredLabel.setText(nativeBannerAd.getSponsoredTranslation());

        // Register the Title and CTA button to listen for clicks.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);
        nativeBannerAd.registerViewForInteraction(adView, nativeAdIconView, clickableViews);
    }


    private void showAdsBanner() {
        PrefManager prefManager= new PrefManager(VideoActivity.this.getApplicationContext());

        if (prefManager.getString("SUBSCRIBED").equals("FALSE")) {
            final AdView mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder()
                    .build();

            // Start loading the ad in the background.
            mAdView.loadAd(adRequest);

            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    mAdView.setVisibility(View.VISIBLE);
                }
            });
        }

    }
    private void initAction() {
        this.image_view_post_guide_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VideoActivity.this, "image", Toast.LENGTH_SHORT).show();
            }
        });

        this.image_view_post_activity_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoActivity.this,EditVideoActivity.class);
                intent.putExtra("id", post.getId());
                intent.putExtra("title", post.getTitle());
                intent.putExtra("description", post.getContent());
                intent.putExtra("image", post.getThumbnail());
                intent.putExtra("video", post.getOriginal());
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                finish();
            }
        });
        this.relative_layout_dialog_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VideoActivity.this,UserActivity.class);
                intent.putExtra("id", post.getUserid());
                intent.putExtra("name", post.getUser());
                intent.putExtra("image", post.getUserimage());
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        this.button_follow_user_post_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                follow();
            }
        });
        this.edit_text_comment_add.addTextChangedListener(new CommentTextWatcher(this.edit_text_comment_add));

        this.image_button_comment_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComment();
            }
        });
        this.image_view_post_activity_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCommentBox();
            }
        });
        this.image_view_post_activity_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFavorite();
            }
        });
        this.image_view_post_activity_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scroll_view_post_activity.fullScroll(View.FOCUS_DOWN);
            }
        });
        this.image_view_post_activity_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check() && mInterstitialAdDownload.isLoaded()){
                    mInterstitialAdDownload.show();
                    mInterstitialAdDownload.setAdListener(new AdListener(){
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            requestNewInterstitial();
                            share();
                        }
                    });
                }else{
                    share();
                }
            }
        });
        this.rating_bar_post_main_post_activity.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser){
                    addRate(rating, post.getId());
                }

            }
        });
        this.image_view_comment_box_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCommentBox();
            }
        });
        this.exo_back_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
                finish();
            }
        });
    }

    private void initView() {
        this.scroll_view_post_activity=(ScrollView) findViewById(R.id.scroll_view_post_activity);
        this.image_view_post_activity_edit=(ImageView) findViewById(R.id.image_view_post_activity_edit);
        this.relative_layout_rating=(RelativeLayout) findViewById(R.id.relative_layout_rating);
        this.relative_layout_post_activity_comments=(RelativeLayout) findViewById(R.id.relative_layout_post_activity_comments);
        this.image_view_post_activity_share=(ImageView) findViewById(R.id.image_view_post_activity_share);
        this.image_view_post_activity_fav=(ImageView) findViewById(R.id.image_view_post_activity_fav);
        this.image_view_post_activity_like=(ImageView) findViewById(R.id.image_view_post_activity_like);
        this.image_view_status_verified=(ImageView) findViewById(R.id.image_view_status_verified);
        this.image_view_post_guide_activity=(ImageView) findViewById(R.id.image_view_post_post_activity);
        this.image_view_post_activity_comments=(ImageView) findViewById(R.id.image_view_post_activity_comments);
        this.text_view_post_activity_views=(TextView) findViewById(R.id.text_view_post_activity_views);
        this.text_view_post_activity_time=(TextView) findViewById(R.id.text_view_post_activity_time);
        this.text_view_post_activity_title=(TextView) findViewById(R.id.text_view_post_activity_title);
        this.text_view_post_activity_name_user=(TextView) findViewById(R.id.text_view_post_activity_name_user);
        this.circle_image_view_post_activity_user=(CircleImageView) findViewById(R.id.circle_image_view_post_activity_user);
        this.button_follow_user_post_activity=(Button) findViewById(R.id.button_follow_user_post_activity);
        this.web_view_post_activity_content=(WebView) findViewById(R.id.web_view_post_activity_content);


        this.rating_bar_post_main_post_activity=(AppCompatRatingBar) findViewById(R.id.rating_bar_post_main_post_activity);
        this.rating_bar_post_value_post_activity=(AppCompatRatingBar) findViewById(R.id.rating_bar_post_value_post_activity);
        this.rating_bar_post_1_post_activity=(RatingBar) findViewById(R.id.rating_bar_post_1_post_activity);
        this.rating_bar_post_2_post_activity=(RatingBar) findViewById(R.id.rating_bar_post_2_post_activity);
        this.rating_bar_post_3_post_activity=(RatingBar) findViewById(R.id.rating_bar_post_3_post_activity);
        this.rating_bar_post_4_post_activity=(RatingBar) findViewById(R.id.rating_bar_post_4_post_activity);
        this.rating_bar_post_5_post_activity=(RatingBar) findViewById(R.id.rating_bar_post_5_post_activity);

        this.text_view_rate_1_post_activity=(TextView) findViewById(R.id.text_view_rate_1_post_activity);
        this.text_view_rate_2_post_activity=(TextView) findViewById(R.id.text_view_rate_2_post_activity);
        this.text_view_rate_3_post_activity=(TextView) findViewById(R.id.text_view_rate_3_post_activity);
        this.text_view_rate_4_post_activity=(TextView) findViewById(R.id.text_view_rate_4_post_activity);
        this.text_view_rate_5_post_activity=(TextView) findViewById(R.id.text_view_rate_5_post_activity);
        this.text_view_rate_main_post_activity=(TextView) findViewById(R.id.text_view_rate_main_post_activity);
        this.progress_bar_rate_1_post_activity=(ProgressBar) findViewById(R.id.progress_bar_rate_1_post_activity);
        this.progress_bar_rate_2_post_activity=(ProgressBar) findViewById(R.id.progress_bar_rate_2_post_activity);
        this.progress_bar_rate_3_post_activity=(ProgressBar) findViewById(R.id.progress_bar_rate_3_post_activity);
        this.progress_bar_rate_4_post_activity=(ProgressBar) findViewById(R.id.progress_bar_rate_4_post_activity);
        this.progress_bar_rate_5_post_activity=(ProgressBar) findViewById(R.id.progress_bar_rate_5_post_activity);

        this.relative_layout_dialog_top=(RelativeLayout) findViewById(R.id.relative_layout_dialog_top);
        this.button_follow_user_post_activity=(Button) findViewById(R.id.button_follow_user_post_activity);

        this.relative_layout_comment_section=(RelativeLayout) findViewById(R.id.relative_layout_comment_section);
        this.edit_text_comment_add=(EditText) findViewById(R.id.edit_text_comment_add);
        this.progress_bar_comment_add=(ProgressBar) findViewById(R.id.progress_bar_comment_add);
        this.progress_bar_comment_list=(ProgressBar) findViewById(R.id.progress_bar_comment_list);
        this.image_button_comment_add=(ImageView) findViewById(R.id.image_button_comment_add);
        this.image_button_comment_add.setEnabled(false);
        this.imageView_empty_comment=(ImageView) findViewById(R.id.imageView_empty_comment);
        this.image_view_comment_box_close=(ImageView) findViewById(R.id.image_view_comment_box_close);
        this.text_view_comment_box_count=(TextView) findViewById(R.id.text_view_comment_box_count);
        this.recycle_view_comment=(RecyclerView) findViewById(R.id.recycle_view_comment);

        this.commentAdapter = new CommentAdapter(commentList, VideoActivity.this.getApplication());
        this.recycle_view_comment.setHasFixedSize(true);
        this.recycle_view_comment.setAdapter(commentAdapter);
        this.linearLayoutManagerCOmment= new LinearLayoutManager(getApplicationContext());
        this.recycle_view_comment.setLayoutManager(linearLayoutManagerCOmment);



        web_view_post_activity_content.setWebChromeClient(new WebChromeClient());
        web_view_post_activity_content.setWebViewClient(new WebViewClient());
        web_view_post_activity_content.getSettings().setJavaScriptEnabled(true);
        web_view_post_activity_content.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null && (url.startsWith("http://")  || url.startsWith("https://"))) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                    return true;
                } else {
                    return false;
                }
            }
        });
        initCommentView();

        ivHideControllerButton = (ImageView) findViewById(R.id.exo_controller);
        exo_replay = (ImageView) findViewById(R.id.exo_replay);
        linear_layout_exo_replay = (LinearLayout) findViewById(R.id.linear_layout_exo_replay);
        shouldAutoPlay = true;
        bandwidthMeter = new DefaultBandwidthMeter();
        mediaDataSourceFactory = new DefaultDataSourceFactory(VideoActivity.this.getApplicationContext(), Util.getUserAgent(VideoActivity.this.getApplicationContext(), "mediaPlayerSample"), (TransferListener<? super DataSource>) bandwidthMeter);
        window = new Timeline.Window();
        ivHideControllerButton = (ImageView) findViewById(R.id.exo_controller);
        simple_arc_loader_exo = (SimpleArcLoader) findViewById(R.id.simple_arc_loader_exo);
        exo_pause = (ImageView) findViewById(R.id.exo_pause);
        exo_play = (ImageView) findViewById(R.id.exo_play);
        exo_back_image_view = (ImageView) findViewById(R.id.exo_back_image_view);

    }
    private void checkFavorite() {
        List<Post> favorites_list =Hawk.get("favorite");
        Boolean exist = false;
        if (favorites_list == null) {
            favorites_list = new ArrayList<>();
        }

        for (int i = 0; i < favorites_list.size(); i++) {
            if (favorites_list.get(i).getId().equals(post.getId())) {
                exist = true;
            }
        }
        if (exist){
            image_view_post_activity_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black));
        }else{
            image_view_post_activity_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border));
        }
    }
    private void addFavorite() {


        List<Post> favorites_list =Hawk.get("favorite");
        Boolean exist = false;
        if (favorites_list == null) {
            favorites_list = new ArrayList<>();
        }
        int fav_position = -1;
        for (int i = 0; i < favorites_list.size(); i++) {
            if (favorites_list.get(i).getId().equals(post.getId())) {
                exist = true;
                fav_position = i;
            }
        }
        if (exist == false) {
            favorites_list.add(post);
            Hawk.put("favorite",favorites_list);
            image_view_post_activity_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black));

        }else{
            favorites_list.remove(fav_position);
            Hawk.put("favorite",favorites_list);
            image_view_post_activity_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border));
        }

    }
    public void initCommentView(){
        Animation c= AnimationUtils.loadAnimation(VideoActivity.this.getApplicationContext(),
                R.anim.slide_down);
        c.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                relative_layout_post_activity_comments.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        relative_layout_post_activity_comments.startAnimation(c);
    }
    public void getRating(Integer id) {
        PrefManager prf = new PrefManager(getApplicationContext());
        String user_id = "0";
        if (prf.getString("LOGGED").toString().equals("TRUE")) {
            user_id=prf.getString("ID_USER").toString();
        }
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<ApiResponse> call = service.getRate(user_id,id);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode() == 200) {
                        rating_bar_post_main_post_activity.setRating(Integer.parseInt(response.body().getMessage()));
                    } else if (response.body().getCode() == 202){
                        rating_bar_post_main_post_activity.setRating(0);
                    }else{
                        rating_bar_post_main_post_activity.setRating(0);
                    }
                    if(response.body().getCode() != 500){
                        Integer rate_1=0;
                        Integer rate_2=0;
                        Integer rate_3=0;
                        Integer rate_4=0;
                        Integer rate_5=0;
                        float rate=0;
                        for (int i=0;i<response.body().getValues().size();i++){

                            if (response.body().getValues().get(i).getName().equals("1")){
                                rate_1=Integer.parseInt(response.body().getValues().get(i).getValue());
                            }
                            if (response.body().getValues().get(i).getName().equals("2")){
                                rate_2=Integer.parseInt(response.body().getValues().get(i).getValue());
                            }
                            if (response.body().getValues().get(i).getName().equals("3")){
                                rate_3=Integer.parseInt(response.body().getValues().get(i).getValue());
                            }
                            if (response.body().getValues().get(i).getName().equals("4")){
                                rate_4=Integer.parseInt(response.body().getValues().get(i).getValue());
                            }
                            if (response.body().getValues().get(i).getName().equals("5")){
                                rate_5=Integer.parseInt(response.body().getValues().get(i).getValue());
                            }
                            if (response.body().getValues().get(i).getName().equals("rate")){
                                rate=Float.parseFloat(response.body().getValues().get(i).getValue());
                            }
                        }
                        rating_bar_post_value_post_activity.setRating(rate);
                        String formattedString=rate + "";


                        text_view_rate_main_post_activity.setText(formattedString);
                        text_view_rate_1_post_activity.setText(rate_1+"");
                        text_view_rate_2_post_activity.setText(rate_2+"");
                        text_view_rate_3_post_activity.setText(rate_3+"");
                        text_view_rate_4_post_activity.setText(rate_4+"");
                        text_view_rate_5_post_activity.setText(rate_5+"");
                        Integer total= rate_1 + rate_2 + rate_3 + rate_4 + rate_5;
                        if(total==0) {
                            total = 1;
                        }
                        progress_bar_rate_1_post_activity.setProgress((int) ((rate_1*100) / total)  );
                        progress_bar_rate_2_post_activity.setProgress((int) ((rate_2*100) / total)  );
                        progress_bar_rate_3_post_activity.setProgress((int) ((rate_3*100) / total)  );
                        progress_bar_rate_4_post_activity.setProgress((int) ((rate_4*100) / total)  );
                        progress_bar_rate_5_post_activity.setProgress((int) ((rate_5*100) / total)  );
                    }
                } else {

                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {


            }
        });

    }
    public void addRate(final float value, final Integer id) {
        PrefManager prf = new PrefManager(getApplicationContext());
        if (prf.getString("LOGGED").toString().equals("TRUE")) {
            Retrofit retrofit = apiClient.getClient();
            apiRest service = retrofit.create(apiRest.class);
            Call<ApiResponse> call = service.addRate(prf.getString("ID_USER").toString(),id, value);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                    if (response.isSuccessful()) {
                        if (response.body().getCode() == 200) {
                            Toasty.success(VideoActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }else{
                            Toasty.success(VideoActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        getRating(id);
                    } else {

                    }

                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {


                }
            });
        } else {
            Intent intent = new Intent(VideoActivity.this, LoginActivity.class);
            startActivity(intent);
        }

    }

    public void showCommentBox(){
        getComments();
        if (relative_layout_post_activity_comments.getVisibility() == View.VISIBLE)
        {
            Animation c= AnimationUtils.loadAnimation(VideoActivity.this.getApplicationContext(),
                    R.anim.slide_down);
            c.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    relative_layout_post_activity_comments.setVisibility(View.GONE);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            relative_layout_post_activity_comments.startAnimation(c);


        }else{
            Animation c= AnimationUtils.loadAnimation(VideoActivity.this.getApplicationContext(),
                    R.anim.slide_up);
            c.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    relative_layout_post_activity_comments.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            relative_layout_post_activity_comments.startAnimation(c);
        }

    }
    public void addComment(){


        PrefManager prf= new PrefManager(VideoActivity.this.getApplicationContext());
        if (prf.getString("LOGGED").toString().equals("TRUE")){

            byte[] data = new byte[0];
            String comment_final ="";
            try {
                data = edit_text_comment_add.getText().toString().getBytes("UTF-8");
                comment_final = Base64.encodeToString(data, Base64.DEFAULT);

            } catch (UnsupportedEncodingException e) {
                comment_final = edit_text_comment_add.getText().toString();
                e.printStackTrace();
            }
            progress_bar_comment_add.setVisibility(View.VISIBLE);
            image_button_comment_add.setVisibility(View.GONE);
            Retrofit retrofit = apiClient.getClient();
            apiRest service = retrofit.create(apiRest.class);
            Call<ApiResponse> call = service.addCommentImage(prf.getString("ID_USER").toString(), post.getId(),comment_final);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()){
                        if (response.body().getCode()==200){
                            post.setComments(post.getComments()+1);
                            text_view_comment_box_count.setText( post.getComments()+" "+VideoActivity.this.getResources().getString(R.string.comments));
                            recycle_view_comment.setVisibility(View.VISIBLE);
                            imageView_empty_comment.setVisibility(View.GONE);
                            Toasty.success(VideoActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            edit_text_comment_add.setText("");
                            String id="";
                            String content="";
                            String user="";
                            String image="";
                            String trusted="false";

                            for (int i=0;i<response.body().getValues().size();i++){
                                if (response.body().getValues().get(i).getName().equals("id")){
                                    id=response.body().getValues().get(i).getValue();
                                }
                                if (response.body().getValues().get(i).getName().equals("content")){
                                    content=response.body().getValues().get(i).getValue();
                                }
                                if (response.body().getValues().get(i).getName().equals("user")){
                                    user=response.body().getValues().get(i).getValue();
                                }
                                if (response.body().getValues().get(i).getName().equals("trusted")){
                                    trusted=response.body().getValues().get(i).getValue();
                                }
                                if (response.body().getValues().get(i).getName().equals("image")){
                                    image=response.body().getValues().get(i).getValue();
                                }
                            }
                            Comment comment= new Comment();
                            comment.setId(Integer.parseInt(id));
                            comment.setUser(user);
                            comment.setContent(content);
                            comment.setImage(image);
                            comment.setEnabled(true);
                            comment.setTrusted(trusted);
                            comment.setCreated(getResources().getString(R.string.now_time));
                            commentList.add(comment);
                            commentAdapter.notifyDataSetChanged();

                        }else{
                            Toasty.error(VideoActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    recycle_view_comment.scrollToPosition(recycle_view_comment.getAdapter().getItemCount()-1);
                    recycle_view_comment.scrollToPosition(recycle_view_comment.getAdapter().getItemCount()-1);
                    commentAdapter.notifyDataSetChanged();
                    progress_bar_comment_add.setVisibility(View.GONE);
                    image_button_comment_add.setVisibility(View.VISIBLE);
                }
                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    progress_bar_comment_add.setVisibility(View.VISIBLE);
                    image_button_comment_add.setVisibility(View.GONE);
                }
            });
        }else{
            Intent intent = new Intent(VideoActivity.this,LoginActivity.class);
            startActivity(intent);
        }

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
    private class CommentTextWatcher implements TextWatcher {
        private View view;
        private CommentTextWatcher(View view) {
            this.view = view;
        }
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edit_text_comment_add:
                    ValidateComment();
                    break;
            }
        }
    }

    private boolean ValidateComment() {
        String email = edit_text_comment_add.getText().toString().trim();
        if (email.isEmpty()) {
            image_button_comment_add.setEnabled(false);
            return false;
        }else{
            image_button_comment_add.setEnabled(true);
        }
        return true;
    }
    public void getComments(){
        progress_bar_comment_list.setVisibility(View.VISIBLE);
        recycle_view_comment.setVisibility(View.GONE);
        imageView_empty_comment.setVisibility(View.GONE);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Comment>> call = service.getComments(post.getId());
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if(response.isSuccessful()) {
                    commentList.clear();
                    post.setComments(response.body().size());
                    text_view_comment_box_count.setText(post.getComments()+" "+VideoActivity.this.getResources().getString(R.string.comments));
                    if (response.body().size() != 0) {
                        for (int i = 0; i < response.body().size(); i++) {
                            commentList.add(response.body().get(i));
                        }
                        commentAdapter.notifyDataSetChanged();

                        progress_bar_comment_list.setVisibility(View.GONE);
                        recycle_view_comment.setVisibility(View.VISIBLE);
                        imageView_empty_comment.setVisibility(View.GONE);


                    } else {
                        progress_bar_comment_list.setVisibility(View.GONE);
                        recycle_view_comment.setVisibility(View.GONE);
                        imageView_empty_comment.setVisibility(View.VISIBLE);

                    }
                }else{

                }
                recycle_view_comment.setNestedScrollingEnabled(false);

            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {


            }
        });
    }
    public void addShare(){
        final PrefManager prefManager = new PrefManager(this);
        Integer id_user = 0;
        String key_user= "";
        if (prefManager.getString("LOGGED").toString().equals("TRUE")) {
            id_user=  Integer.parseInt(prefManager.getString("ID_USER"));
            key_user=  prefManager.getString("TOKEN_USER");
        }
        // if (!prefManager.getString(post.getId()+"_share").equals("true")) {
        prefManager.setString(post.getId()+"_share", "true");
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<Integer> call = service.addShare(post.getId(),id_user,key_user);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, retrofit2.Response<Integer> response) {

            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
        //  }
    }
    public void addView(){
        final PrefManager prefManager = new PrefManager(this);
        Integer id_user = 0;
        String key_user= "";
        if (prefManager.getString("LOGGED").toString().equals("TRUE")) {
            id_user=  Integer.parseInt(prefManager.getString("ID_USER"));
            key_user=  prefManager.getString("TOKEN_USER");
        }
        if (!prefManager.getString(post.getId()+"_view").equals("true")) {
            prefManager.setString(post.getId()+"_view", "true");
            Retrofit retrofit = apiClient.getClient();
            apiRest service = retrofit.create(apiRest.class);
            Call<Integer> call = service.addView(post.getId(),id_user,key_user);
            call.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, retrofit2.Response<Integer> response) {
                    if (response.isSuccessful()){
                        text_view_post_activity_views.setText(response.body().toString()+" Views");
                    }
                }
                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                }
            });
        }
    }
    public void share(){
        String shareBody = post.getTitle()+"\n\n"+getResources().getString(R.string.see_post_from)+"\n"+ Global.API_URL.replace("api","share")+ post.getId()+".html";
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT,  getString(R.string.app_name));
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.app_name)));
        addShare();
    }
    public boolean check(){
        PrefManager prf = new PrefManager(VideoActivity.this.getApplicationContext());
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

                if (seconds > Integer.parseInt(getResources().getString(R.string.AD_MOB_TIME))) {
                    prf.setString("LAST_DATE_ADS", strDate);
                    return  true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return  false;
    }
}
