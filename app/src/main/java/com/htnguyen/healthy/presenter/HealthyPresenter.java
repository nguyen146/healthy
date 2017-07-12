package com.htnguyen.healthy.presenter;

import android.support.annotation.NonNull;

import com.htnguyen.healthy.view.HealthyView;

import javax.inject.Inject;

public class HealthyPresenter implements Presenter{

    private HealthyView healthyView;

    @Inject
    public HealthyPresenter() {
    }

    public void setView(@NonNull HealthyView view){
        this.healthyView = view;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public void navigateToHeartRateActivity(){
        healthyView.navigateToHeartRateActivity();
    }
}
