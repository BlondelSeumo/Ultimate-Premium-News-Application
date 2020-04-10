package com.virmana.news_app.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.virmana.news_app.R;
import com.virmana.news_app.model.Language;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tamim on 29/06/2018.
 */



public class LanguageSelectAdapter extends RecyclerView.Adapter implements SelectableLanguageViewHolder.OnItemSelectedListener {

    private final List<Language> mValues;
    private boolean isMultiSelectionEnabled = false;
    SelectableLanguageViewHolder.OnItemSelectedListener listener;
    Activity activity;
    public LanguageSelectAdapter( SelectableLanguageViewHolder.OnItemSelectedListener listener,
                               List<Language> items,boolean isMultiSelectionEnabled,Activity activity) {
        this.listener = listener;
        this.isMultiSelectionEnabled = isMultiSelectionEnabled;
        this.activity=activity;
        mValues = new ArrayList<>();
        for (Language item : items) {
            mValues.add(item);
        }
    }

    @Override
    public SelectableLanguageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_select, parent, false);

        return new SelectableLanguageViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        SelectableLanguageViewHolder holder = (SelectableLanguageViewHolder) viewHolder;
        Language selectableItem = mValues.get(position);
        String name = selectableItem.getLanguage();
        holder.text_view_item_category_item_select.setText(name);
        //holder.card_view_category_item_select.setCardBackgroundColor(android.graphics.Color.parseColor(mValues.get(position).getCode()));

        //Picasso.with(activity.getApplicationContext()).load(selectableItem.getImage()).error(R.drawable.flag_placeholder).placeholder(R.drawable.flag_placeholder).into(holder.image_view_iten_language);

        if (isMultiSelectionEnabled) {
            TypedValue value = new TypedValue();
            holder.textView.getContext().getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorMultiple, value, true);
            int checkMarkDrawableResId = value.resourceId;
            holder.textView.setCheckMarkDrawable(checkMarkDrawableResId);
        } else {
            TypedValue value = new TypedValue();
            holder.textView.getContext().getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorSingle, value, true);
            int checkMarkDrawableResId = value.resourceId;
            holder.textView.setCheckMarkDrawable(checkMarkDrawableResId);
        }

        holder.mItem = selectableItem;
        holder.setChecked(holder.mItem.isSelected());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public List<Language> getSelectedItems() {

        List<Language> selectedItems = new ArrayList<>();
        for (Language item : mValues) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    @Override
    public int getItemViewType(int position) {
        if(isMultiSelectionEnabled){
            return SelectableLanguageViewHolder.MULTI_SELECTION;
        }
        else{
            return SelectableLanguageViewHolder.SINGLE_SELECTION;
        }
    }

    @Override
    public void onItemSelected(Language item) {

        if (!isMultiSelectionEnabled) {
            for (Language selectableItem : mValues) {
                if (!selectableItem.equals(item)
                        && selectableItem.isSelected()) {
                    selectableItem.setSelected(false);
                } else if (selectableItem.equals(item)
                        && item.isSelected()) {
                    selectableItem.setSelected(true);
                }
            }
            notifyDataSetChanged();
        }
        listener.onItemSelected(item);

    }
}
