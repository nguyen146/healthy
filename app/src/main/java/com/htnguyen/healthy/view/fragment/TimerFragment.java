package com.htnguyen.healthy.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.htnguyen.healthy.R;
import com.htnguyen.healthy.dialog.CreateTimerDialog;
import com.htnguyen.healthy.model.Timer;
import com.htnguyen.healthy.util.DbHelper;
import com.htnguyen.healthy.util.Tools;
import com.htnguyen.healthy.view.activity.MainActivity;
import com.htnguyen.healthy.view.adapter.TimerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimerFragment extends BaseFragment implements
        TimerAdapter.TimerAdapterListener,
        CreateTimerDialog.OnCreateTimerListener{

    private Unbinder unbinder;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    RealmResults<Timer> timerRealmResults;
    private TimerAdapter mTimerAdapter;
    public TimerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).hideFab();

        //RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        timerRealmResults = DbHelper.getsItemRealmResults();
        mTimerAdapter = new TimerAdapter(timerRealmResults, this);

        recyclerView.setAdapter(mTimerAdapter);
        mTimerAdapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();

    }

    @OnClick(R.id.fabadd)
    public void OnAddItem(){
        new CreateTimerDialog(getActivity(), this).show();
    }

    public void refeshAdapter(){
        timerRealmResults = DbHelper.getsItemRealmResults();
        mTimerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDelete(Timer timer) {
        DbHelper.deleteTimer(getActivity(), timer);
        refeshAdapter();
    }

    @Override
    public void onCreate(String title, String date, String description) {
                Timer timer = new Timer(title,description,
                DbHelper.getRandomPendingId(),
                Tools.convertStringToDate(date));
        if(DbHelper.addTimer(getActivity(),timer)){
            showToastMessage(getString(R.string.saved));
        }else {
            showToastMessage(getString(R.string.errsaved));
        }
        refeshAdapter();
    }
}
