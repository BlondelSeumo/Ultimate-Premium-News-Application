package com.virmana.news_app.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.virmana.news_app.R;
import com.virmana.news_app.model.Choice;

/**
 * Created by hsn on 27/12/2017.
 */

public class SelectableVoteViewHolder extends RecyclerView.ViewHolder {

    public static final int MULTI_SELECTION = 2;
    public static final int SINGLE_SELECTION = 1;
    public final RelativeLayout relative_layout_item_question_choice_global;

    CheckedTextView checked_text_view_item_vote_choice;
    TextView text_view_item_vote_choice;
    ImageView image_view_item_vote_choice;
    OnItemSelectedListener itemSelectedListener;

    Choice mItem;

    public SelectableVoteViewHolder(View view, OnItemSelectedListener listener) {
        super(view);
        itemSelectedListener = listener;

        checked_text_view_item_vote_choice = (CheckedTextView) view.findViewById(R.id.checked_text_view_item_vote_choice);
        text_view_item_vote_choice = (TextView) view.findViewById(R.id.text_view_item_vote_choice);
        image_view_item_vote_choice = (ImageView) view.findViewById(R.id.image_view_item_vote_choice);
        relative_layout_item_question_choice_global = (RelativeLayout) view.findViewById(R.id.relative_layout_item_question_choice_global);
         relative_layout_item_question_choice_global.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItem.isSelected() && getItemViewType() == MULTI_SELECTION) {
                    setChecked(false);
                    image_view_item_vote_choice.setVisibility(View.GONE);

                } else {
                    setChecked(true);
                    image_view_item_vote_choice.setVisibility(View.VISIBLE);
                }
                itemSelectedListener.onItemSelected(mItem);
            }
        });
    }

    public void setChecked(boolean value) {
        if (value) {
            //card_view_category_item_select.setCardBackgroundColor(Color.parseColor("#FF007E71"));
        } else {
            // card_view_category_item_select.setCardBackgroundColor(Color.parseColor("#a7164f"));
        }
        mItem.setSelected(value);
        checked_text_view_item_vote_choice.setChecked(value);
    }

    public interface OnItemSelectedListener {

        void onItemSelected(Choice item);
    }

}