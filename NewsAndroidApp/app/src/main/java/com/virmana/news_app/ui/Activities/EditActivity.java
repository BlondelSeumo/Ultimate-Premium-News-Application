package com.virmana.news_app.ui.Activities;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.virmana.news_app.R;
import com.virmana.news_app.api.apiClient;
import com.virmana.news_app.api.apiRest;
import com.virmana.news_app.model.ApiResponse;
import com.virmana.news_app.Provider.PrefManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditActivity extends AppCompatActivity {

    private EditText edit_input_email;
    private EditText edit_input_name;
    private EditText edit_input_facebook;
    private EditText edit_input_twitter;
    private EditText edit_input_instragram;
    private TextInputLayout edit_input_layout_instragram;
    private TextInputLayout edit_input_layout_twitter;
    private TextInputLayout edit_input_layout_facebook;
    private TextInputLayout edit_input_layout_name;
    private TextInputLayout edit_input_layout_email;
    private Button edit_button;
    private ProgressDialog register_progress;
    private int id;
    private String name;
    private String image;
    private String facebook;
    private String email;
    private String instagram;
    private String twitter;
    private ImageView image_view_user_profile;
    private TextView text_view_name_user;
    private ProgressDialog register_progress_load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras() ;
        this.id =  bundle.getInt("id");
        this.name =  bundle.getString("name");
        this.image =  bundle.getString("image");
        setContentView(R.layout.activity_edit);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //  SelectWallpaper();
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.edit_my_profile));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

        setUser();
        initAction();

    }
    public void initView(){
        this.text_view_name_user = (TextView) findViewById(R.id.text_view_name_user);
        this.edit_input_email=(EditText) findViewById(R.id.edit_input_email);
        this.edit_input_name=(EditText) findViewById(R.id.edit_input_name);
        this.edit_input_facebook=(EditText) findViewById(R.id.edit_input_facebook);
        this.edit_input_twitter=(EditText) findViewById(R.id.edit_input_twitter);
        this.edit_input_instragram=(EditText) findViewById(R.id.edit_input_instragram);
        this.edit_input_layout_email=(TextInputLayout) findViewById(R.id.edit_input_layout_email);
        this.edit_input_layout_name=(TextInputLayout) findViewById(R.id.edit_input_layout_name);
        this.edit_input_layout_facebook=(TextInputLayout) findViewById(R.id.edit_input_layout_facebook);
        this.edit_input_layout_twitter=(TextInputLayout) findViewById(R.id.edit_input_layout_twitter);
        this.edit_input_layout_instragram=(TextInputLayout) findViewById(R.id.edit_input_layout_instragram);
        this.image_view_user_profile=(ImageView) findViewById(R.id.image_view_user_profile);
        this.edit_button=(Button) findViewById(R.id.edit_button);
    }
    public void initAction(){
        this.edit_input_email.addTextChangedListener(new SupportTextWatcher(this.edit_input_email));
        this.edit_input_name.addTextChangedListener(new SupportTextWatcher(this.edit_input_name));
        this.edit_input_facebook.addTextChangedListener(new SupportTextWatcher(this.edit_input_facebook));
        this.edit_input_twitter.addTextChangedListener(new SupportTextWatcher(this.edit_input_twitter));
        this.edit_input_instragram.addTextChangedListener(new SupportTextWatcher(this.edit_input_instragram));

        this.edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }
    public void submit(){

        if (!validatName()) {
            return;
        }
        if (!validateEmail()) {
            return;
        }
        if (!validateFacebook()) {
            return;
        }
        if (!validateTwitter()) {
            return;
        }
        if (!validateInstagram()) {
            return;
        }
        editUser();

    }

    private void editUser() {
        final PrefManager prf= new PrefManager(getApplicationContext());
        if (prf.getString("LOGGED").toString().equals("TRUE")) {
            String user = prf.getString("ID_USER");
            String key = prf.getString("TOKEN_USER");

            register_progress = ProgressDialog.show(this, null, getString(R.string.progress_login));
            Retrofit retrofit = apiClient.getClient();
            apiRest service = retrofit.create(apiRest.class);
            Call<ApiResponse> call = service.editUser(Integer.parseInt(user), key, edit_input_name.getText().toString(), edit_input_email.getText().toString(), edit_input_facebook.getText().toString(), edit_input_twitter.getText().toString(), edit_input_instragram.getText().toString());
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        prf.setString("NAME_USER",edit_input_name.getText().toString());

                        Toasty.success(getApplicationContext(), getResources().getString(R.string.message_sended), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toasty.error(getApplicationContext(), getString(R.string.no_connexion), Toast.LENGTH_SHORT).show();
                    }
                    register_progress.dismiss();
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    register_progress.dismiss();
                    Toasty.error(getApplicationContext(), getString(R.string.no_connexion), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean validatName() {
        if (edit_input_name.getText().toString().trim().isEmpty() || edit_input_name.getText().length()  < 3 ) {
            edit_input_layout_name.setError(getString(R.string.error_short_value));
            requestFocus(edit_input_name);
            return false;
        } else {
            edit_input_layout_name.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateEmail() {
        if (edit_input_email.getText().toString().trim().length()==0){
            return true;
        }
        String email = edit_input_email.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {
            edit_input_layout_email.setError(getString(R.string.error_mail_valide));
            requestFocus(edit_input_email);
            return false;
        } else {
            edit_input_layout_email.setErrorEnabled(false);
        }
        return true;
    }
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    private class SupportTextWatcher implements TextWatcher {
        private View view;
        private SupportTextWatcher(View view) {
            this.view = view;
        }
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edit_input_email:
                    validateEmail();
                    break;
                case R.id.edit_input_name:
                    validatName();
                    break;
                case R.id.edit_input_facebook:
                    validateFacebook();
                    break;
                case R.id.edit_input_twitter:
                    validateTwitter();
                    break;
                case R.id.edit_input_instragram:
                    validateInstagram();
                    break;

            }
        }
    }
    private boolean validateInstagram() {
        if (edit_input_instragram.getText().toString().trim().length()==0){
            return true;
        }
        if (!URLUtil.isValidUrl(edit_input_instragram.getText().toString())) {
            edit_input_layout_instragram.setError(getString(R.string.invalide_url));
            requestFocus(edit_input_instragram);

            return false;
        }else{
            edit_input_layout_instragram.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateTwitter() {
        if (edit_input_twitter.getText().toString().trim().length()==0){
            return true;
        }
        if (!URLUtil.isValidUrl(edit_input_twitter.getText().toString())) {
            edit_input_layout_twitter.setError(getString(R.string.invalide_url));
            requestFocus(edit_input_twitter);

            return false;
        }else{
            edit_input_layout_twitter.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateFacebook() {
        if (edit_input_facebook.getText().toString().trim().length()==0){
            return true;
        }
        if (!URLUtil.isValidUrl(edit_input_facebook.getText().toString())) {
            edit_input_layout_facebook.setError(getString(R.string.invalide_url));
            requestFocus(edit_input_facebook);

            return false;
        }else{
            edit_input_layout_facebook.setErrorEnabled(false);
            return true;
        }
    }
    private void getUser() {
        register_progress_load = ProgressDialog.show(this, null, getString(R.string.loading_user_data));
        edit_input_name.setText(name);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<ApiResponse> call = service.getUser(id,id);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()){

                    for (int i=0;i<response.body().getValues().size();i++){
                        if (response.body().getValues().get(i).getName().equals("facebook")){
                            facebook = response.body().getValues().get(i).getValue();
                            if (facebook!=null){
                                if (!facebook.isEmpty()){
                                    if (facebook.startsWith("http://") || facebook.startsWith("https://")) {
                                        edit_input_facebook.setText(facebook);
                                    }
                                }
                            }
                        }
                        if (response.body().getValues().get(i).getName().equals("twitter")){
                            twitter = response.body().getValues().get(i).getValue();
                            if (twitter!=null) {

                                if (!twitter.isEmpty()) {
                                    if (twitter.startsWith("http://") || twitter.startsWith("https://")) {
                                        edit_input_twitter.setText(twitter);

                                    }
                                }
                            }
                        }
                        if (response.body().getValues().get(i).getName().equals("instagram")){

                            instagram = response.body().getValues().get(i).getValue();
                            if (instagram!=null) {

                                if (!instagram.isEmpty()){
                                    if (instagram.startsWith("http://") || instagram.startsWith("https://")) {

                                        edit_input_instragram.setText(instagram);
                                    }
                                }
                            }
                        }
                        if (response.body().getValues().get(i).getName().equals("email")){
                            email = response.body().getValues().get(i).getValue();
                            if (email!=null) {
                                if (!email.isEmpty()){
                                    edit_input_email.setText(email);
                                }
                            }
                        }

                    }

                }
                    register_progress_load.hide();
                    register_progress_load.dismiss();

            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

                register_progress_load.hide();
                register_progress_load.dismiss();


            }
        });
    }
    public void setUser(){
        this.text_view_name_user.setText(name);
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                image_view_user_profile.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                image_view_user_profile.setImageResource(R.mipmap.ic_launcher);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(this)
                .load(image)
                .error(R.drawable.profile)
                .placeholder(R.drawable.profile)
                .centerCrop()
                .resize(100,80)
                .into(target);
        getUser();
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        return;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
