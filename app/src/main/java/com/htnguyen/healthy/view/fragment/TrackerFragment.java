package com.htnguyen.healthy.view.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.htnguyen.healthy.R;
import com.htnguyen.healthy.dialog.ConfirmDialog;
import com.htnguyen.healthy.dialog.CreateCategoryDialog;
import com.htnguyen.healthy.dialog.LoadingDialog;
import com.htnguyen.healthy.dialog.TrackerDialog;
import com.htnguyen.healthy.model.Category;
import com.htnguyen.healthy.view.activity.MainActivity;
import com.htnguyen.healthy.view.adapter.CategoryAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrackerFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        CategoryAdapter.CategoryAdapterListener, CreateCategoryDialog.OnCreateCategoryListener,ConfirmDialog.OnConfirmListener{

    private Unbinder unbinder;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList = new ArrayList<>();
    private List<Category> categoryListRemove = new ArrayList<>();
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;


    public TrackerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracker, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).hideFab();
        //SwipeRefresh
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark,
                android.R.color.holo_green_dark, android.R.color.holo_blue_bright);
        swipeRefreshLayout.setOnRefreshListener(this);
        //RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.
//                VERTICAL));

        categoryAdapter = new CategoryAdapter(this, categoryList, getContext());
        recyclerView.setAdapter(categoryAdapter);
        categoryAdapter.notifyDataSetChanged();
        loadListTracker();
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
        categoryAdapter.notifyDataSetChanged();

    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Refresh adapter here
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }
//
//    @Override
//    public void onFabClick(int position) {
//        Category category = categoryList.get(position);
//        new TrackerDialog(getActivity(),category, mCategory).show();
//    }

    private void loadListTracker(){
        mCategory.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Category category1 = dataSnapshot.getValue(Category.class);
                category1.setCateId(dataSnapshot.getKey());
                categoryList.add(category1);
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Category category1 = dataSnapshot.getValue(Category.class);
                for(int i = 0 ; i< categoryList.size(); i++){
                    if(dataSnapshot.getKey().equals(categoryList.get(i).getCateId())){
                        categoryList.get(i).setDescription(category1.getDescription());
                        categoryList.get(i).setItemsList(category1.getItemsList());
                    }
                }
                categoryAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                for(int i = 0 ; i< categoryList.size(); i++){
                if(dataSnapshot.getKey().equals(categoryList.get(i).getCateId())){
                    categoryList.remove(i);
                }
            }
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @OnClick(R.id.fabadd)
    public void addTracker(){
        new CreateCategoryDialog(getActivity(),this).show();
    }

    @Override
    public void onCreateCategory(String title, String nameValue) {
        //        Items items = new Items()
        Category category = new Category(title,"",nameValue);
        final LoadingDialog loadingDialog = new LoadingDialog(getActivity(),getString(R.string.progressing));
        loadingDialog.show();
        mCategory.push().setValue(category, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError!=null){
                    showToastMessage(getString(R.string.errsaved));
                }
                else {
                    showToastMessage(getString(R.string.saved));
                }
                loadingDialog.dismiss();
            }
        });

    }

    @Override
    public void onAddItem(Category category) {
        new TrackerDialog(getActivity(),category, mCategory).show();
    }

    @Override
    public void onDeleteCatagory(Category category) {
        new ConfirmDialog(getActivity(), this, getString(R.string.deleteTracker) , category).show();
    }

    @Override
    public void onConfirm(Category category) {
        final LoadingDialog loadingDialog = new LoadingDialog(getActivity(), getString(R.string.progressing));
        loadingDialog.show();
        mCategory.child(category.getCateId()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError!=null){
                    showToastMessage(getString(R.string.delerror));
                }else {
                    showToastMessage(getString(R.string.delsucess));
                }
                loadingDialog.dismiss();
            }
        });
    }
}
