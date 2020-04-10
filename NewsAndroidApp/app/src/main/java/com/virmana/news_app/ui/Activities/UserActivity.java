package com.virmana.news_app.ui.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.transition.Slide;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.virmana.news_app.R;
import com.virmana.news_app.Adapters.UserAdapter;
import com.virmana.news_app.api.apiClient;
import com.virmana.news_app.api.apiRest;
import com.virmana.news_app.model.ApiResponse;
import com.virmana.news_app.model.Category;
import com.virmana.news_app.model.User;
import com.virmana.news_app.Provider.PrefManager;
import com.virmana.news_app.ui.fragement.UserFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserActivity extends AppCompatActivity {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private String query = "e";

    private Toolbar toolbar;
    private int id;
    private String name;
    private String image;
    private String facebook;
    private String email;
    private String instagram;
    private String twitter;

    private CircleImageView image_view_profile_user_activity;
    private TextView text_view_profile_user_activity;
    private TextView text_view_followers_count_user_activity;
    private TextView text_view_wallpaper_count_activity_user;
    private TextView text_view_following_count_activity_user;
    private SwipeRefreshLayout swipe_refreshl_user_activity;
    private LinearLayout linear_layout_page_error;
    private ImageView image_view_empty;
    private RecyclerView recycle_view_user_activity;
    private RelativeLayout relative_layout_load_more;
    private Button button_follow_user_activity;
    private List<Slide> slideList=new ArrayList<>();
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    private Integer page = 0;
    private Boolean loaded=false;
    private GridLayoutManager gridLayoutManager;
    private LinearLayout linear_layout_followers;
    private LinearLayout linear_layout_following;

    private AlertDialog.Builder builderFollowing;
    private List<User> followings=new ArrayList<>();

    private AlertDialog.Builder builderFollowers;
    private List<User> followers=new ArrayList<>();
    private ProgressDialog loading_progress;
    private Button button_try_again;
    private ViewPager viewPager;
    private CoordinatorLayout coordinatorLayout;
    private ImageView image_view_activity_user_email;
    private ImageView image_view_activity_user_instagram;
    private ImageView image_view_activity_user_twitter;
    private ImageView image_view_activity_user_facebook;
    private Button button_edit_user_activity;
    private TextView text_view_status_count_activity_user;
    private ImageView image_view_status_verified;
    private String from = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras() ;


        PrefManager prf= new PrefManager(getApplicationContext());

        this.id =  bundle.getInt("id");
        this.name =  bundle.getString("name");
        this.image =  bundle.getString("image");
        this.from =  bundle.getString("from");
        initView();
        getSection();
        initAction();
        initUser();
    }
    private void initUser() {
        text_view_profile_user_activity.setText(name);
        if (!image.isEmpty()){
            Picasso.with(getApplicationContext()).load(image).error(R.drawable.logo_w).placeholder(R.drawable.profile).into(this.image_view_profile_user_activity);
        }else{
            Picasso.with(getApplicationContext()).load(R.drawable.logo_w).error(R.drawable.logo_w).placeholder(R.drawable.profile).into(this.image_view_profile_user_activity);
        }
        PrefManager prf= new PrefManager(getApplicationContext());
        if (prf.getString("LOGGED").toString().equals("TRUE")) {
            Integer me = Integer.parseInt(prf.getString("ID_USER"));
            if (id==me){
                button_follow_user_activity.setVisibility(View.GONE);
                button_edit_user_activity.setVisibility(View.VISIBLE);
            }else{
                button_follow_user_activity.setVisibility(View.VISIBLE);
                button_edit_user_activity.setVisibility(View.GONE);
            }
        }

        getUser();
    }
    public void getSection(){
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        PrefManager prefManager= new PrefManager(getApplicationContext());

        String language=prefManager.getString("LANGUAGE_DEFAULT");
        Call<List<Category>> call = service.allCategoriesByLanguage(language);        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
            }
            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
            }
        });
    }
    private void initView() {
        setContentView(R.layout.activity_user);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.image_view_status_verified=(ImageView) findViewById(R.id.image_view_status_verified);

        this.coordinatorLayout =  (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        this.button_try_again=(Button) findViewById(R.id.button_try_again);
        this.image_view_profile_user_activity=(CircleImageView) findViewById(R.id.image_view_profile_user_activity);
        this.text_view_profile_user_activity=(TextView) findViewById(R.id.text_view_profile_user_activity);
        this.text_view_followers_count_user_activity =(TextView) findViewById(R.id.text_view_followers_count_user_activity);
        this.text_view_following_count_activity_user=(TextView) findViewById(R.id.text_view_following_count_activity_user);
        this.linear_layout_page_error=(LinearLayout) findViewById(R.id.linear_layout_page_error);
        this.relative_layout_load_more=(RelativeLayout) findViewById(R.id.relative_layout_load_more);
        this.button_follow_user_activity=(Button) findViewById(R.id.button_follow_user_activity);
        this.linear_layout_followers=(LinearLayout) findViewById(R.id.linear_layout_followers);
        this.linear_layout_following=(LinearLayout) findViewById(R.id.linear_layout_following);
        this.image_view_activity_user_email=(ImageView) findViewById(R.id.image_view_activity_user_email);
        this.image_view_activity_user_instagram=(ImageView) findViewById(R.id.image_view_activity_user_instagram);
        this.image_view_activity_user_twitter=(ImageView) findViewById(R.id.image_view_activity_user_twitter);
        this.image_view_activity_user_facebook=(ImageView) findViewById(R.id.image_view_activity_user_facebook);
        this.button_edit_user_activity=(Button) findViewById(R.id.button_edit_user_activity);
        this.text_view_status_count_activity_user=(TextView) findViewById(R.id.text_view_status_count_activity_user);
        viewPager = (ViewPager) findViewById(R.id.main_view_pager);



        setupViewPager(viewPager);


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());


        Bundle bundle_video = new Bundle();
        bundle_video.putInt("user", id);
        bundle_video.putString("type", "video");
        UserFragment video = new UserFragment();
        video.setArguments(bundle_video);




        adapter.addFragment(video,"Videos");

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

            return mFragmentList.get(position);

        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    public void loadFollowings(){
        loading_progress= ProgressDialog.show(this, null,getResources().getString(R.string.operation_progress), true);

        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<User>> call = service.getFollowing(id);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()){
                    final Dialog dialog = new Dialog(UserActivity.this,
                            R.style.Theme_Dialog);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(R.layout.user_row);
                    Window window = dialog.getWindow();
                    WindowManager.LayoutParams wlp = window.getAttributes();
                    getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                    wlp.gravity = Gravity.BOTTOM;
                    wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                    window.setAttributes(wlp);
                    RelativeLayout relativeLayout_close_rate_gialog=(RelativeLayout) dialog.findViewById(R.id.relativeLayout_close_rate_gialog);
                    ListView listView= (ListView) dialog.findViewById(R.id.user_rows);
                    TextView text_view_title_type= (TextView) dialog.findViewById(R.id.text_view_title_type);
                    text_view_title_type.setText("Followings list");
                    listView.setAdapter(new UserAdapter(response.body(),UserActivity.this));
                    ImageView image_view_close= (ImageView) dialog.findViewById(R.id.image_view_close);
                    image_view_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    relativeLayout_close_rate_gialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.setOnKeyListener(new Dialog.OnKeyListener() {

                        @Override
                        public boolean onKey(DialogInterface arg0, int keyCode,
                                             KeyEvent event) {
                            // TODO Auto-generated method stub
                            if (keyCode == KeyEvent.KEYCODE_BACK) {

                                dialog.dismiss();
                            }
                            return true;
                        }
                    });
                    dialog.show();
                }
                loading_progress.dismiss();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                loading_progress.dismiss();
            }
        });

    }

    private void initAction() {
        this.image_view_activity_user_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",email, null));
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });
        this.image_view_activity_user_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(facebook));
                startActivity(i);
            }
        });
        this.image_view_activity_user_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(instagram));
                startActivity(i);
            }
        });
        this.image_view_activity_user_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(twitter));
                startActivity(i);
            }
        });
        this.linear_layout_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                builderFollowing = new AlertDialog.Builder(UserActivity.this);
                builderFollowing.setIcon(R.drawable.ic_follow);
                builderFollowing.setTitle("Following");

                View view = (View)  getLayoutInflater().inflate(R.layout.user_row,null);

                ListView listView= (ListView) view.findViewById(R.id.user_rows);
                listView.setAdapter(new UserAdapter(followings,UserActivity.this));
                builderFollowing.setView(view);
                builderFollowing.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderFollowing.show();
            }
        });
        this.linear_layout_followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFollowers();

            }
        });
        this.linear_layout_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFollowings();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        this.button_follow_user_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                follow();
            }
        });
        this.button_edit_user_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this,EditActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("name",name);
                intent.putExtra("image",image);
                startActivity(intent);
            }
        });
    }
    public void loadFollowers(){
        loading_progress= ProgressDialog.show(this, null,getResources().getString(R.string.operation_progress), true);

        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<User>> call = service.getFollowers(id);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()){
                    for (int i = 0; i < followers.size(); i++) {
                        followers.add(response.body().get(i));
                    }

                    final Dialog dialog = new Dialog(UserActivity.this,
                            R.style.Theme_Dialog);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(R.layout.user_row);
                    Window window = dialog.getWindow();
                    WindowManager.LayoutParams wlp = window.getAttributes();
                    getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                    wlp.gravity = Gravity.BOTTOM;
                    wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                    window.setAttributes(wlp);
                    RelativeLayout relativeLayout_close_rate_gialog=(RelativeLayout) dialog.findViewById(R.id.relativeLayout_close_rate_gialog);
                    ListView listView= (ListView) dialog.findViewById(R.id.user_rows);
                    TextView text_view_title_type= (TextView) dialog.findViewById(R.id.text_view_title_type);
                    text_view_title_type.setText("Followers list");
                    listView.setAdapter(new UserAdapter(response.body(),UserActivity.this));
                    ImageView image_view_close= (ImageView) dialog.findViewById(R.id.image_view_close);
                    image_view_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    relativeLayout_close_rate_gialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.setOnKeyListener(new Dialog.OnKeyListener() {

                        @Override
                        public boolean onKey(DialogInterface arg0, int keyCode,
                                             KeyEvent event) {
                            // TODO Auto-generated method stub
                            if (keyCode == KeyEvent.KEYCODE_BACK) {

                                dialog.dismiss();
                            }
                            return true;
                        }
                    });
                    dialog.show();
                }
                loading_progress.dismiss();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                loading_progress.dismiss();
            }
        });

    }
    public void follow(){

        PrefManager prf= new PrefManager(getApplicationContext());
        if (prf.getString("LOGGED").toString().equals("TRUE")) {
            button_follow_user_activity.setText(getResources().getString(R.string.loading));
            button_follow_user_activity.setEnabled(false);
            String follower = prf.getString("ID_USER");
            String key = prf.getString("TOKEN_USER");
            Retrofit retrofit = apiClient.getClient();
            apiRest service = retrofit.create(apiRest.class);
            Call<ApiResponse> call = service.follow(id, Integer.parseInt(follower), key);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals(200)){
                            button_follow_user_activity.setText("UnFollow");
                            text_view_followers_count_user_activity.setText((Integer.parseInt(text_view_followers_count_user_activity.getText().toString())+1)+"");
                        }else if (response.body().getCode().equals(202)) {
                            button_follow_user_activity.setText("Follow");
                            text_view_followers_count_user_activity.setText((Integer.parseInt(text_view_followers_count_user_activity.getText().toString())-1)+"");

                        }
                    }
                    button_follow_user_activity.setEnabled(true);

                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    button_follow_user_activity.setEnabled(true);
                }
            });
        }else{
            Intent intent = new Intent(UserActivity.this,LoginActivity.class);
            startActivity(intent);
        }
    }
    @Override
    public void onBackPressed(){
        if (from!=null){
            Intent intent = new Intent(UserActivity.this,MainActivity.class);
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
                    Intent intent = new Intent(UserActivity.this,MainActivity.class);
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
    private void getUser() {
        PrefManager prf= new PrefManager(getApplicationContext());
        Integer follower= -1;
        if (prf.getString("LOGGED").toString().equals("TRUE")) {
            button_follow_user_activity.setEnabled(false);
            follower = Integer.parseInt(prf.getString("ID_USER"));
        }
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<ApiResponse> call = service.getUser(id,follower);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                apiClient.FormatData(UserActivity.this,response);
                if (response.isSuccessful()){

                    for (int i=0;i<response.body().getValues().size();i++){
                        if (response.body().getValues().get(i).getName().equals("trusted")) {
                            if (response.body().getValues().get(i).getValue().equals("true"))
                                image_view_status_verified.setVisibility(View.VISIBLE);
                            else
                                image_view_status_verified.setVisibility(View.GONE);
                        }
                        if (response.body().getValues().get(i).getName().equals("followers")){
                            text_view_followers_count_user_activity.setText(format(Integer.parseInt(response.body().getValues().get(i).getValue())));
                        }
                        if (response.body().getValues().get(i).getName().equals("followings")){
                            text_view_following_count_activity_user.setText(format(Integer.parseInt(response.body().getValues().get(i).getValue())));

                        }
                        if (response.body().getValues().get(i).getName().equals("posts")){
                            text_view_status_count_activity_user.setText(format(Integer.parseInt(response.body().getValues().get(i).getValue())));
                        }
                        if (response.body().getValues().get(i).getName().equals("facebook")){
                           facebook = response.body().getValues().get(i).getValue();
                            if (facebook!=null){
                                if (!facebook.isEmpty()){
                                    if (facebook.startsWith("http://") || facebook.startsWith("https://")) {
                                        image_view_activity_user_facebook.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        }
                        if (response.body().getValues().get(i).getName().equals("twitter")){
                            twitter = response.body().getValues().get(i).getValue();
                            if (twitter!=null) {

                                if (!twitter.isEmpty()) {
                                    if (twitter.startsWith("http://") || twitter.startsWith("https://")) {
                                        image_view_activity_user_twitter.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        }
                        if (response.body().getValues().get(i).getName().equals("instagram")){

                            instagram = response.body().getValues().get(i).getValue();
                            if (instagram!=null) {

                                if (!instagram.isEmpty()){
                                    if (instagram.startsWith("http://") || instagram.startsWith("https://")) {

                                        image_view_activity_user_instagram.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        }
                        if (response.body().getValues().get(i).getName().equals("email")){
                            email = response.body().getValues().get(i).getValue();
                            if (email!=null) {
                                if (!email.isEmpty()){
                                    image_view_activity_user_email.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        if (response.body().getValues().get(i).getName().equals("follow")){
                            if (response.body().getValues().get(i).getValue().equals("true"))
                                button_follow_user_activity.setText("UnFollow");
                            else
                                button_follow_user_activity.setText("Follow");
                        }
                    }

                }else{
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, getResources().getString(R.string.no_connexion), Snackbar.LENGTH_INDEFINITE)
                            .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    getUser();
                                }
                            });
                    snackbar.setActionTextColor(Color.RED);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();

                }
                button_follow_user_activity.setEnabled(true);
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                button_follow_user_activity.setEnabled(true);

                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, getResources().getString(R.string.no_connexion), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getUser();
                            }
                        });
                snackbar.setActionTextColor(Color.RED);
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                snackbar.show();

            }
        });
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
}
