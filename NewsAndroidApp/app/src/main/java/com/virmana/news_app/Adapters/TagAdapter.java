package com.virmana.news_app.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.virmana.news_app.R;
import com.virmana.news_app.model.Keyword;
import com.virmana.news_app.ui.Activities.SearchActivity;


import java.util.List;

/**
 * Created by hsn on 05/04/2018.
 */

public class TagAdapter extends  RecyclerView.Adapter<TagAdapter.TagHolder>{
    private List<Keyword> keywordList;
    private Activity activity;
    private Boolean tags = false;
    public TagAdapter(List<Keyword> keywordList, Activity activity) {
        this.keywordList = keywordList;
        this.activity = activity;
    }

    public TagAdapter(List<Keyword> keywordList, Activity activity, Boolean tags) {
        this.keywordList = keywordList;
        this.activity = activity;
        this.tags = tags;
    }

    @Override
    public TagHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, null);
        TagHolder mh = new TagHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(TagHolder holder,final int position) {
        holder.text_view_item_tag_item.setText(keywordList.get(position).getName());
        if (!tags) {
            String[] colorsTxt = activity.getResources().getStringArray(R.array.colors);

            int step = 1;
            int final_step = 1;
            for (int i = 1; i < position + 1; i++) {
                if (i == position + 1) {
                    final_step = step;
                }
                step++;
                if (step > 5) {
                    step = 1;
                }
            }

            holder.text_view_item_tag_item.setTextColor(Color.parseColor(colorsTxt[step-1]));
            holder.recycle_view_item_tag_indicator.setCardBackgroundColor(Color.parseColor(colorsTxt[step-1]));
            Typeface face = Typeface.createFromAsset(activity.getAssets(), "Pattaya-Regular.ttf");
            holder.text_view_item_tag_item.setTypeface(face);

            holder.card_view_tag_item_global.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        Intent intent_video  =  new Intent(activity, SearchActivity.class);
                        intent_video.putExtra("query",keywordList.get(position).getName());
                        activity.startActivity(intent_video);
                    }catch (IndexOutOfBoundsException e){

                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return keywordList.size();
    }

    public class TagHolder extends RecyclerView.ViewHolder {
        public TextView text_view_item_tag_item ;
        public CardView card_view_tag_item_global ;
        public CardView recycle_view_item_tag_indicator;

        public TagHolder(View itemView) {
            super(itemView);
            this.recycle_view_item_tag_indicator=(CardView) itemView.findViewById(R.id.recycle_view_item_tag_indicator);
            this.card_view_tag_item_global=(CardView) itemView.findViewById(R.id.card_view_tag_item_global);
            this.text_view_item_tag_item=(TextView) itemView.findViewById(R.id.text_view_item_tag_item);
        }
    }
}
