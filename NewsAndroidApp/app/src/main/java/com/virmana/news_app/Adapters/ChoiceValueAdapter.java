package com.virmana.news_app.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.virmana.news_app.R;
import com.virmana.news_app.model.Choice;

import java.util.List;

/**
 * Created by hsn on 01/01/2018.
 */

public class ChoiceValueAdapter extends  RecyclerView.Adapter<ChoiceValueAdapter.ChoiceHolder>{
    private List<Choice> choiceList;
    private Activity activity;
    public ChoiceValueAdapter(List<Choice> choiceList, Activity activity) {
        this.choiceList = choiceList;
        this.activity = activity;

    }

    @Override
    public ChoiceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_value, null);
        ChoiceHolder mh = new ChoiceHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ChoiceHolder holder, final int position) {
        Choice selectableItem = choiceList.get(position);
        String name = selectableItem.getChoice();
        int  somme=0;
        for (int i = 0; i < choiceList.size(); i++) {
            somme+=choiceList.get(i).getValue();
        }

        holder.text_view_item_question_value_choice_title.setText(name+" ("+choiceList.get(position).getValue()+")");
        int pourcent=0;
        if (somme!=0){
             pourcent= (choiceList.get(position).getValue()*100)/somme;
        }


        holder.text_view_item_question_value_progress.setText(pourcent+"%");
        holder.progress_bar_item_question_choice_value.setProgress(pourcent);
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
                holder.progress_bar_item_question_choice_value.setProgressDrawable(activity.getResources().getDrawable(R.drawable.bg_progress_1));

                break;
            case 2:
                holder.progress_bar_item_question_choice_value.setProgressDrawable(activity.getResources().getDrawable(R.drawable.bg_progress_2));
                break;
            case 3:
                holder.progress_bar_item_question_choice_value.setProgressDrawable(activity.getResources().getDrawable(R.drawable.bg_progress_3));
                break;
            case 4:
                holder.progress_bar_item_question_choice_value.setProgressDrawable(activity.getResources().getDrawable(R.drawable.bg_progress_4));
                break;
            case 5:
                holder.progress_bar_item_question_choice_value.setProgressDrawable(activity.getResources().getDrawable(R.drawable.bg_progress_5));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return choiceList.size();
    }

    public class ChoiceHolder extends RecyclerView.ViewHolder {
        public final ProgressBar progress_bar_item_question_choice_value;
        public final TextView text_view_item_question_value_choice_title;
        public final TextView text_view_item_question_value_progress;
        public final RelativeLayout relative_layout_item_question_choice_value;
        public ChoiceHolder(View view) {
            super(view);
            relative_layout_item_question_choice_value = (RelativeLayout) view.findViewById(R.id.relative_layout_item_question_choice_value);
            progress_bar_item_question_choice_value=(ProgressBar) view.findViewById(R.id.progress_bar_item_question_choice_value);
            text_view_item_question_value_choice_title=(TextView) view.findViewById(R.id.text_view_item_question_value_choice_title);
            text_view_item_question_value_progress=(TextView) view.findViewById(R.id.text_view_item_question_value_progress);

        }
    }
}


