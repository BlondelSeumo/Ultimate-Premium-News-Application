package com.virmana.news_app.ui.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.vivchar.viewpagerindicator.ViewPagerIndicator;
import com.virmana.news_app.R;
import com.virmana.news_app.Adapters.IntroAdapter;
import com.virmana.news_app.Provider.PrefManager;
import com.virmana.news_app.ui.view.CarouselEffectTransformer;
import com.virmana.news_app.ui.view.ClickableViewPager;

import java.util.ArrayList;
import java.util.List;

public class SlideActivity extends AppCompatActivity {
    private ClickableViewPager view_pager_slide;
    private IntroAdapter introAdapter;
    private List<Integer> slideList= new ArrayList<>();
    private ViewPagerIndicator view_pager_indicator;
    private RelativeLayout relative_layout_slide;
    private LinearLayout linear_layout_skip;
    private PrefManager prefManager;
    private LinearLayout linear_layout_next;
    private TextView text_view_next_done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);
        prefManager= new PrefManager(getApplicationContext());

        slideList.add(1);
        slideList.add(2);
        slideList.add(3);
        slideList.add(4);
        slideList.add(5);
        slideList.add(6);
        slideList.add(7);

        this.text_view_next_done=(TextView) findViewById(R.id.text_view_next_done);
        this.linear_layout_next=(LinearLayout) findViewById(R.id.linear_layout_next);
        this.linear_layout_skip=(LinearLayout) findViewById(R.id.linear_layout_skip);
        this.view_pager_indicator=(ViewPagerIndicator) findViewById(R.id.view_pager_indicator);
        this.view_pager_slide=(ClickableViewPager) findViewById(R.id.view_pager_slide);
        this.relative_layout_slide=(RelativeLayout) findViewById(R.id.relative_layout_slide);
        introAdapter = new IntroAdapter(getApplicationContext(),slideList);
        view_pager_slide.setAdapter(this.introAdapter);
        view_pager_slide.setOffscreenPageLimit(1);
        view_pager_slide.setPageTransformer(false, new CarouselEffectTransformer(SlideActivity.this)); // Set transformer


        view_pager_slide.setOnItemClickListener(new ClickableViewPager.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position <7){
                    view_pager_slide.setCurrentItem(position+1);
                }else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        if (ContextCompat.checkSelfPermission(SlideActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)  != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(SlideActivity.this,   Manifest.permission.READ_CONTACTS)) {
                                Intent intent_status  =  new Intent(getApplicationContext(), PermissionActivity.class);
                                startActivity(intent_status);
                                overridePendingTransition(R.anim.enter, R.anim.exit);
                                finish();
                            } else {
                                Intent intent_status  =  new Intent(getApplicationContext(), PermissionActivity.class);
                                startActivity(intent_status);
                                overridePendingTransition(R.anim.enter, R.anim.exit);
                                finish();
                            }
                        }else{
                            Intent intent = new Intent(SlideActivity.this,MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            finish();
                        }

                    }else{
                        if (!prefManager.getString("first_lang_set").equals("true")){
                            prefManager.setString("first_lang_set","true");
                            Intent intent_status  =  new Intent(getApplicationContext(), LanguageActivity.class);
                            startActivity(intent_status);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            finish();
                        }else{
                            Intent intent = new Intent(SlideActivity.this,MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            finish();
                        }
                    }
                }
            }
        });
        this.linear_layout_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( view_pager_slide.getCurrentItem() < slideList.size()) {
                    view_pager_slide.setCurrentItem(view_pager_slide.getCurrentItem() + 1);
                }
                if (view_pager_slide.getCurrentItem() +1==slideList.size()){

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        if (ContextCompat.checkSelfPermission(SlideActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)  != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(SlideActivity.this,   Manifest.permission.READ_CONTACTS)) {
                                Intent intent_status  =  new Intent(getApplicationContext(), PermissionActivity.class);
                                startActivity(intent_status);
                                overridePendingTransition(R.anim.enter, R.anim.exit);
                                finish();
                            } else {
                                Intent intent_status  =  new Intent(getApplicationContext(), PermissionActivity.class);
                                startActivity(intent_status);
                                overridePendingTransition(R.anim.enter, R.anim.exit);
                                finish();
                            }
                        }else{
                            Intent intent = new Intent(SlideActivity.this,MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            finish();
                        }

                    }else{
                        if (!prefManager.getString("first_lang_set").equals("true")){
                            prefManager.setString("first_lang_set","true");
                            Intent intent_status  =  new Intent(getApplicationContext(), LanguageActivity.class);
                            startActivity(intent_status);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            finish();
                        }else{
                            Intent intent = new Intent(SlideActivity.this,MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            finish();
                        }

                    }

                }
            }
        });
        view_pager_slide.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position+1==slideList.size()){
                    text_view_next_done.setText("DONE");
                }else{
                    text_view_next_done.setText("NEXT");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        this.linear_layout_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(SlideActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)  != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(SlideActivity.this,   Manifest.permission.READ_CONTACTS)) {
                            Intent intent_status  =  new Intent(getApplicationContext(), PermissionActivity.class);
                            startActivity(intent_status);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            finish();
                        } else {
                            Intent intent_status  =  new Intent(getApplicationContext(), PermissionActivity.class);
                            startActivity(intent_status);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            finish();
                        }
                    }else{
                        Intent intent = new Intent(SlideActivity.this,MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                        finish();
                    }

                }else{
                    if (!prefManager.getString("first_lang_set").equals("true")){
                        prefManager.setString("first_lang_set","true");
                        Intent intent_status  =  new Intent(getApplicationContext(), LanguageActivity.class);
                        startActivity(intent_status);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                        finish();
                    }else{
                        Intent intent = new Intent(SlideActivity.this,MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                        finish();
                    }

                }

            }
        });
        this.view_pager_slide.setClipToPadding(false);
        this.view_pager_slide.setPageMargin(0);
        view_pager_indicator.setupWithViewPager(view_pager_slide);
    }

}
