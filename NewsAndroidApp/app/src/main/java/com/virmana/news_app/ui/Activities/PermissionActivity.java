package com.virmana.news_app.ui.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rm.rmswitch.RMSwitch;
import com.virmana.news_app.R;
import com.virmana.news_app.Provider.PrefManager;

public class PermissionActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private static final int REQUEST_WRITE_PERMISSION = 786;
    private static final int REQUEST_WRITE_PERMISSION_LOCATION = 586;
    private CardView card_view_allow_permission;
    private PrefManager prefManager;
    private RMSwitch rm_switch_view_weather_one;
    private RMSwitch rm_switch_view_weather_unit;
    private TextView text_view_temp_unit_title;
    private RelativeLayout relative_layout_weather;
    private RelativeLayout relative_layout_permission;
    private CardView card_view_finish;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_PERMISSION : {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    relative_layout_permission.setVisibility(View.GONE);
                    relative_layout_weather.setVisibility(View.VISIBLE);
                }
                return;
            }
            case REQUEST_WRITE_PERMISSION_LOCATION:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    rm_switch_view_weather_one.setChecked(true);
                    rm_switch_view_weather_unit.setEnabled(true);
                    prefManager.setString("widget_enabled","true");

                    card_view_finish.setVisibility(View.VISIBLE);
                }

            }
            return;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        prefManager= new PrefManager(getApplicationContext());

        prefManager.getString("widget_enabled").equals("false");
        initView();
        initAction();
    }

    private void initAction() {
        this.card_view_allow_permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });

        this.card_view_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_status  =  new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent_status);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                finish();
            }
        });
        this.rm_switch_view_weather_one.addSwitchObserver(new RMSwitch.RMSwitchObserver() {
            @Override
            public void onCheckStateChange(RMSwitch switchView, boolean isChecked) {

                if (isChecked){
                    if (ActivityCompat.checkSelfPermission(PermissionActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(PermissionActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            ActivityCompat.requestPermissions(PermissionActivity.this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, REQUEST_WRITE_PERMISSION_LOCATION);
                        }
                        rm_switch_view_weather_one.setChecked(false);
                        return;
                    }
                    prefManager.setString("widget_enabled","true");
                    rm_switch_view_weather_unit.setEnabled(true);
                }else{
                    prefManager.setString("widget_enabled","false");
                    rm_switch_view_weather_unit.setEnabled(false);
                }
            }
        });
        this.rm_switch_view_weather_unit.addSwitchObserver(new RMSwitch.RMSwitchObserver() {
            @Override
            public void onCheckStateChange(RMSwitch switchView, boolean isChecked) {
                if (isChecked){
                    prefManager.setString("units","metric");
                    text_view_temp_unit_title.setText("Unite weather : Celsius (°C)");
                }else{
                    prefManager.setString("units","imperial");
                    text_view_temp_unit_title.setText("Unite weather : Farad (°F)");
                }
            }
        });
    }

    private void initView() {
        this.card_view_finish=(CardView) findViewById(R.id.card_view_finish);
        this.relative_layout_permission=(RelativeLayout) findViewById(R.id.relative_layout_permission);
        this.relative_layout_weather=(RelativeLayout) findViewById(R.id.relative_layout_weather);
        this.card_view_allow_permission=(CardView) findViewById(R.id.card_view_allow_permission);
        this.text_view_temp_unit_title= (TextView) findViewById(R.id.text_view_temp_unit_title);
        this.rm_switch_view_weather_unit= (RMSwitch) findViewById(R.id.rm_switch_view_weather_unit);
        this.rm_switch_view_weather_one= (RMSwitch) findViewById(R.id.rm_switch_view_weather_one);
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(PermissionActivity.this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE }, REQUEST_WRITE_PERMISSION);
        }
    }

    private void requestPermissionLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(PermissionActivity.this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, REQUEST_WRITE_PERMISSION_LOCATION);
        }
    }

}
