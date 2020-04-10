package com.virmana.news_app.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.virmana.news_app.R;
import com.virmana.news_app.model.User;
import com.virmana.news_app.ui.Activities.UserActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Tamim on 04/11/2018.
 */

class SubscribeAdapter  extends  RecyclerView.Adapter<SubscribeAdapter.SubscribeHolder>{
    private List<User> userList;
    private Activity activity;
    public SubscribeAdapter(List<User> userList, Activity activity) {
        this.userList = userList;
        this.activity = activity;
    }

    @Override
    public SubscribeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subscribe, null);
        SubscribeHolder mh = new SubscribeHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SubscribeHolder holder,final int position) {
        if (!userList.get(position).getImage().isEmpty()){
            Picasso.with(activity).load(userList.get(position).getImage()).error(R.drawable.profile).placeholder(R.drawable.profile).into(holder.image_view_follow_iten);
        }else{
            Picasso.with(activity).load(R.drawable.profile).error(R.drawable.profile).placeholder(R.drawable.profile).into(holder.image_view_follow_iten);
        }
        Picasso.with(activity).load(userList.get(position).getThum()).error(R.drawable.placeholder).placeholder(R.drawable.placeholder).into(holder.image_view_item_subscribe_thum);
        holder.text_view_follow_itme_label.setText(userList.get(position).getLabel());
        holder.relative_layout_subscribte_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  =  new Intent(activity.getApplicationContext(), UserActivity.class);
                intent.putExtra("id",userList.get(position).getId());
                intent.putExtra("image",userList.get(position).getImage());
                intent.putExtra("name",userList.get(position).getName());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class SubscribeHolder extends RecyclerView.ViewHolder {

        private final CircleImageView image_view_follow_iten;
        private final ImageView image_view_item_subscribe_thum;
        private final TextView text_view_follow_itme_label;
        private final RelativeLayout relative_layout_subscribte_item;

        public SubscribeHolder(View itemView) {
            super(itemView);
            this.relative_layout_subscribte_item=(RelativeLayout) itemView.findViewById(R.id.relative_layout_subscribte_item);
            this.image_view_item_subscribe_thum=(ImageView) itemView.findViewById(R.id.image_view_item_subscribe_thum);
            this.image_view_follow_iten=(CircleImageView) itemView.findViewById(R.id.image_view_follow_iten);
            this.text_view_follow_itme_label=(TextView) itemView.findViewById(R.id.text_view_follow_itme_label);
        }
    }
}
