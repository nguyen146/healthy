package com.htnguyen.healthy.view.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.htnguyen.healthy.R;
import com.htnguyen.healthy.dialog.CalculateBMI;
import com.htnguyen.healthy.dialog.CalculateFat;
import com.htnguyen.healthy.dialog.CalculateMaxHeart;
import com.htnguyen.healthy.dialog.CalculateWater;
import com.htnguyen.healthy.model.Heart;
import com.htnguyen.healthy.model.Timer;
import com.htnguyen.healthy.navigation.Navigator;
import com.htnguyen.healthy.util.DbHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends BaseFragment{

    private Unbinder unbinder;


    @BindView(R.id.bpm)
    TextView bpmView;
    @BindView(R.id.timer)
    TextView timerView;
    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        RealmResults<Heart> hearts = DbHelper.getsHeartRealmResults();
            int heart = 0;
            for(int i=0; i<hearts.size(); i++){
                heart += hearts.get(i).getHeart();
            }
            if (hearts.size()>0)
            bpmView.setText(String.valueOf((heart/hearts.size())));
        else {
                bpmView.setText("0");
            }

        RealmResults<Timer> timers = DbHelper.getsItemRealmResults();
            timerView.setText(String.valueOf(timers.size()));

        return view;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @OnClick(R.id.btnHeart)
    public void onHeart(){
        new Navigator().navigateToHeartRate(getActivity());
    }

    @OnClick(R.id.btnTimer)
    public void ontimer(){
        TimerFragment timerFragment = new TimerFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack if needed
        transaction.replace(R.id.content_main, timerFragment);
        transaction.addToBackStack(null);

// Commit the transaction
        transaction.commit();
    }

    @OnClick(R.id.btnMaxheart)
    public void maxHeart(){
        new CalculateMaxHeart(getActivity()).show();
    }

    @OnClick(R.id.btnBmi)
    public void bmi(){
        new CalculateBMI(getActivity()).show();
    }

    @OnClick(R.id.btnWater)
    public void water(){
        new CalculateWater(getActivity()).show();
    }

    @OnClick(R.id.btnFat)
    public void fat(){
        new CalculateFat(getActivity()).show();
    }
}
