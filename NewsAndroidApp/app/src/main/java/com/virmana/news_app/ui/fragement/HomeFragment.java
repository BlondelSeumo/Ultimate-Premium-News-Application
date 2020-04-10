package com.virmana.news_app.ui.fragement;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.virmana.news_app.R;
import com.virmana.news_app.Adapters.PostAdapter;
import com.virmana.news_app.api.apiClient;
import com.virmana.news_app.api.apiRest;
import com.virmana.news_app.model.Category;
import com.virmana.news_app.model.HomeResponse;
import com.virmana.news_app.model.Post;
import com.virmana.news_app.model.Question;
import com.virmana.news_app.model.Slide;
import com.virmana.news_app.Provider.PrefManager;
import com.virmana.news_app.ui.Activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    private Integer type_ads = 0;
    private Integer page = 0;

    private View view;
    private PrefManager prefManager;
    private String language = "0";
    private boolean loaded = false;
    private RelativeLayout relative_layout_load_more;
    private Button button_try_again;
    private SwipeRefreshLayout swipe_refreshl_status_fragment;
    private LinearLayout linear_layout_page_error;
    private ImageView imageView_empty_favorite;
    private RecyclerView recycler_view_status_fragment;
    private LinearLayoutManager linearLayoutManager;

    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;

    private List<Question> questionList =new ArrayList<>();
    private List<Post> postList =new ArrayList<>();
    private List<Category> categoryList =new ArrayList<>();
    private PostAdapter postAdapter;
    private Integer item = 0 ;
    private Integer lines_beetween_ads = 8 ;
    private Boolean native_ads_enabled = false ;
    private List<Slide> slideList=new ArrayList<>();
    private boolean widget_enabled = false;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        this.view =  inflater.inflate(R.layout.fragment_home, container, false);
        this.prefManager= new PrefManager(getActivity().getApplicationContext());

        this.language=prefManager.getString("LANGUAGE_DEFAULT");

        initView();
        initAction();

        if (prefManager.getString("widget_enabled").equals("true")){
            widget_enabled =  true;
        }


        loadData();
        return view;
    }

    private void loadData() {
        swipe_refreshl_status_fragment.setRefreshing(true);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<HomeResponse> call = service.home(language);
        call.enqueue(new Callback<HomeResponse>() {
            @Override
            public void onResponse(Call<HomeResponse> call, Response<HomeResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getPosts().size()==0
                            && response.body().getSlides().size()==0
                            && response.body().getQuestions().size()==0
                            && response.body().getCategories().size()==0
                            )
                    {
                        linear_layout_page_error.setVisibility(View.GONE);
                        imageView_empty_favorite.setVisibility(View.VISIBLE);
                        recycler_view_status_fragment.setVisibility(View.GONE);
                    }else {
                        for (int i = 0; i < response.body().getSlides().size(); i++) {
                            slideList.add(response.body().getSlides().get(i));
                        }
                        if (response.body().getSlides().size()>0){
                            postList.add(new Post().setViewType(3));
                        }
                        if (widget_enabled)
                            postList.add(new Post().setViewType(9));

                        for (int i = 0; i < response.body().getCategories().size(); i++) {
                            categoryList.add(response.body().getCategories().get(i));
                        }
                        if (response.body().getCategories().size()>0){
                            postList.add(new Post().setViewType(2));
                        }
                        for (int i = 0; i < response.body().getQuestions().size(); i++) {
                            questionList.add(response.body().getQuestions().get(i));
                        }
                        if (response.body().getQuestions().size()>0){
                            postList.add(new Post().setViewType(7));
                        }
                        for (int i = 0; i < response.body().getPosts().size(); i++) {
                            postList.add(response.body().getPosts().get(i));
                            if (native_ads_enabled){
                                item++;
                                if (item == lines_beetween_ads ){
                                    item= 0;
                                    if (getResources().getString(R.string.NATIVE_ADS_TYPE).equals("admob")) {
                                        postList.add(new Post().setViewType(8));
                                    }else if (getResources().getString(R.string.NATIVE_ADS_TYPE).equals("facebook")){
                                        postList.add(new Post().setViewType(4));
                                    } else if (getResources().getString(R.string.NATIVE_ADS_TYPE).equals("both")){
                                        if (type_ads == 0) {
                                            postList.add(new Post().setViewType(8));
                                            type_ads = 1;
                                        }else if (type_ads == 1){
                                            postList.add(new Post().setViewType(4));
                                            type_ads = 0;
                                        }
                                    }
                                }
                            }
                        }
                        page++;
                        loading=true;
                        postAdapter.notifyDataSetChanged();
                        linear_layout_page_error.setVisibility(View.GONE);
                        imageView_empty_favorite.setVisibility(View.GONE);
                        recycler_view_status_fragment.setVisibility(View.VISIBLE);
                    }

                }else{
                    linear_layout_page_error.setVisibility(View.VISIBLE);
                    imageView_empty_favorite.setVisibility(View.GONE);
                    recycler_view_status_fragment.setVisibility(View.GONE);
                }
                swipe_refreshl_status_fragment.setRefreshing(false);

            }
            @Override
            public void onFailure(Call<HomeResponse> call, Throwable t) {
                linear_layout_page_error.setVisibility(View.VISIBLE);
                imageView_empty_favorite.setVisibility(View.GONE);
                recycler_view_status_fragment.setVisibility(View.GONE);
                swipe_refreshl_status_fragment.setRefreshing(false);
                Toast.makeText(getActivity(), ""+t.getMessage().toString()+" = ", Toast.LENGTH_SHORT).show();

            }
        });
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);


    }
    private void initAction() {
        this.swipe_refreshl_status_fragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                categoryList.clear();
                questionList.clear();
                postList.clear();
                slideList.clear();

                postAdapter.notifyDataSetChanged();
                item = 0;
                page = 0;
                loading = true;
                loadData();
            }
        });
        button_try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryList.clear();
                questionList.clear();
                postList.clear();
                slideList.clear();

                postAdapter.notifyDataSetChanged();
                item = 0;
                page = 0;
                loading = true;
                loadData();
            }
        });
    }

    private void initView() {
        if (getResources().getString(R.string.FACEBOOK_ADS_ENABLED_NATIVE).equals("true")){
            native_ads_enabled=true;
            lines_beetween_ads=Integer.parseInt(getResources().getString(R.string.NATIVE_ADS_ITEM_BETWWEN_ADS));
        }
        PrefManager prefManager= new PrefManager(getActivity().getApplicationContext());
        if (prefManager.getString("SUBSCRIBED").equals("TRUE")) {
            native_ads_enabled=false;
        }
        this.relative_layout_load_more=(RelativeLayout) view.findViewById(R.id.relative_layout_load_more);
        this.button_try_again =(Button) view.findViewById(R.id.button_try_again);
        this.swipe_refreshl_status_fragment=(SwipeRefreshLayout) view.findViewById(R.id.swipe_refreshl_status_fragment);
        this.linear_layout_page_error=(LinearLayout) view.findViewById(R.id.linear_layout_page_error);
        this.imageView_empty_favorite=(ImageView) view.findViewById(R.id.imageView_empty_favorite);
        this.recycler_view_status_fragment=(RecyclerView) view.findViewById(R.id.recycler_view_status_fragment);
        this.linearLayoutManager=  new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        postAdapter =new PostAdapter(postList,categoryList,questionList,slideList,getActivity());

        recycler_view_status_fragment.setHasFixedSize(true);
        recycler_view_status_fragment.setAdapter(postAdapter);
        recycler_view_status_fragment.setLayoutManager(linearLayoutManager);
        recycler_view_status_fragment.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {

                    visibleItemCount    = linearLayoutManager.getChildCount();
                    totalItemCount      = linearLayoutManager.getItemCount();
                    pastVisiblesItems   = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            loadNextStatus();
                        }
                    }
                }else{

                }
            }
        });
    }

    public void loadNextStatus(){
        relative_layout_load_more.setVisibility(View.VISIBLE);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Post>> call = service.imageAll(page,"created",language);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(response.isSuccessful()){

                    if (response.body().size()!=0){
                        for (int i=0;i<response.body().size();i++){
                            postList.add(response.body().get(i));
                            if (native_ads_enabled){
                                item++;
                                if (item == lines_beetween_ads ){
                                    item= 0;
                                    if (getResources().getString(R.string.NATIVE_ADS_TYPE).equals("admob")) {
                                        postList.add(new Post().setViewType(8));
                                    }else if (getResources().getString(R.string.NATIVE_ADS_TYPE).equals("facebook")){
                                        postList.add(new Post().setViewType(4));
                                    } else if (getResources().getString(R.string.NATIVE_ADS_TYPE).equals("both")){
                                        if (type_ads == 0) {
                                            postList.add(new Post().setViewType(8));
                                            type_ads = 1;
                                        }else if (type_ads == 1){
                                            postList.add(new Post().setViewType(4));
                                            type_ads = 0;
                                        }
                                    }
                                }
                            }
                        }
                        postAdapter.notifyDataSetChanged();
                        page++;
                        loading=true;

                    }else {

                    }
                    relative_layout_load_more.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                relative_layout_load_more.setVisibility(View.GONE);
            }
        });
    }


}
