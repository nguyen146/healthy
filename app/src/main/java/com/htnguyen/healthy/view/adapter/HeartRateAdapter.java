package com.htnguyen.healthy.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.htnguyen.healthy.R;
import com.htnguyen.healthy.model.Heart;
import com.htnguyen.healthy.util.Tools;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

public class HeartRateAdapter extends RecyclerView.Adapter<HeartRateAdapter.MyViewHolder>{

    private RealmResults<Heart> heartList;
    private HeartAdapterListener heartAdapterListener;
    private Context context;

    public HeartRateAdapter(RealmResults<Heart> heartList, HeartAdapterListener heartAdapterListener, Context context) {
        this.heartList = heartList;
        this.heartAdapterListener = heartAdapterListener;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_heart_rate,
                parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Heart heart = heartList.get(holder.getAdapterPosition());
        switch (heart.getStatus()){
            case 0:
                holder.titleView.setText(context.getString(R.string.StatusHeartRate1));
                break;
            case 1:
                holder.titleView.setText(context.getString(R.string.StatusHeartRate2));
                break;
            case 2:
                holder.titleView.setText(context.getString(R.string.StatusHeartRate3));
                break;
        }
        holder.timeStamp.setText(Tools.convertDateToString(heart.getTimeStamp()));
        holder.bpmView.setText(String.valueOf(heart.getHeart()));
        holder.deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heartAdapterListener.onDelete(heartList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return heartList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.bpm2)
        TextView bpmView;
        @BindView(R.id.title)
        TextView titleView;
        @BindView(R.id.timeStamp)
        TextView timeStamp;
        @BindView(R.id.delete)
        TextView deleteView;


        public MyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public interface HeartAdapterListener{
        void onDelete(Heart heart);
    }
}
