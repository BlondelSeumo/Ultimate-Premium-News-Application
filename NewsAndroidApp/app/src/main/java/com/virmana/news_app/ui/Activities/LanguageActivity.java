package com.virmana.news_app.ui.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.leo.simplearcloader.SimpleArcLoader;
import com.virmana.news_app.R;
import com.virmana.news_app.Adapters.LanguageAdapter;
import com.virmana.news_app.Adapters.SelectableViewHolder;
import com.virmana.news_app.api.apiClient;
import com.virmana.news_app.api.apiRest;
import com.virmana.news_app.model.Language;
import com.virmana.news_app.Provider.LanguageStorage;
import com.virmana.news_app.Provider.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LanguageActivity extends AppCompatActivity implements SelectableViewHolder.OnItemSelectedListener {

    private LanguageAdapter languageAdapter;
    private final List<Language> languageList = new ArrayList<>();
    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerView;
    private SimpleArcLoader simple_arc_loader_lang;
    private Toolbar toolbar;
    private PrefManager prefManager;
    private Button button_select_langauge;
    private Button button_back_langauge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefManager= new PrefManager(getApplicationContext());

        setContentView(R.layout.activity_language);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        initView();
        loadLang();
        initAction();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.button_back_langauge=(Button) findViewById(R.id.button_back_langauge);
        this.button_select_langauge=(Button) findViewById(R.id.button_select_langauge);
        this.simple_arc_loader_lang=(SimpleArcLoader) findViewById(R.id.simple_arc_loader_lang);
        gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView = (RecyclerView) findViewById(R.id.selection_list);

    }
    public void initAction(){

        this.button_select_langauge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = "";
                for (int i = 0; i < languageAdapter.getSelectedItems().size(); i++) {
                    if (i == 0) {
                        s += +languageAdapter.getSelectedItems().get(i).getId();

                    } else {
                        s += "," + languageAdapter.getSelectedItems().get(i).getId();

                    }
                    for (int j = 0; j < languageList.size(); j++) {
                        if (languageList.get(j).getId().equals(languageAdapter.getSelectedItems().get(i).getId())) {
                            languageList.get(j).setSelected(true);
                        }
                    }
                }
                languageAdapter.notifyDataSetChanged();
                prefManager.setString("LANGUAGE_DEFAULT", s);

                final LanguageStorage storageFavorites = new LanguageStorage(getApplicationContext());
                ArrayList<Language> storage = new ArrayList<Language>();
                for (int j = 0; j < languageList.size(); j++) {
                    storage.add(languageList.get(j));
                }
                storageFavorites.StoreLang(storage);


                finish();
            }
        });
    this.button_back_langauge.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    });

    }

    @Override
    public void onItemSelected(Language item) {

    }

    public void loadLang(){
        recyclerView.setVisibility(View.GONE);
        simple_arc_loader_lang.setVisibility(View.VISIBLE);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Language>> call = service.languageAll();
        call.enqueue(new Callback<List<Language>>() {
            @Override
            public void onResponse(Call<List<Language>> call, final Response<List<Language>> response) {
                if (response.isSuccessful()){
                    for (int i = 0; i <response.body().size() ; i++) {
                        languageList.add(response.body().get(i));
                    }
                    recyclerView.setVisibility(View.VISIBLE);
                    simple_arc_loader_lang.setVisibility(View.GONE);




                    if (response.isSuccessful()) {
                        languageList.clear();
                        String s = prefManager.getString("LANGUAGE_DEFAULT");
                        String[] array = s.split(",");
                        for (int i = 0; i < response.body().size(); i++) {
                            languageList.add(response.body().get(i));
                            if (s.length() != 0) {
                                for (int j = 0; j < array.length; j++) {
                                    if (languageList.get(i).getId() == Integer.parseInt(array[j])) {
                                        languageList.get(i).setSelected(true);
                                    }
                                }
                            }
                        }
                    }

                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    languageAdapter = new LanguageAdapter(LanguageActivity.this, languageList, false, LanguageActivity.this);
                    recyclerView.setAdapter(languageAdapter);
                }else{
                    recyclerView.setVisibility(View.GONE);
                    simple_arc_loader_lang.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<List<Language>> call, Throwable t) {
                recyclerView.setVisibility(View.GONE);
                simple_arc_loader_lang.setVisibility(View.GONE);
            }
        });
    }
}
