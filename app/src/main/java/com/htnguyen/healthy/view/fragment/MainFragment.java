package com.htnguyen.healthy.view.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.htnguyen.healthy.R;
import com.htnguyen.healthy.model.Category;
import com.htnguyen.healthy.view.adapter.CategoryAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    private Unbinder unbinder;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList = new ArrayList<>();
    private List<Category> categoryListRemove = new ArrayList<>();
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;


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
        //SwipeRefresh
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark,
                android.R.color.holo_green_dark, android.R.color.holo_blue_bright);
        swipeRefreshLayout.setOnRefreshListener(this);
        //RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.
                VERTICAL));

//        categoryAdapter = new CategoryAdapter(this, categoryList, context);
//        recyclerView.setAdapter(categoryAdapter);
//        categoryAdapter.notifyDataSetChanged();
//        init();
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
//        categoryAdapter.notifyDataSetChanged();

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

//    public void init(){
//        List<Items> itemses = new ArrayList<>();
//
//        Items items = new Items(Tools.getCurrentDate(), "+2 value", 10);
//        itemses.add(items);
//        Items items2 = new Items(Tools.getCurrentDate(), "+2 value", 5);
//        itemses.add(items2);
//        Items items3 = new Items(Tools.getCurrentDate(), "+2 value", 15);
//        itemses.add(items3);
//
//        final Category category = new Category(Tools.getCurrentDate(),"Weight","Can nang", itemses);
//        mCategory.push().setValue(category);
//
//    }

    public void removeList(){
        for(int i = 0; i<categoryList.size(); i++){
            if(categoryListRemove.equals(categoryList.get(i))){
                categoryList.remove(i);
            }
        }
        categoryAdapter.notifyDataSetChanged();
    }
}
