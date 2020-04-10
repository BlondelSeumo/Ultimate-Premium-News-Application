package com.virmana.news_app.ui.fragement;



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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.virmana.news_app.R;
import com.virmana.news_app.Adapters.PostAdapter;
import com.virmana.news_app.api.apiClient;
import com.virmana.news_app.api.apiRest;
import com.virmana.news_app.model.Category;
import com.virmana.news_app.model.Keyword;
import com.virmana.news_app.model.Post;
import com.virmana.news_app.Provider.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class PopularFragment extends Fragment {

    private Integer type_ads = 0;
    private Integer page = 0;

    private View view;
    private PrefManager prefManager;
    private String language = "0";
    private boolean loaded = false;
    private RelativeLayout relative_layout_load_more;
    private Button button_try_again;
    private SwipeRefreshLayout swipe_refreshl_popular_fragment;
    private LinearLayout linear_layout_page_error;
    private LinearLayout linear_layout_load_popular_fragment;
    private RecyclerView recycler_view_popular_fragment;
    private LinearLayoutManager linearLayoutManager;

    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;

    private List<Post> postList =new ArrayList<>();
    private List<Category> categoryList =new ArrayList<>();
    private List<Keyword> keywordList =new ArrayList<>();
    private PostAdapter postAdapter;
    private Integer item = 0 ;
    private Integer lines_beetween_ads = 8 ;
    private Boolean native_ads_enabled = false ;


    public PopularFragment() {
        // Required empty public constructor
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser){
            if (!loaded){
                loadTags();
                loaded = true;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        this.view =  inflater.inflate(R.layout.fragment_popular, container, false);
        this.prefManager= new PrefManager(getActivity().getApplicationContext());

        this.language=prefManager.getString("LANGUAGE_DEFAULT");
        Keyword keyword= new Keyword();
        keyword.setId(0);
        keyword.setName("Techno");
        keyword.setSearch(100);
        keywordList.add(keyword);

        Keyword keyword1= new Keyword();
        keyword1.setId(1);
        keyword1.setName("Sport");
        keyword1.setSearch(100);
        keywordList.add(keyword1);

        Keyword keyword2= new Keyword();

        keyword2.setId(2);
        keyword2.setName("Travel");
        keyword2.setSearch(100);
        keywordList.add(keyword2);

        Keyword keyword3= new Keyword();

        keyword3.setId(3);
        keyword3.setName("Movies");
        keyword3.setSearch(100);
        keywordList.add(keyword3);
        Keyword keyword4= new Keyword();

        keyword4.setId(4);
        keyword4.setName("Space");
        keyword4.setSearch(100);
        keywordList.add(keyword4);
        Keyword keyword5= new Keyword();

        keyword5.setId(5);
        keyword5.setName("House");
        keyword5.setSearch(100);
        keywordList.add(keyword5);
        Keyword keyword6= new Keyword();

        keyword6.setId(6);
        keyword6.setName("Politic");
        keyword6.setSearch(100);
        keywordList.add(keyword6);
        Keyword keyword7= new Keyword();

        keyword7.setId(7);
        keyword7.setName("Nature");
        keyword7.setSearch(100);
        keywordList.add(keyword7);


        initView();
        initAction();

        return view;
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


    private void initAction() {
        this.swipe_refreshl_popular_fragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                loading = true;
                item = 0;
                loadTags();
            }
        });
        button_try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 0;
                item = 0;
                loading = true;

                loadTags();
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
        this.swipe_refreshl_popular_fragment=(SwipeRefreshLayout) view.findViewById(R.id.swipe_refreshl_popular_fragment);
        this.linear_layout_page_error=(LinearLayout) view.findViewById(R.id.linear_layout_page_error);
        this.linear_layout_load_popular_fragment=(LinearLayout) view.findViewById(R.id.linear_layout_load_popular_fragment);
        this.recycler_view_popular_fragment=(RecyclerView) view.findViewById(R.id.recycler_view_popular_fragment);
        this.linearLayoutManager=  new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


        postAdapter =new PostAdapter(postList,categoryList,null,null,keywordList,getActivity(),false,false);

        recycler_view_popular_fragment.setHasFixedSize(true);
        recycler_view_popular_fragment.setAdapter(postAdapter);
        recycler_view_popular_fragment.setLayoutManager(linearLayoutManager);
        recycler_view_popular_fragment.addOnScrollListener(new RecyclerView.OnScrollListener()
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

    public void loadTags(){
        swipe_refreshl_popular_fragment.setRefreshing(true);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Keyword>> call = service.PopularTagsByLanguage(0,language);
        call.enqueue(new Callback<List<Keyword>>() {
            @Override
            public void onResponse(Call<List<Keyword>> call, Response<List<Keyword>> response) {
                apiClient.FormatData(getActivity(),response);
                keywordList.clear();
                if (response.isSuccessful()){
                    for (int i = 0; i < response.body().size(); i++) {
                        keywordList.add(response.body().get(i));
                    }
                }
                loadStatus();
            }
            @Override
            public void onFailure(Call<List<Keyword>> call, Throwable t) {
                loadStatus();
            }
        });
    }
    public void loadStatus(){
        recycler_view_popular_fragment.setVisibility(View.GONE);
        linear_layout_load_popular_fragment.setVisibility(View.VISIBLE);
        linear_layout_page_error.setVisibility(View.GONE);
        swipe_refreshl_popular_fragment.setRefreshing(true);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Post>> call = service.imageAll(page, "views",language);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                apiClient.FormatData(getActivity(),response);
                if(response.isSuccessful()){
                    postList.clear();
                    if (keywordList.size()>0){
                        postList.add(new Post().setViewType(6));
                    }
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
                        loaded=true;
                    }else {

                    }
                    recycler_view_popular_fragment.setVisibility(View.VISIBLE);
                    linear_layout_load_popular_fragment.setVisibility(View.GONE);
                    linear_layout_page_error.setVisibility(View.GONE);

                }else{
                    recycler_view_popular_fragment.setVisibility(View.GONE);
                    linear_layout_load_popular_fragment.setVisibility(View.GONE);
                    linear_layout_page_error.setVisibility(View.VISIBLE);
                }
                swipe_refreshl_popular_fragment.setRefreshing(false);

            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                recycler_view_popular_fragment.setVisibility(View.GONE);
                linear_layout_load_popular_fragment.setVisibility(View.GONE);
                linear_layout_page_error.setVisibility(View.VISIBLE);
                swipe_refreshl_popular_fragment.setRefreshing(false);
            }
        });
    }
    public void loadNextStatus(){
        relative_layout_load_more.setVisibility(View.VISIBLE);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Post>> call = service.imageAll(page,"views",language);
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
