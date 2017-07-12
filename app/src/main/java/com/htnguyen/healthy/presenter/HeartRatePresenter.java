package com.htnguyen.healthy.presenter;

import android.support.annotation.NonNull;

import com.htnguyen.healthy.view.HeartRateView;

import javax.inject.Inject;

public class HeartRatePresenter implements Presenter{

    private HeartRateView heartRateView;

    @Inject
    public HeartRatePresenter() {
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

    public void setView(@NonNull HeartRateView view){
        this.heartRateView = view;
    }

    public void initializeView(){
        heartRateView.init();
    }

    public void updateUi(){
        heartRateView.renderUi();
    }
}
