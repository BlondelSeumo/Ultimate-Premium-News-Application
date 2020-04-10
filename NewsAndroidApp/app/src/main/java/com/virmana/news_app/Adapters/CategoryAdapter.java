package com.virmana.news_app.Adapters;

/**
 * Created by Tamim on 08/10/2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.virmana.news_app.R;
import com.virmana.news_app.model.Category;
import com.virmana.news_app.ui.Activities.AllCategoryActivity;
import com.virmana.news_app.ui.Activities.CategoryActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by Tamim on 28/09/2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Category> categoryList =new ArrayList<>();
    private Activity activity;

    public CategoryAdapter(List<Category> categoryList, Activity activity) {
        this.categoryList = categoryList;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case 1: {
                View v1 = inflater.inflate(R.layout.item_category_mini, null);
                viewHolder = new CategoryHolder(v1);
                break;
            }
            case 2: {
                View v2 = inflater.inflate(R.layout.item_category_all,null);
                viewHolder = new AllHolder(v2);
                break;
            }
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {
        switch (getItemViewType(position)) {

            case 1: {
                CategoryHolder categoryHolder = (CategoryHolder) holder;
                Typeface face = Typeface.createFromAsset(activity.getAssets(), "Pattaya-Regular.ttf");
                categoryHolder.text_view_item_category_status.setTypeface(face);
                categoryHolder.text_view_item_category_status.setText(categoryList.get(position).getTitle());
                Picasso.with(activity.getApplicationContext()).load(categoryList.get(position).getImage()).into(categoryHolder.image_view_item_category_status);
                GradientDrawable drawable = (GradientDrawable)  ((CategoryHolder) holder).relative_layout_category_status.getBackground();
                int index = 0;
                for (int i = 0; i < position; i++) {
                    index ++;
                    if (index==5){
                        index = 0;
                    }
                }
                String[] colorsTxt = activity.getResources().getStringArray(R.array.colors);

                drawable.setColor(Color.parseColor(colorsTxt[index]));

                categoryHolder.linear_layout_item_category_click.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent  =  new Intent(activity.getApplicationContext(), CategoryActivity.class);
                        intent.putExtra("id",categoryList.get(position).getId());
                        intent.putExtra("title",categoryList.get(position).getTitle());
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                    }
                });

            break;
        }case 2: {
                AllHolder allHolder = (AllHolder) holder;
                Typeface face = Typeface.createFromAsset(activity.getAssets(), "Pattaya-Regular.ttf");
                allHolder.text_view_item_category_status.setTypeface(face);
                GradientDrawable drawable =(GradientDrawable) allHolder.relative_layout_show_all_categories_all.getBackground();
                drawable.setColor(Color.parseColor("#00aa88"));

                allHolder.relative_layout_show_all_categories_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(activity.getApplicationContext(), AllCategoryActivity.class);
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                    }
                });
            }
            break;
        }
    }

    public static class CategoryHolder extends RecyclerView.ViewHolder {
        private final LinearLayout linear_layout_item_category_click;
        private  RelativeLayout relative_layout_category_status;
        private ImageView image_view_item_category_status;
        private TextView text_view_item_category_status;

        public CategoryHolder(View view) {
            super(view);
            this.linear_layout_item_category_click = (LinearLayout) itemView.findViewById(R.id.linear_layout_item_category_click);
            this.relative_layout_category_status = (RelativeLayout) itemView.findViewById(R.id.relative_layout_category_status);
            this.text_view_item_category_status = (TextView) itemView.findViewById(R.id.text_view_item_category_status);
            this.image_view_item_category_status = (ImageView) itemView.findViewById(R.id.image_view_item_category_status);
        }
    }
    @Override
    public int getItemCount() {
        return categoryList.size();
    }
    private class AllHolder extends RecyclerView.ViewHolder {
        private final RelativeLayout relative_layout_show_all_categories_all;
        private final TextView text_view_item_category_status;

        public AllHolder(View v2) {
            super(v2);
            this.relative_layout_show_all_categories_all=(RelativeLayout) v2.findViewById(R.id.relative_layout_show_all_categories_all);
            this.text_view_item_category_status=(TextView) v2.findViewById(R.id.text_view_item_category_status);
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (categoryList.get(position)==null){
            return 2;
        }else{
            return 1;
        }
    }
}
