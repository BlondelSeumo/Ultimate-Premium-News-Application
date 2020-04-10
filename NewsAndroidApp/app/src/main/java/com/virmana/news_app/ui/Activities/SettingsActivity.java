package com.virmana.news_app.ui.Activities;



import com.rm.rmswitch.RMSwitch;
import com.virmana.news_app.R;
import com.virmana.news_app.Provider.PrefManager;

import android.Manifest;
import android.content.pm.Signature;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

import es.dmoral.toasty.Toasty;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
public class SettingsActivity extends AppCompatActivity {

    private LinearLayout linear_layout_clea_cache;
    private TextView text_view_cache_value;
    private PrefManager prf;
    private Switch switch_button_notification;
    private TextView text_view_version;
    private LinearLayout linearLayout_policy_privacy;
    private LinearLayout linearLayout_contact_us;
    private LinearLayout linear_layout_hash;
    private TextView text_view_temp_unit_title;
    private RMSwitch rm_switch_view_weather_unit;
    private RMSwitch rm_switch_view_weather_one;
    private static final int REQUEST_WRITE_PERMISSION_LOCATION = 586;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        prf= new PrefManager(getApplicationContext());

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.action_settings));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        setValues();
        initAction();
        initializeCache();
    }

    private void initAction() {

        this.linear_layout_clea_cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCache(getApplicationContext());
                initializeCache();
            }
        });
        this.switch_button_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b==true){
                    prf.setString("notifications","true");
                }else{
                    prf.setString("notifications","false");
                }
            }
        });
        this.linearLayout_policy_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PolicyActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);

            }
        });

        this.linearLayout_contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SupportActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);

            }
        });
        this.linear_layout_hash.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                printHashKey(getApplicationContext());
                return false;
            }
        });
        this.rm_switch_view_weather_one.addSwitchObserver(new RMSwitch.RMSwitchObserver() {
            @Override
            public void onCheckStateChange(RMSwitch switchView, boolean isChecked) {

                if (isChecked){
                    if (ActivityCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            ActivityCompat.requestPermissions(SettingsActivity.this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, REQUEST_WRITE_PERMISSION_LOCATION);
                        }
                        rm_switch_view_weather_one.setChecked(false);
                        return;
                    }
                    prf.setString("widget_enabled","true");
                    rm_switch_view_weather_unit.setEnabled(true);
                }else{
                    prf.setString("widget_enabled","false");
                    rm_switch_view_weather_unit.setEnabled(false);
                }
            }
        });
        this.rm_switch_view_weather_unit.addSwitchObserver(new RMSwitch.RMSwitchObserver() {
            @Override
            public void onCheckStateChange(RMSwitch switchView, boolean isChecked) {
                if (isChecked){
                    prf.setString("units","metric");
                    text_view_temp_unit_title.setText("Unite weather : Celsius (°C)");
                }else{
                    prf.setString("units","imperial");
                    text_view_temp_unit_title.setText("Unite weather : Farad (°F)");
                }
            }
        });
    }
    public  void printHashKey(Context pContext) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("HASKEY", "printHashKey() Hash Key: " + hashKey);
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("haskey", hashKey);
                clipboard.setPrimaryClip(clip);
                Toasty.info(getApplicationContext(),"haskey has been copied",Toast.LENGTH_SHORT).show();
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("HASKEY", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("HASKEY", "printHashKey()", e);
        }
    }
    private void initView() {
        this.text_view_temp_unit_title= (TextView) findViewById(R.id.text_view_temp_unit_title);
        this.rm_switch_view_weather_unit= (RMSwitch) findViewById(R.id.rm_switch_view_weather_unit);
        this.rm_switch_view_weather_one= (RMSwitch) findViewById(R.id.rm_switch_view_weather_one);
        this.linear_layout_clea_cache= (LinearLayout) findViewById(R.id.linear_layout_clea_cache);
        this.text_view_cache_value=(TextView) findViewById(R.id.text_view_cache_value);
        this.switch_button_notification=(Switch) findViewById(R.id.switch_button_notification);
        this.text_view_version=(TextView) findViewById(R.id.text_view_version);
        this.linearLayout_policy_privacy=(LinearLayout) findViewById(R.id.linearLayout_policy_privacy);
        this.linearLayout_contact_us=(LinearLayout) findViewById(R.id.linearLayout_contact_us);
        this.linear_layout_hash=(LinearLayout) findViewById(R.id.linear_layout_hash);


        if (prf.getString("widget_enabled").equals("false")){
            this.rm_switch_view_weather_one.setChecked(false);
            rm_switch_view_weather_unit.setEnabled(false);

        }else{
            this.rm_switch_view_weather_one.setChecked(true);
            rm_switch_view_weather_unit.setEnabled(true);
        }

        if (prf.getString("units").equals("metric") ){
            text_view_temp_unit_title.setText("Unite weather : Celsius (°C)");
            rm_switch_view_weather_unit.setChecked(true);
        }else{
            text_view_temp_unit_title.setText("Unite weather : Farad (°F)");
            rm_switch_view_weather_unit.setChecked(false);

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
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
    private void initializeCache() {
        long size = 0;
        try {
            size += getDirSize(this.getCacheDir());
            size += getDirSize(this.getExternalCacheDir());
        }catch (Exception  e){

        }

        this.text_view_cache_value.setText(getResources().getString(R.string.label_cache)  + readableFileSize(size));

    }

    public long getDirSize(File dir){
        long size = 0;

        for (File file : dir.listFiles()) {
            if (file != null && file.isDirectory()) {
                size += getDirSize(file);
            } else if (file != null && file.isFile()) {
                size += file.length();
            }
        }
        return size;
    }

    public static String readableFileSize(long size) {
        if (size <= 0) return "0 Bytes";
        final String[] units = new String[]{"Bytes", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }



    private void setValues() {
        if (prf.getString("notifications").equals("false")){
            this.switch_button_notification.setChecked(false);
        }else{
            this.switch_button_notification.setChecked(true);
        }
        try {
            PackageInfo pInfo =getPackageManager().getPackageInfo(getPackageName(),0);
            String version = pInfo.versionName;
            text_view_version.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {

            case REQUEST_WRITE_PERMISSION_LOCATION:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    rm_switch_view_weather_one.setChecked(true);
                    rm_switch_view_weather_unit.setEnabled(true);
                    prf.setString("widget_enabled","true");
                }

            }
            return;
        }
    }

}
