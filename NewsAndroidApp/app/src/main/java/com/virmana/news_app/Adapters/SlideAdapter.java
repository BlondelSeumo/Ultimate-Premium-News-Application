package com.virmana.news_app.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.virmana.news_app.R;
import com.virmana.news_app.model.Slide;
import com.virmana.news_app.ui.Activities.CategoryActivity;
import com.virmana.news_app.ui.Activities.PostActivity;
import com.virmana.news_app.ui.Activities.VideoActivity;
import com.virmana.news_app.ui.Activities.YoutubeActivity;


import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hsn on 28/11/2017.
 */

public class SlideAdapter extends PagerAdapter {
    private List<Slide> slideList =new ArrayList<Slide>();
    private Activity activity;

    public SlideAdapter(Activity activity, List<Slide> stringList) {
        this.slideList = stringList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return slideList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater layoutInflater = (LayoutInflater)this.activity.getSystemService(this.activity.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.item_slide_one, container, false);

        TextView text_view_item_slide_one_title =  (TextView)  view.findViewById(R.id.text_view_item_slide_one_title);
        ImageView image_view_item_slide_one =  (ImageView)  view.findViewById(R.id.image_view_item_slide_one);

        Typeface face = Typeface.createFromAsset(activity.getAssets(), "Pattaya-Regular.ttf");
        text_view_item_slide_one_title.setTypeface(face);

        byte[] data = android.util.Base64.decode(slideList.get(position).getTitle(), android.util.Base64.DEFAULT);
        final String final_text = new String(data, Charset.forName("UTF-8"));

        text_view_item_slide_one_title.setText(final_text);

        CardView card_view_item_slide_one = (CardView) view.findViewById(R.id.card_view_item_slide_one);
        card_view_item_slide_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (slideList.get(position).getType().equals("3") && slideList.get(position).getPost()!=null){

                    Intent intent  =  new Intent(activity.getApplicationContext(), PostActivity.class);

                    if (slideList.get(position).getPost().getType().equals("post"))
                        intent  =  new Intent(activity.getApplicationContext(), PostActivity.class);
                    else if (slideList.get(position).getPost().getType().equals("video"))
                        intent  =  new Intent(activity.getApplicationContext(), VideoActivity.class);
                    else if (slideList.get(position).getPost().getType().equals("youtube"))
                        intent  =  new Intent(activity.getApplicationContext(), YoutubeActivity.class);


                    intent.putExtra("id",slideList.get(position).getPost().getId());
                    intent.putExtra("title",slideList.get(position).getPost().getTitle());
                    intent.putExtra("created",slideList.get(position).getPost().getCreated());
                    intent.putExtra("content",slideList.get(position).getPost().getContent());
                    intent.putExtra("thumbnail",slideList.get(position).getPost().getThumbnail());
                    intent.putExtra("original",slideList.get(position).getPost().getOriginal());
                    intent.putExtra("comment",slideList.get(position).getPost().getComment());
                    intent.putExtra("comments",slideList.get(position).getPost().getComments());
                    intent.putExtra("review",slideList.get(position).getPost().getReview());
                    intent.putExtra("shares",slideList.get(position).getPost().getShares());
                    intent.putExtra("trusted",slideList.get(position).getPost().getTrusted());
                    intent.putExtra("user",slideList.get(position).getPost().getUser());
                    intent.putExtra("userid",slideList.get(position).getPost().getUserid());
                    intent.putExtra("userimage",slideList.get(position).getPost().getUserimage());
                    intent.putExtra("type",slideList.get(position).getPost().getType());
                    intent.putExtra("video",slideList.get(position).getPost().getVideo());


                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.enter, R.anim.exit);

                }
                if (slideList.get(position).getType().equals("1") && slideList.get(position).getCategory()!=null){
                    Intent intent  =  new Intent(activity.getApplicationContext(), CategoryActivity.class);
                    intent.putExtra("id",slideList.get(position).getCategory().getId());
                    intent.putExtra("title",slideList.get(position).getCategory().getTitle());
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                }
                if (slideList.get(position).getType().equals("2") && slideList.get(position).getUrl()!=null){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(slideList.get(position).getUrl()));
                    activity.startActivity(browserIntent);
                }
            }
        });

         Picasso.with(activity).load(slideList.get(position).getImage()).placeholder(R.drawable.placeholder).into(image_view_item_slide_one);

        container.addView(view);
        return view;
    }
    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public float getPageWidth (int position) {
        return 1f;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);

    }
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);

    }
    @Override
    public Parcelable saveState() {
        return null;
    }
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
