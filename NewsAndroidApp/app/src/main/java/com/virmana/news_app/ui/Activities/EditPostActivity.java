package com.virmana.news_app.ui.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.squareup.picasso.Picasso;
import com.virmana.news_app.Adapters.CategorySelectAdapter;
import com.virmana.news_app.Adapters.SelectableCategoryViewHolder;
import com.virmana.news_app.Adapters.SelectableLanguageViewHolder;
import com.virmana.news_app.Provider.PrefManager;
import com.virmana.news_app.R;
import com.virmana.news_app.api.ProgressRequestBody;
import com.virmana.news_app.api.apiClient;
import com.virmana.news_app.api.apiRest;
import com.virmana.news_app.model.ApiResponse;
import com.virmana.news_app.model.Category;
import com.virmana.news_app.model.Language;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import jp.wasabeef.richeditor.RichEditor;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditPostActivity extends AppCompatActivity implements ProgressRequestBody.UploadCallbacks,SelectableCategoryViewHolder.OnItemSelectedListener ,SelectableLanguageViewHolder.OnItemSelectedListener {

    private EditText edit_text_upload_title;
    private LinearLayout linear_layout_categories;
    private RichEditor edit_text_upload_description;
    private ImageView image_view_post_activity_save;
    private RelativeLayout relative_layout_upload;
    private LinearLayoutManager gridLayoutManagerCategorySelect;
    private LinearLayoutManager gridLayoutManagerLanguageSelect;
    private RecyclerView recycle_view_selected_category;
    private RecyclerView recycle_view_selected_language;
    private ImageView image_view_post_activity_select_image;
    private ImageView image_view_done_rich;

    private int PICK_IMAGE = 1002;
    private ProgressDialog pd;
    private String   imageUrl;

    private ArrayList<Category> categoriesListObj = new ArrayList<Category>();
    private CategorySelectAdapter categorySelectAdapter;
    private ProgressDialog register_progress;

    private List<Language> languageList = new ArrayList<Language>();
    private ImageView image_view_add_post_preview;
    private Spinner spinner_language_select;

    private int id;
    private String title;
    private String image;
    private String description;
    private ImageView image_view_edit;
    private RelativeLayout relativeLayout_rich_box;
    private WebView web_view_post_activity_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        Bundle bundle = getIntent().getExtras() ;
        this.id =  bundle.getInt("id");
        this.title =  bundle.getString("title");
        this.image =  bundle.getString("image");
        this.description =  bundle.getString("description");

        setContentView(R.layout.activity_edit_post);
        initView();
        initAction();
        setPost();
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.edit_post));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void setPost() {
        edit_text_upload_title.setText(title);
        edit_text_upload_description.setHtml(description);
        String content_article ="<style type=\"text/css\">img{background-color:#ccc !important;min-height:100px !important;max-width:100%  !important;border-radius: 5px !important;  box-shadow: 0px 5px 5px 1px  #ccc !important;margin-bottom:10px !important;margin-top:10px !important}</style>"+ description.replace("<img","<img onClick=\"showAndroidToast(this.src)\" ")+" <script type=\"text/javascript\"> function showAndroidToast(toast) {Android.showToast(toast);}</script>";
        web_view_post_activity_content.loadData(content_article, "text/html; charset=utf-8", "utf-8");
        Picasso.with(this).load(image).placeholder(getResources().getDrawable(R.drawable.placeholder)).placeholder(getResources().getDrawable(R.drawable.error_placeholder)).into(image_view_add_post_preview);

    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
        return;
    }
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int iditem = item.getItemId();

        switch (iditem){
            case android.R.id.home:
                super.onBackPressed();
                overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void SelectImage() {
        if (ContextCompat.checkSelfPermission(EditPostActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EditPostActivity.this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }else{
            Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            i.setType("image/jpeg");
            startActivityForResult(i, PICK_IMAGE);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        checkPermission();
    }

    public void checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(EditPostActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)  != PackageManager.PERMISSION_GRANTED) {


                if (ActivityCompat.shouldShowRequestPermissionRationale(EditPostActivity.this,   Manifest.permission.READ_CONTACTS)) {
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
            }

        }
    }
    private void initView() {
        this.relativeLayout_rich_box=(RelativeLayout) findViewById(R.id.relativeLayout_rich_box);
        this.web_view_post_activity_content=(WebView) findViewById(R.id.web_view_post_activity_content);
        this.linear_layout_categories=(LinearLayout) findViewById(R.id.linear_layout_categories);
        this.image_view_done_rich=(ImageView) findViewById(R.id.image_view_done_rich);

        pd = new ProgressDialog(EditPostActivity.this);
        pd.setMessage("Uploading Post");
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setCancelable(false);
        this.image_view_edit =  (ImageView) findViewById(R.id.image_view_edit);
        this.edit_text_upload_description =  (RichEditor) findViewById(R.id.editor);
        this.image_view_post_activity_save =  (ImageView) findViewById(R.id.image_view_post_activity_save);
        this.image_view_add_post_preview =  (ImageView) findViewById(R.id.image_view_add_post_preview);
        this.image_view_post_activity_select_image =  (ImageView) findViewById(R.id.image_view_post_activity_select_image);
        this.edit_text_upload_title=(EditText) findViewById(R.id.edit_text_upload_title);
        this.relative_layout_upload=(RelativeLayout) findViewById(R.id.relative_layout_upload);

        PrefManager prf= new PrefManager(getApplicationContext());


        final RichEditor mEditor = (RichEditor) findViewById(R.id.editor);

        //mEditor.setEditorBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundResource(R.drawable.bg);
        mEditor.setPadding(10, 10, 10, 10);
        //mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        mEditor.setPlaceholder("Insert text here...");
        //mEditor.setInputEnabled(false);


        findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.undo();
            }
        });

        findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.redo();
            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setBold();
            }
        });

        findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setItalic();
            }
        });

        findViewById(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setSubscript();
            }
        });

        findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setSuperscript();
            }
        });

        findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setStrikeThrough();
            }
        });

        findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setUnderline();
            }
        });

        findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(1);
            }
        });

        findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(2);
            }
        });

        findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(3);
            }
        });

        findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(4);
            }
        });

        findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(5);
            }
        });

        findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(6);
            }
        });

        findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override public void onClick(View v) {
                ColorPickerDialogBuilder
                        .with(EditPostActivity.this)
                        .setTitle("Choose color")
                        .initialColor(Color.BLACK)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                mEditor.setTextColor(Color.parseColor("#"+Integer.toHexString(selectedColor)));
                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                mEditor.setTextColor(Color.parseColor("#"+Integer.toHexString(selectedColor)));
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();
            }
        });

        findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                ColorPickerDialogBuilder
                        .with(EditPostActivity.this)
                        .setTitle("Choose color")
                        .initialColor(Color.BLACK)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                mEditor.setTextBackgroundColor(Color.parseColor("#"+Integer.toHexString(selectedColor)));
                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                mEditor.setTextBackgroundColor(Color.parseColor("#"+Integer.toHexString(selectedColor)));
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();


            }
        });

        findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setIndent();
            }
        });

        findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setOutdent();
            }
        });

        findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setAlignLeft();
            }
        });

        findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setAlignCenter();
            }
        });

        findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setAlignRight();
            }
        });

        findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setBlockquote();
            }
        });

        findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setBullets();
            }
        });

        findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setNumbers();
            }
        });

        findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EditPostActivity.this);
                builder.setTitle("Insert Image");
                final EditText input = new EditText(EditPostActivity.this);
                input.setHint("Image Link");
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString();
                        mEditor.insertImage(m_Text,
                                m_Text);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EditPostActivity.this);
                //you should edit this to fit your needs
                builder.setTitle("Insert Link");

                final EditText one = new EditText(EditPostActivity.this);
                final EditText two = new EditText(EditPostActivity.this);

                one.setHint("Text");
                two.setHint("URL");

                LinearLayout lay = new LinearLayout(EditPostActivity.this);
                lay.setOrientation(LinearLayout.VERTICAL);
                lay.addView(one);
                lay.addView(two);
                builder.setView(lay);

                // Set up the buttons
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //get the two inputs
                        String i = one.getText().toString();
                        String j = two.getText().toString();
                        mEditor.insertLink(j,i);

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
                builder.show();


            }
        });


        gridLayoutManagerCategorySelect = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);

        recycle_view_selected_category= (RecyclerView) findViewById(R.id.recycle_view_selected_category);

        spinner_language_select = (Spinner) findViewById(R.id.spinner_language_select);


        register_progress   = ProgressDialog.show(this, null,getResources().getString(R.string.operation_progress), true);
        getLanguages();
    }
    private void initAction() {
        image_view_done_rich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                description = edit_text_upload_description.getHtml();
                relativeLayout_rich_box.setVisibility(View.GONE);
                web_view_post_activity_content.loadData(description,"text/html; charset=utf-8", "utf-8");
                web_view_post_activity_content.setVisibility(View.VISIBLE);
            }
        });
        image_view_post_activity_select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });
        image_view_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout_rich_box.setVisibility(View.VISIBLE);
                edit_text_upload_description.setHtml(description);
                String content_article ="<style type=\"text/css\">img{background-color:#ccc !important;min-height:100px !important;max-width:100%  !important;border-radius: 5px !important;  box-shadow: 0px 5px 5px 1px  #ccc !important;margin-bottom:10px !important;margin-top:10px !important}</style>"+ description.replace("<img","<img onClick=\"showAndroidToast(this.src)\" ")+" <script type=\"text/javascript\"> function showAndroidToast(toast) {Android.showToast(toast);}</script>";
                web_view_post_activity_content.loadData(content_article, "text/html; charset=utf-8", "utf-8");            }
        });
        image_view_post_activity_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        spinner_language_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getCategory();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    public boolean save(){
        if (edit_text_upload_title.getText().toString().trim().length()<3){
            Toasty.error(EditPostActivity.this, getResources().getString(R.string.edit_text_upload_title_error), Toast.LENGTH_SHORT).show();
            return true;
        }
        if (edit_text_upload_description.getHtml() != null){
            if (edit_text_upload_description.getHtml().toString().trim().length()<10){
                Toasty.error(EditPostActivity.this, getResources().getString(R.string.edit_text_upload_description_error), Toast.LENGTH_SHORT).show();
                return true;
            }
        }else{
            Toasty.error(EditPostActivity.this, getResources().getString(R.string.edit_text_upload_description_error), Toast.LENGTH_SHORT).show();
            return true;
        }


        upload();
        return true;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK
                && null != data) {


            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Video.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();


            imageUrl = picturePath  ;
            File file = new File(imageUrl);
            Log.v("SIZE",file.getName()+"");
            image_view_add_post_preview.setImageURI(selectedImage);

        } else {

            Log.i("SonaSys", "resultCode: " + resultCode);
            switch (resultCode) {
                case 0:
                    Log.i("SonaSys", "User cancelled");
                    break;
                case -1:
                    break;
            }
        }
    }
    public String getSelectedCategories(){
        String categories = "";
        for (int i = 0; i < categorySelectAdapter.getSelectedItems().size(); i++) {
            categories+="_"+categorySelectAdapter.getSelectedItems().get(i).getId();
        }
        Log.v("categories",categories);

        return categories;
    }

    private void getCategory() {
        register_progress.show();

        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        PrefManager prefManager= new PrefManager(getApplicationContext());

        String language=languageList.get(spinner_language_select.getSelectedItemPosition()).getId().toString();
        Call<List<Category>> call = service.allCategoriesByLanguage(language);
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                register_progress.dismiss();
                if(response.isSuccessful()){
                    categoriesListObj.clear();
                    for (int i = 0;i<response.body().size();i++){

                        categoriesListObj.add(response.body().get(i));
                    }
                    categorySelectAdapter = new CategorySelectAdapter(EditPostActivity.this, categoriesListObj, true, EditPostActivity.this);
                    recycle_view_selected_category.setHasFixedSize(true);
                    recycle_view_selected_category.setAdapter(categorySelectAdapter);
                    recycle_view_selected_category.setLayoutManager(gridLayoutManagerCategorySelect);
                    if (response.body().size()>0) {
                        linear_layout_categories.setVisibility(View.VISIBLE);
                    }
                    getCategoriesPost();
                }else {
                    Snackbar snackbar = Snackbar
                            .make(relative_layout_upload, getResources().getString(R.string.no_connexion), Snackbar.LENGTH_INDEFINITE)
                            .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    getCategory();
                                }
                            });
                    snackbar.setActionTextColor(android.graphics.Color.RED);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(android.graphics.Color.YELLOW);
                    snackbar.show();
                }

            }
            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                register_progress.dismiss();
                Snackbar snackbar = Snackbar
                        .make(relative_layout_upload, getResources().getString(R.string.no_connexion), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getCategory();
                            }
                        });
                snackbar.setActionTextColor(android.graphics.Color.RED);
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(android.graphics.Color.YELLOW);
                snackbar.show();
            }
        });
    }

    private void getPostLanguages(){
        register_progress.show();

        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Language>> call = service.languageByPost(id);
        call.enqueue(new Callback<List<Language>>() {
            @Override
            public void onResponse(Call<List<Language>> call, Response<List<Language>> response) {
                if(response.isSuccessful()) {
                    for (int i = 0; i < response.body().size(); i++) {

                        for (int j = 0; j < languageList.size(); j++) {
                            if (response.body().get(i).getId().equals(languageList.get(j).getId())){
                               if (languageList.get(j).getId() == response.body().get(i).getId()){
                                   spinner_language_select.setSelection(j);
                               }
                             }
                        }
                    }
                }
                register_progress.dismiss();

            }
            @Override
            public void onFailure(Call<List<Language>> call, Throwable t) {
                register_progress.dismiss();

            }
        });
    }
    private void getCategoriesPost(){
        register_progress.show();

        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Category>> call = service.CategoryByPost(id);
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if(response.isSuccessful()) {
                    for (int i = 0; i < response.body().size(); i++) {

                        for (int j = 0; j < categoriesListObj.size(); j++) {
                            if (response.body().get(i).getId().equals(categoriesListObj.get(j).getId())){
                                categoriesListObj.get(j).setSelected(true);
                            }
                        }
                    }
                    categorySelectAdapter.notifyDataSetChanged();
                }
                register_progress.dismiss();
            }
            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                register_progress.dismiss();
            }
        });
    }
    private void getLanguages(){
        register_progress.show();

        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Language>> call = service.languageAll();
        call.enqueue(new Callback<List<Language>>() {
            @Override
            public void onResponse(Call<List<Language>> call, Response<List<Language>> response) {
                if(response.isSuccessful()) {
                    languageList.clear();
                    ArrayList<String> contactlist= new ArrayList<String>();

                    for (int i = 0; i < response.body().size(); i++) {
                        languageList.add(response.body().get(i));
                        contactlist.add(response.body().get(i).getLanguage());
                    }
                    ArrayAdapter adapter = new ArrayAdapter(EditPostActivity.this, R.layout.my_spinner_dropdown_item, contactlist);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_language_select.setAdapter(adapter);
                    getPostLanguages();
                }else{
                        Snackbar snackbar = Snackbar
                                .make(relative_layout_upload, getResources().getString(R.string.no_connexion), Snackbar.LENGTH_INDEFINITE)
                                .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        getLanguages();
                                    }
                                });
                        snackbar.setActionTextColor(Color.RED);
                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.YELLOW);
                        snackbar.show();
                }
                register_progress.dismiss();

            }
            @Override
            public void onFailure(Call<List<Language>> call, Throwable t) {
                register_progress.dismiss();
                Snackbar snackbar = Snackbar
                        .make(relative_layout_upload, getResources().getString(R.string.no_connexion), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getLanguages();
                            }
                        });
                snackbar.setActionTextColor(android.graphics.Color.RED);
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(android.graphics.Color.YELLOW);
                snackbar.show();
            }
        });
    }
    @Override
    public void onItemSelected(Language item) {

    }

    @Override
    public void onItemSelected(Category item) {

    }
    @Override
    public void onProgressUpdate(int percentage) {
        pd.setProgress(percentage);
    }

    @Override
    public void onError() {
        pd.dismiss();
        pd.cancel();
    }

    @Override
    public void onFinish() {
        pd.dismiss();
        pd.cancel();

    }
    public void upload(){
        if (imageUrl!=null) {
            File file1 = new File(imageUrl);
            int file_size = Integer.parseInt(String.valueOf(file1.length() / 1024 / 1024));
            if (file_size > 20) {
                Toasty.error(getApplicationContext(), "Max file size allowed 20M", Toast.LENGTH_LONG).show();
            }
            Log.v("SIZE",file1.getName()+"");
        }

        pd.show();
        PrefManager prf = new PrefManager(getApplicationContext());

        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        MultipartBody.Part body = null;
        if (imageUrl!=null) {

            //File creating from selected URL
            final File file = new File(imageUrl);


            ProgressRequestBody requestFile  = new ProgressRequestBody(file, EditPostActivity.this);

           body  =MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);

        }
        // create RequestBody instance from file
        // RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        String id_ser=  prf.getString("ID_USER");
        String key_ser=  prf.getString("TOKEN_USER");

        Call<ApiResponse> request = service.editPost(body,id+"", id_ser, key_ser, edit_text_upload_title.getText().toString().trim(),edit_text_upload_description.getHtml().toString(),languageList.get(spinner_language_select.getSelectedItemPosition()).getId().toString(),getSelectedCategories());

        request.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                if (response.isSuccessful()){
                    Toasty.success(getApplication(),getResources().getString(R.string.post_upload_success),Toast.LENGTH_LONG).show();
                        Integer id=0;
                    String title="";
                    for (int i = 0; i < response.body().getValues().size(); i++) {
                        if (response.body().getValues().get(i).getName().equals("id")){
                            id = Integer.parseInt(response.body().getValues().get(i).getValue());
                        }
                        if (response.body().getValues().get(i).getName().equals("title")){
                            title = response.body().getValues().get(i).getValue();
                        }
                    }

                    Intent intent = new Intent(getApplicationContext(), LoadActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    finish();
                }else{
                    Toasty.error(getApplication(),getResources().getString(R.string.no_connexion),Toast.LENGTH_LONG).show();

                }
                // file.delete();
                // getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                pd.dismiss();
                pd.cancel();
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toasty.error(getApplication(),getResources().getString(R.string.no_connexion),Toast.LENGTH_LONG).show();
                pd.dismiss();
                pd.cancel();
            }
        });
    }
}
