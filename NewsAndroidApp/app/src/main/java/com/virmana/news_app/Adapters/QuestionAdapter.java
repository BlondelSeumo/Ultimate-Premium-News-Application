package com.virmana.news_app.Adapters;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.virmana.news_app.Provider.PrefQuestionManager;
import com.virmana.news_app.R;
import com.virmana.news_app.api.apiClient;
import com.virmana.news_app.api.apiRest;
import com.virmana.news_app.model.ApiResponse;
import com.virmana.news_app.model.Question;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by hsn on 31/12/2017.
 */

public class QuestionAdapter extends  RecyclerView.Adapter<QuestionAdapter.QuestionHolder>{
    private List<Question> questionList;
    private Activity activity;
    public QuestionAdapter(List<Question> questionList, Activity activity) {
        this.questionList = questionList;
        this.activity = activity;

    }

    @Override
    public QuestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, null);
        QuestionHolder mh = new QuestionHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(QuestionHolder holder,final int position) {

        LinearLayoutManager linearLayoutManagerVoteSelect = new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false);


        final PrefQuestionManager prefQuestionManager= new PrefQuestionManager(activity.getApplicationContext());
        List<Integer> votedlist = prefQuestionManager.loadVoted();
        Boolean exist = false;
        if (votedlist==null){
            votedlist= new ArrayList<>();
        }
        for (int i = 0; i <votedlist.size() ; i++) {
            if (votedlist.get(i)==questionList.get(position).getId()){
                exist = true;
            }
            Log.v("V: ",""+votedlist);
        }


        final ChoiceSelectAdapter choiceSelectAdapter =new ChoiceSelectAdapter(questionList.get(position).getChoices(),questionList.get(position).getMultichoice(),activity);
        if (questionList.get(position).getFeatured()){
            holder.image_view_item_question_featured.setVisibility(View.VISIBLE);
        }else{
            holder.image_view_item_question_featured.setVisibility(View.GONE);
        }
        holder.text_view_item_question_number.setText((position+1)+"");
        holder.text_view_item_question.setText(questionList.get(position).getQuestion());
        holder.recycler_view_item_question_choices.setHasFixedSize(true);
        holder.recycler_view_item_question_choices.setAdapter(choiceSelectAdapter);
        holder.recycler_view_item_question_choices.setLayoutManager(linearLayoutManagerVoteSelect);
        holder.text_view_item_question_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (choiceSelectAdapter.getSelectedItems().size()>0) {
                   String choices = "";
                   for (int i = 0; i < choiceSelectAdapter.getSelectedItems().size(); i++) {
                       Log.v("Id", ": " + choiceSelectAdapter.getSelectedItems().get(i).getId());
                       if (i == 0) {
                           choices += choiceSelectAdapter.getSelectedItems().get(i).getId();
                       } else {
                           choices += "_" + choiceSelectAdapter.getSelectedItems().get(i).getId();
                       }
                       for (int j = 0; j < questionList.get(position).getChoices().size(); j++) {
                           if (questionList.get(position).getChoices().get(j).getId() == choiceSelectAdapter.getSelectedItems().get(i).getId()) {
                               questionList.get(position).getChoices().get(j).setValue(questionList.get(position).getChoices().get(j).getValue() + 1);
                           }
                       }
                   }
                   Retrofit retrofit = apiClient.getClient();
                   apiRest service = retrofit.create(apiRest.class);
                   Call<ApiResponse> call = service.addVote(questionList.get(position).getId(), choices);
                   call.enqueue(new Callback<ApiResponse>() {
                       @Override
                       public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                           if (response.isSuccessful()){
                               if (response.body().getCode()==200){
                                   questionList.get(position).setClose(true);
                                   List<Integer> votedlist = prefQuestionManager.loadVoted();
                                   ArrayList<Integer> audios = new ArrayList<Integer>();
                                   if (votedlist==null){
                                       votedlist= new ArrayList<Integer>();
                                   }
                                   for (int i = 0; i < votedlist.size(); i++) {
                                       audios.add(votedlist.get(i));
                                   }
                                   audios.add(questionList.get(position).getId());
                                   prefQuestionManager.storeVoted(audios);

                                   notifyDataSetChanged();
                                   Toasty.success(activity,response.body().getMessage(), Toast.LENGTH_SHORT).show();
                               }else{
                                   Toasty.error(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                               }

                           }else{
                               Toasty.error(activity, activity.getResources().getString(R.string.no_connexion), Toast.LENGTH_SHORT).show();
                           }

                       }
                       @Override
                       public void onFailure(Call<ApiResponse> call, Throwable t) {
                           Toasty.error(activity, activity.getResources().getString(R.string.no_connexion), Toast.LENGTH_SHORT).show();

                       }
                   });
               }else{
                   Toasty.error(activity, activity.getResources().getString(R.string.select_choice), Toast.LENGTH_SHORT).show();
               }
            }
        });

        LinearLayoutManager linearLayoutManagerValue = new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false);

        final ChoiceValueAdapter choiceValueAdapter  =new ChoiceValueAdapter(questionList.get(position).getChoices(),activity);

        holder.recycler_view_item_question_values.setHasFixedSize(true);
        holder.recycler_view_item_question_values.setAdapter(choiceValueAdapter);
        holder.recycler_view_item_question_values.setLayoutManager(linearLayoutManagerValue);
        if (questionList.get(position).getClose()){
            holder.recycler_view_item_question_choices.setVisibility(View.GONE);
            holder.recycler_view_item_question_values.setVisibility(View.VISIBLE);
            holder.text_view_item_question_send.setVisibility(View.GONE);
        }else{
            if (exist){
                holder.recycler_view_item_question_choices.setVisibility(View.GONE);
                holder.recycler_view_item_question_values.setVisibility(View.VISIBLE);
                holder.text_view_item_question_send.setVisibility(View.GONE);
            }else{
                holder.recycler_view_item_question_choices.setVisibility(View.VISIBLE);
                holder.recycler_view_item_question_values.setVisibility(View.GONE);
                holder.text_view_item_question_send.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class QuestionHolder extends RecyclerView.ViewHolder {

        private final RecyclerView recycler_view_item_question_choices;
        private final TextView text_view_item_question;
        private final TextView text_view_item_question_send;
        private final TextView text_view_item_question_number;
        private final RecyclerView recycler_view_item_question_values;
        private final CardView card_view_item_question;
        private final ImageView image_view_item_question_featured;

        public QuestionHolder(View itemView) {
            super(itemView);
            this.image_view_item_question_featured=(ImageView) itemView.findViewById(R.id.image_view_item_question_featured);
            this.card_view_item_question=(CardView) itemView.findViewById(R.id.card_view_item_question);
            this.text_view_item_question_number=(TextView) itemView.findViewById(R.id.text_view_item_question_number);
            this.recycler_view_item_question_choices=(RecyclerView) itemView.findViewById(R.id.recycler_view_item_question_choices);
            this.text_view_item_question=(TextView) itemView.findViewById(R.id.text_view_item_question);
            this.text_view_item_question_send=(TextView) itemView.findViewById(R.id.text_view_item_question_send);

            this.recycler_view_item_question_values=(RecyclerView) itemView.findViewById(R.id.recycler_view_item_question_values);
        }
    }
}
