package com.htnguyen.healthy.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.htnguyen.healthy.R;
import com.htnguyen.healthy.model.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

public class TimerAdapter extends RecyclerView.Adapter<TimerAdapter.MyViewHolder>{
    private RealmResults<Timer> itemRealmResults;
    private TimerAdapterListener timerAdapterListener;

    public TimerAdapter(RealmResults<Timer> itemRealmResults, TimerAdapterListener timerAdapterListener) {
        this.itemRealmResults = itemRealmResults;
        this.timerAdapterListener = timerAdapterListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timer,
                parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Timer timer = itemRealmResults.get(holder.getAdapterPosition());
        holder.titleView.setText(timer.getTitle());
        holder.descriptionView.setText(timer.getDescription());
        holder.timeView.setText(timer.getWakeUpTime().toString());
        holder.deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerAdapterListener.onDelete(timer);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemRealmResults.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.title)
        TextView titleView;
        @BindView(R.id.description)
        TextView descriptionView;
        @BindView(R.id.time)
        TextView timeView;
        @BindView(R.id.delete)
        TextView deleteView;


        public MyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public interface TimerAdapterListener{
        void onDelete(Timer timer);
    }
}
