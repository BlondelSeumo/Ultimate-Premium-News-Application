package com.virmana.news_app.ui.Activities;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.virmana.news_app.R;
import com.virmana.news_app.Adapters.AllCategoryVideoAdapter;
import com.virmana.news_app.api.apiClient;
import com.virmana.news_app.api.apiRest;
import com.virmana.news_app.model.Category;
import com.virmana.news_app.Provider.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AllCategoryActivity extends AppCompatActivity {

    private int id;
    private String title;
    private String image;

    private Integer page = 0;
    private String language = "0";
    private Boolean loaded=false;

    private View view;
    private SwipeRefreshLayout swipe_refreshl_all_category;
    private LinearLayout linear_layout_load_all_category;
    private RecyclerView recycler_view_all_category;
    private List<Category> categoryList =new ArrayList<>();
    private GridLayoutManager gridLayoutManager;
    private PrefManager prefManager;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private RelativeLayout relative_layout_load_more;
    private LinearLayout linear_layout_page_error;
    private Button button_try_again;
    private AllCategoryVideoAdapter allCategoryVideoAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.prefManager= new PrefManager(getApplicationContext());
        this.language=prefManager.getString("LANGUAGE_DEFAULT");

        setContentView(R.layout.activity_all_category);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.all_categories));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showAdsBanner();
        initView();
        loadStatusCategory();
        initAction();

    }
    private void showAdsBanner() {
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
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
        return;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initView(){
        this.relative_layout_load_more=(RelativeLayout) findViewById(R.id.relative_layout_load_more);
        this.button_try_again =(Button) findViewById(R.id.button_try_again);
        this.swipe_refreshl_all_category=(SwipeRefreshLayout) findViewById(R.id.swipe_refreshl_all_category);
        this.linear_layout_page_error=(LinearLayout) findViewById(R.id.linear_layout_page_error);
        this.linear_layout_load_all_category=(LinearLayout) findViewById(R.id.linear_layout_load_all_category);
        this.recycler_view_all_category=(RecyclerView) findViewById(R.id.recycler_view_all_category);
        this.gridLayoutManager=  new GridLayoutManager(this,1);
        allCategoryVideoAdapter =  new AllCategoryVideoAdapter(categoryList,this);
        recycler_view_all_category.setHasFixedSize(true);
        recycler_view_all_category.setAdapter(allCategoryVideoAdapter);
        recycler_view_all_category.setLayoutManager(gridLayoutManager);

    }
    public void initAction(){
        swipe_refreshl_all_category.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                categoryList.clear();
                page = 0;
                loading=true;
                loadStatusCategory();
            }
        });
    }
    public void loadStatusCategory(){
        swipe_refreshl_all_category.setRefreshing(true);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        PrefManager prefManager= new PrefManager(getApplicationContext());

        String language=prefManager.getString("LANGUAGE_DEFAULT");
        Call<List<Category>> call = service.allCategoriesByLanguage(language);
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if(response.isSuccessful()){
                    if (response.body().size()!=0){
                        categoryList.clear();
                        for (int i=0;i<response.body().size();i++){
                            categoryList.add(response.body().get(i));
                        }
                        allCategoryVideoAdapter.notifyDataSetChanged();
                    }
                    recycler_view_all_category.setVisibility(View.VISIBLE);
                    linear_layout_load_all_category.setVisibility(View.GONE);
                    linear_layout_page_error.setVisibility(View.GONE);
                    swipe_refreshl_all_category.setRefreshing(false);
                }else{
                    recycler_view_all_category.setVisibility(View.GONE);
                    linear_layout_load_all_category.setVisibility(View.GONE);
                    linear_layout_page_error.setVisibility(View.VISIBLE);
                    swipe_refreshl_all_category.setRefreshing(false);
                }
            }
            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                recycler_view_all_category.setVisibility(View.GONE);
                linear_layout_load_all_category.setVisibility(View.GONE);
                linear_layout_page_error.setVisibility(View.VISIBLE);
                swipe_refreshl_all_category.setRefreshing(false);

            }
        });
    }
}
