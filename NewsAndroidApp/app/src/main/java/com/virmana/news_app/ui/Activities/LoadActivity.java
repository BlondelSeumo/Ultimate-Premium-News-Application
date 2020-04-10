package com.virmana.news_app.ui.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.virmana.news_app.R;
import com.virmana.news_app.api.apiClient;
import com.virmana.news_app.api.apiRest;
import com.virmana.news_app.model.Post;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoadActivity extends AppCompatActivity {

    private  Integer id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        Uri data = this.getIntent().getData();
        if (data==null){
            Bundle bundle = getIntent().getExtras() ;
            this.id =  bundle.getInt("id");

        }else{
            this.id=Integer.parseInt(data.getPath().replace("/share/","").replace(".html",""));
        }
        getGuide();
    }
    public void getGuide(){

        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<Post> call = service.postById(id);
        call.enqueue(new retrofit2.Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(response.isSuccessful()) {
                    Intent intent = new Intent(LoadActivity.this, PostActivity.class);

                    if (response.body().getType().equals("post"))
                        intent  =  new Intent(getApplicationContext(), PostActivity.class);
                    else if (response.body().getType().equals("video"))
                        intent  =  new Intent(getApplicationContext(), VideoActivity.class);
                    else if (response.body().getType().equals("youtube"))
                        intent  =  new Intent(getApplicationContext(), YoutubeActivity.class);


                    intent.putExtra("id",response.body().getId());
                    intent.putExtra("title",response.body().getTitle());
                    intent.putExtra("created",response.body().getCreated());
                    intent.putExtra("content",response.body().getContent());
                    intent.putExtra("thumbnail",response.body().getThumbnail());
                    intent.putExtra("original",response.body().getOriginal());
                    intent.putExtra("comment",response.body().getComment());
                    intent.putExtra("comments",response.body().getComments());
                    intent.putExtra("review",response.body().getReview());
                    intent.putExtra("shares",response.body().getShares());
                    intent.putExtra("trusted",response.body().getTrusted());
                    intent.putExtra("user",response.body().getUser());
                    intent.putExtra("userid",response.body().getUserid());
                    intent.putExtra("userimage",response.body().getUserimage());
                    intent.putExtra("type",response.body().getType());
                    intent.putExtra("video",response.body().getVideo());
                    intent.putExtra("from","yes");



                    startActivity(intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    finish();
                }
            }
            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }
}
