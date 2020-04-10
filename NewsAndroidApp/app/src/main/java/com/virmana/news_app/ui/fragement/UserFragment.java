package com.virmana.news_app.ui.fragement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.virmana.news_app.R;
import com.virmana.news_app.Adapters.PostAdapter;
import com.virmana.news_app.api.apiClient;
import com.virmana.news_app.api.apiRest;
import com.virmana.news_app.model.Post;
import com.virmana.news_app.Provider.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class UserFragment extends Fragment {
    private RelativeLayout relative_layout_load_more;
    private Button button_try_again;
    private SwipeRefreshLayout swipe_refreshl_search_fragment;
    private LinearLayout linear_layout_page_error;
    private LinearLayout linear_layout_load_search_fragment;
    private RecyclerView recycler_view_search_fragment;
    private LinearLayoutManager linearLayoutManager;



    private List<Post> postList =new ArrayList<Post>();



    private PostAdapter postAdapter;

    private View view;

    private Integer type_ads = 0;
    private Integer page = 0;
    private Boolean loaded=false;

    private Integer user;
    private String type;
    private PrefManager prefManager;
    private String language;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    private int me;

    private Integer item = 0 ;
    private Integer lines_beetween_ads = 8 ;
    private Boolean native_ads_enabled = false ;


    public UserFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.prefManager= new PrefManager(getActivity().getApplicationContext());

        user = getArguments().getInt("user");
        type = getArguments().getString("type");
        try{
            me = Integer.parseInt(prefManager.getString("ID_USER"));

        }catch (Exception e){
            me=-1;
        }


        this.language=prefManager.getString("LANGUAGE_DEFAULT");

        view= inflater.inflate(R.layout.fragment_user, container, false);

        iniView(view);
        initAction();


        getStatus();

        return view;
    }


    private void getStatus() {


        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Post>> call;
        if (user == me){
            call = service.meImage(page,user);
        }else{
            call= service.userImage("created",language,user,page);
        }
        swipe_refreshl_search_fragment.setRefreshing(true);

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
                    recycler_view_search_fragment.setVisibility(View.VISIBLE);
                    linear_layout_load_search_fragment.setVisibility(View.GONE);
                    linear_layout_page_error.setVisibility(View.GONE);

                }else{
                    recycler_view_search_fragment.setVisibility(View.GONE);
                    linear_layout_load_search_fragment.setVisibility(View.GONE);
                    linear_layout_page_error.setVisibility(View.VISIBLE);
                }
                swipe_refreshl_search_fragment.setRefreshing(false);

            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                recycler_view_search_fragment.setVisibility(View.GONE);
                linear_layout_load_search_fragment.setVisibility(View.GONE);
                linear_layout_page_error.setVisibility(View.VISIBLE);
                swipe_refreshl_search_fragment.setRefreshing(false);

            }
        });
    }


    private void getStatusNext() {

        linear_layout_load_search_fragment.setVisibility(View.VISIBLE);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Post>> call;
        if (user == me){
            call = service.meImage(page,user);
        }else{
            call= service.userImage("created",language,user,page);
        }

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
                    linear_layout_load_search_fragment.setVisibility(View.GONE);
                }

            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                linear_layout_load_search_fragment.setVisibility(View.GONE);

            }
        });
    }


    public void iniView(View  view){
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
        this.swipe_refreshl_search_fragment=(SwipeRefreshLayout) view.findViewById(R.id.swipe_refreshl_search_fragment);
        this.linear_layout_page_error=(LinearLayout) view.findViewById(R.id.linear_layout_page_error);
        this.linear_layout_load_search_fragment=(LinearLayout) view.findViewById(R.id.linear_layout_load_search_fragment);
        this.recycler_view_search_fragment=(RecyclerView) view.findViewById(R.id.recycler_view_search_fragment);
        this.linearLayoutManager=  new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        postAdapter = new PostAdapter(postList,null,null,null,getActivity(),false,true);

        recycler_view_search_fragment.setHasFixedSize(true);
        recycler_view_search_fragment.setAdapter(postAdapter);
        recycler_view_search_fragment.setLayoutManager(linearLayoutManager);

        recycler_view_search_fragment.setVisibility(View.GONE);
        linear_layout_load_search_fragment.setVisibility(View.VISIBLE);
        linear_layout_page_error.setVisibility(View.GONE);
        swipe_refreshl_search_fragment.setRefreshing(true);
    }

    public void initAction(){
        this.swipe_refreshl_search_fragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                        postList.clear();
                        page = 0;
                        item = 0;
                        loading=true;
                        getStatus();

            }
        });
        this.button_try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postList.clear();
                postAdapter.notifyDataSetChanged();
                page = 0;
                item = 0;
                loading=true;
                getStatus();
            }
        });
        recycler_view_search_fragment.addOnScrollListener(new RecyclerView.OnScrollListener()
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
                            getStatusNext();

                        }
                    }
                }else{

                }
            }
        });
    }


}
