package com.virmana.news_app.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.virmana.news_app.R;
import com.virmana.news_app.model.Choice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hsn on 27/12/2017.
 */


public class ChoiceSelectAdapter extends RecyclerView.Adapter implements SelectableVoteViewHolder.OnItemSelectedListener {

    private final List<Choice> mValues;
    private boolean isMultiSelectionEnabled = false;
    SelectableVoteViewHolder.OnItemSelectedListener listener;
    Activity activity;
    public ChoiceSelectAdapter(List<Choice> items, final boolean isMultiSelectionEnabled, final Activity activity) {
        this.isMultiSelectionEnabled = isMultiSelectionEnabled;
        this.activity=activity;
        mValues = new ArrayList<>();
        for (Choice item : items) {
            mValues.add(item);
        }
        listener= this;
        listener = new SelectableVoteViewHolder.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Choice item) {
                if (!isMultiSelectionEnabled) {
                    for (Choice selectableItem : mValues) {
                        if (!selectableItem.equals(item) && selectableItem.isSelected()) {
                            selectableItem.setSelected(false);
                        } else if (selectableItem.equals(item) && item.isSelected()) {
                            selectableItem.setSelected(true);
                        }
                    }
                    notifyDataSetChanged();
                }

              //  Toast.makeText(activity, "here", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public SelectableVoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question_choice, parent, false);

        return new SelectableVoteViewHolder(itemView,listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        SelectableVoteViewHolder holder = (SelectableVoteViewHolder) viewHolder;
        Choice selectableItem = mValues.get(position);
        String name = selectableItem.getChoice();
        holder.text_view_item_vote_choice.setText(name);
        int step=1;
        int final_step=1;
        for (int i = 1; i < position+1; i++) {
            if (i==position+1){
                final_step=step;
            }
            step++;
            if (step>5){
                step=1;
            }
        }
        switch (step){
            case 1:
                holder.relative_layout_item_question_choice_global.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.bg_choice_1));

                break;
            case 2:
                holder.relative_layout_item_question_choice_global.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.bg_choice_2));
                break;
            case 3:
                holder.relative_layout_item_question_choice_global.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.bg_choice_3));
                break;
            case 4:
                holder.relative_layout_item_question_choice_global.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.bg_choice_4));
                break;
            case 5:
                holder.relative_layout_item_question_choice_global.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.bg_choice_5));
                break;
        }

        //Picasso.with(activity.getApplicationContext()).load(selectableItem.getImage()).error(R.drawable.flag_placeholder).placeholder(R.drawable.flag_placeholder).into(holder.image_view_iten_language);

        if (isMultiSelectionEnabled) {
            TypedValue value = new TypedValue();
            holder.checked_text_view_item_vote_choice.getContext().getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorMultiple, value, true);
            int checkMarkDrawableResId = value.resourceId;
            holder.checked_text_view_item_vote_choice.setCheckMarkDrawable(checkMarkDrawableResId);
        } else {
            TypedValue value = new TypedValue();
            holder.checked_text_view_item_vote_choice.getContext().getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorSingle, value, true);
            int checkMarkDrawableResId = value.resourceId;
            holder.checked_text_view_item_vote_choice.setCheckMarkDrawable(checkMarkDrawableResId);
        }

        holder.mItem = selectableItem;
        holder.setChecked(holder.mItem.isSelected());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public List<Choice> getSelectedItems() {

        List<Choice> selectedItems = new ArrayList<>();
        for (Choice item : mValues) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    @Override
    public int getItemViewType(int position) {
        if(isMultiSelectionEnabled){
            return SelectableVoteViewHolder.MULTI_SELECTION;
        }
        else{
            return SelectableVoteViewHolder.SINGLE_SELECTION;
        }
    }

    @Override
    public void onItemSelected(Choice item) {

    }



}
