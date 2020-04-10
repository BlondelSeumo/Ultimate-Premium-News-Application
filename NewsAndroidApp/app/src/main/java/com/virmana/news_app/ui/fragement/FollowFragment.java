package com.virmana.news_app.ui.fragement;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.virmana.news_app.R;
import com.virmana.news_app.Adapters.PostAdapter;
import com.virmana.news_app.api.apiClient;
import com.virmana.news_app.api.apiRest;
import com.virmana.news_app.model.Post;
import com.virmana.news_app.model.User;
import com.virmana.news_app.Provider.PrefManager;
import com.virmana.news_app.ui.Activities.LoginActivity;
import com.virmana.news_app.ui.Activities.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowFragment extends Fragment {

    private Integer type_ads = 0;
    private Integer page = 0;
    private Boolean loaded=false;

    private View view;
    private RelativeLayout relative_layout_follow_fragment;
    private SwipeRefreshLayout swipe_refreshl_follow_fragment;
    private RecyclerView recycle_view_follow_fragment;
    private RelativeLayout relative_layout_load_more;
    private LinearLayout linear_layout_page_error;
    private Button button_try_again;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    private PostAdapter postAdapter;
    private List<Post> postList =new ArrayList<>();
    private Button button_login_nav_follow_fragment;
    private LinearLayout linear_layout_follow_fragment_me;
    private Integer id_user;

    private AlertDialog.Builder builderFollowing;

    private AlertDialog.Builder builderFollowers;
    private ProgressDialog loading_progress;
    private LinearLayoutManager linearLayoutManager;
    private PrefManager prefManager;
    private String language;
    private ImageView imageView_empty_follow;
    private Integer item = 0 ;
    private Integer lines_beetween_ads = 8 ;
    private Boolean native_ads_enabled = false ;
    private List<User> userList=new ArrayList<>();


    public FollowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=  inflater.inflate(R.layout.fragment_follow, container, false);
        this.prefManager= new PrefManager(getActivity().getApplicationContext());

        this.language=prefManager.getString("LANGUAGE_DEFAULT");
        initView();
        initAction();

        return  view;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser){
            if (!loaded) {
                page = 0;
                loading = true;
                postList.clear();
                userList.clear();

                loadFollowings();
                loaded = true;
            }
        }
        else{

        }
    }

    private void initAction() {
        recycle_view_follow_fragment.addOnScrollListener(new RecyclerView.OnScrollListener()
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
        this.swipe_refreshl_follow_fragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                item = 0;
                page = 0;
                loading = true;
                postList.clear();
                userList.clear();

                loadFollowings();
            }
        });
        button_try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 0;
                item = 0;
                loading = true;
                postList.clear();
                userList.clear();

                loadFollowings();
            }
        });
        this.button_login_nav_follow_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity)getActivity()).setFromLogin();
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
            }
        });

    }
    public void initView(){
        if (getResources().getString(R.string.FACEBOOK_ADS_ENABLED_NATIVE).equals("true")){
            native_ads_enabled=true;
            lines_beetween_ads=Integer.parseInt(getResources().getString(R.string.NATIVE_ADS_ITEM_BETWWEN_ADS));
        }
        PrefManager prefManager= new PrefManager(getActivity().getApplicationContext());
        if (prefManager.getString("SUBSCRIBED").equals("TRUE")) {
            native_ads_enabled=false;
        }
        this.imageView_empty_follow=(ImageView) view.findViewById(R.id.imageView_empty_follow);
        this.relative_layout_follow_fragment=(RelativeLayout) view.findViewById(R.id.relative_layout_follow_fragment);
        this.swipe_refreshl_follow_fragment=(SwipeRefreshLayout) view.findViewById(R.id.swipe_refreshl_follow_fragment);
        this.recycle_view_follow_fragment=(RecyclerView) view.findViewById(R.id.recycle_view_follow_fragment);
        this.relative_layout_load_more=(RelativeLayout) view.findViewById(R.id.relative_layout_load_more);
        this.linear_layout_follow_fragment_me=(LinearLayout) view.findViewById(R.id.linear_layout_follow_fragment_me);
        this.linear_layout_page_error=(LinearLayout) view.findViewById(R.id.linear_layout_page_error);
        this.button_try_again=(Button) view.findViewById(R.id.button_try_again);
        this.button_login_nav_follow_fragment=(Button) view.findViewById(R.id.button_login_nav_follow_fragment);
        this.linearLayoutManager=  new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


        postAdapter =new PostAdapter(postList,null,userList,null,getActivity(),false,false);
        recycle_view_follow_fragment.setHasFixedSize(true);
        recycle_view_follow_fragment.setAdapter(postAdapter);
        recycle_view_follow_fragment.setLayoutManager(linearLayoutManager);


        recycle_view_follow_fragment.addOnScrollListener(new RecyclerView.OnScrollListener()
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
        PrefManager prf= new PrefManager(getActivity().getApplicationContext());
        if (prf.getString("LOGGED").toString().equals("TRUE")) {
            this.id_user = Integer.parseInt(prf.getString("ID_USER"));
            linear_layout_follow_fragment_me.setVisibility(View.GONE);
            relative_layout_follow_fragment.setVisibility(View.VISIBLE);
        }else{
            linear_layout_follow_fragment_me.setVisibility(View.VISIBLE);
            relative_layout_follow_fragment.setVisibility(View.GONE);
        }
    }
    private void loadNextStatus() {
        relative_layout_load_more.setVisibility(View.VISIBLE);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Post>> call = service.followImage(page,language,id_user);
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
                    }
                }
                relative_layout_load_more.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                relative_layout_load_more.setVisibility(View.GONE);
            }
        });
    }
    private void loadStatus() {

        linear_layout_page_error.setVisibility(View.GONE);
        swipe_refreshl_follow_fragment.setRefreshing(true);

        postAdapter.notifyDataSetChanged();

        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Post>> call = service.followImage(page,language,id_user);
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
                        loaded=true;
                        recycle_view_follow_fragment.setVisibility(View.VISIBLE);
                        linear_layout_page_error.setVisibility(View.GONE);
                        imageView_empty_follow.setVisibility(View.GONE);
                    }else{
                        recycle_view_follow_fragment.setVisibility(View.GONE);
                        linear_layout_page_error.setVisibility(View.GONE);
                        imageView_empty_follow.setVisibility(View.VISIBLE);
                    }

                }else{
                    recycle_view_follow_fragment.setVisibility(View.GONE);
                    linear_layout_page_error.setVisibility(View.VISIBLE);
                    imageView_empty_follow.setVisibility(View.GONE);

                }
                swipe_refreshl_follow_fragment.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                recycle_view_follow_fragment.setVisibility(View.GONE);
                linear_layout_page_error.setVisibility(View.VISIBLE);
                imageView_empty_follow.setVisibility(View.GONE);
                swipe_refreshl_follow_fragment.setRefreshing(false);

            }
        });
    }

    public void Resume() {
        try {
            PrefManager prf= new PrefManager(getActivity().getApplicationContext());

            if (prf.getString("LOGGED").toString().equals("TRUE")){
                relative_layout_follow_fragment.setVisibility(View.VISIBLE);
                linear_layout_follow_fragment_me.setVisibility(View.GONE);


                this.id_user = Integer.parseInt(prf.getString("ID_USER"));

                item = 0;
                page = 0;
                loading = true;
                postList.clear();
                userList.clear();

                loadFollowings();

            }else{
                relative_layout_follow_fragment.setVisibility(View.GONE);
                linear_layout_follow_fragment_me.setVisibility(View.VISIBLE);
            }
        }catch (java.lang.NullPointerException e){
            startActivity(new Intent(getContext(),MainActivity.class));
            getActivity().finish();
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
    public void loadFollowings(){

        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<User>> call = service.getFollowingTop(id_user);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()){
                    if (response.body().size()>0){
                        postList.add(new Post().setViewType(5));
                        for (int i=0;i<response.body().size();i++){
                            userList.add(response.body().get(i));
                        }
                        postAdapter.notifyDataSetChanged();
                    }
                    loadStatus();
                }
            }
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                loadStatus();
            }
        });

    }
}
