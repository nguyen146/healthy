package com.htnguyen.healthy.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.htnguyen.healthy.R;
import com.htnguyen.healthy.model.Heart;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

public class HeartRateAdapter extends RecyclerView.Adapter<HeartRateAdapter.MyViewHolder>{

    private RealmResults<Heart> heartList;
    private HeartAdapterListener heartAdapterListener;

    public HeartRateAdapter(RealmResults<Heart> heartList, HeartAdapterListener heartAdapterListener) {
        this.heartList = heartList;
        this.heartAdapterListener = heartAdapterListener;
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
        holder.titleView.setText(heart.getTitle());
        holder.timeStamp.setText(heart.getTimeStamp());
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
