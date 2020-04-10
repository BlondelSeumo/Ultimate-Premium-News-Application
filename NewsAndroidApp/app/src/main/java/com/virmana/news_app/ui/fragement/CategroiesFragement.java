package com.virmana.news_app.ui.fragement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
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

/**
 * Created by Tamim on 01/11/2018.
 */

public class CategroiesFragement extends Fragment {


    private Integer page = 0;

    private View view;
    private PrefManager prefManager;
    private String language = "0";
    private boolean loaded = false;
    private RelativeLayout relative_layout_load_more;
    private Button button_try_again;
    private SwipeRefreshLayout swipe_refreshl_categroies_fragment;
    private LinearLayout linear_layout_page_error;
    private LinearLayout linear_layout_load_categroies_fragment;
    private RecyclerView recycler_view_categroies_fragment;


    private List<Category> categoryList =new ArrayList<>();
    private AllCategoryVideoAdapter allCategoryVideoAdapter;
    private GridLayoutManager gridLayoutManager;


    public CategroiesFragement() {
        // Required empty public constructor
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser){
            if (!loaded){
                loaded =  true;
                loadStatusCategory();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        this.view =  inflater.inflate(R.layout.fragment_categories, container, false);


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
        this.swipe_refreshl_categroies_fragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                loadStatusCategory();
            }
        });
        button_try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadStatusCategory();
            }
        });
    }

    private void initView() {

        this.relative_layout_load_more=(RelativeLayout) view.findViewById(R.id.relative_layout_load_more);
        this.button_try_again =(Button) view.findViewById(R.id.button_try_again);
        this.swipe_refreshl_categroies_fragment=(SwipeRefreshLayout) view.findViewById(R.id.swipe_refreshl_categroies_fragment);
        this.linear_layout_page_error=(LinearLayout) view.findViewById(R.id.linear_layout_page_error);
        this.linear_layout_load_categroies_fragment=(LinearLayout) view.findViewById(R.id.linear_layout_load_categroies_fragment);
        this.recycler_view_categroies_fragment=(RecyclerView) view.findViewById(R.id.recycler_view_categroies_fragment);
        this.gridLayoutManager=  new GridLayoutManager(getActivity(),1);

        allCategoryVideoAdapter =new AllCategoryVideoAdapter(categoryList,getActivity());
        recycler_view_categroies_fragment.setHasFixedSize(true);
        recycler_view_categroies_fragment.setAdapter(allCategoryVideoAdapter);
        recycler_view_categroies_fragment.setLayoutManager(gridLayoutManager);

    }

    public void loadStatusCategory(){
        swipe_refreshl_categroies_fragment.setRefreshing(true);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        this.prefManager= new PrefManager(getActivity().getApplicationContext());

        this.language=prefManager.getString("LANGUAGE_DEFAULT");
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
                    recycler_view_categroies_fragment.setVisibility(View.VISIBLE);
                    linear_layout_load_categroies_fragment.setVisibility(View.GONE);
                    linear_layout_page_error.setVisibility(View.GONE);
                    swipe_refreshl_categroies_fragment.setRefreshing(false);
                }else{
                    recycler_view_categroies_fragment.setVisibility(View.GONE);
                    linear_layout_load_categroies_fragment.setVisibility(View.GONE);
                    linear_layout_page_error.setVisibility(View.VISIBLE);
                    swipe_refreshl_categroies_fragment.setRefreshing(false);
                }
            }
            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                recycler_view_categroies_fragment.setVisibility(View.GONE);
                linear_layout_load_categroies_fragment.setVisibility(View.GONE);
                linear_layout_page_error.setVisibility(View.VISIBLE);
                swipe_refreshl_categroies_fragment.setRefreshing(false);
            }
        });
    }
}
