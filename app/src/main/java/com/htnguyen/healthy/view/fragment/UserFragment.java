package com.htnguyen.healthy.view.fragment;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.htnguyen.healthy.R;
import com.htnguyen.healthy.dialog.ChatDialog;
import com.htnguyen.healthy.model.User;
import com.htnguyen.healthy.util.ListUtil;
import com.htnguyen.healthy.util.Tools;
import com.htnguyen.healthy.view.activity.MainActivity;
import com.htnguyen.healthy.view.adapter.UserAdapter;
import com.htnguyen.healthy.view.component.BadgeDrawable;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends BaseFragment implements SwipyRefreshLayout.OnRefreshListener, UserAdapter.OnUserClickListener {

    //    private OnUserClickListener userClickListener;
    public static final String U_ID_USER = "UidUser";
    private Unbinder unbinder;
    private UserAdapter userAdapter;
    private List<User> userList = new ArrayList<>();
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipyRefreshLayout swipeRefreshLayout;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    private int limitLoading = 10;

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnUserClickListener ){
//            userClickListener = (OnUserClickListener ) context;
//        } else {
//            throw new RuntimeException(context.toString() + " must implement onViewSelected");
//        }
    }
    int i =0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        unbinder = ButterKnife.bind(this, view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_user:
                        showToastMessage("User");
                        break;
                    case R.id.nav_chat:
                        showToastMessage("Chat");
                        break;
                    case R.id.nav_fr:
                        showToastMessage("Friend");
                        break;
                }
                return true;
            }
        });
        //SwipeRefresh
        ((MainActivity) getActivity()).hideFab();
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark,
                android.R.color.holo_green_dark, android.R.color.holo_blue_bright);
        swipeRefreshLayout.setOnRefreshListener(this);
        //RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.
//                VERTICAL));
        userAdapter = new UserAdapter(userList, this, getContext());
        recyclerView.setAdapter(userAdapter);
        userAdapter.notifyDataSetChanged();
        getUser();
        // Inflate the layout for this fragment
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.nav_chat);
        LayerDrawable icon = (LayerDrawable) menuItem.getIcon();
        setBadgeCount(getContext(), icon, String.valueOf(userList.size()));
        //https://mobikul.com/adding-badge-count-on-menu-items-like-cart-notification-etc/
        return view;
    }



    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }

    public void getUser() {

        mUsers.limitToLast(limitLoading).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                if(!user.getUserId().equals(mAuth.getCurrentUser().getUid()))
                userList.add(user);
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                for(int i=0; i< userList.size(); i++){
                    if (user.getUserId().equals(userList.get(i).getUserId())){
                        userList.get(i).setImage(user.getImage());
                        userList.get(i).setGender(user.getGender());
                        userList.get(i).setUserName(user.getUserName());
                    }
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onUserClick(int position) {
//        showToastMessage(userList.get(position).getUserName());
//        ChatFragment chatFragment = new ChatFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString(U_ID_USER, userList.get(position).getUserId());
//        chatFragment.setArguments(bundle);
        Bitmap userOther = getImageUser(userList.get(position));
        Bitmap user = ((MainActivity)getActivity()).getImgUser();
        new ChatDialog(getContext(), userList.get(position).getUserId(), getActivity(), userOther, user).show();
//        userClickListener.onUserClick(chatFragment);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }


    //    public interface OnUserClickListener {
//        void onUserClick(ChatFragment chatFragment);
//        }
    public Bitmap getImageUser(User user) {
        if (user.getImage() == null) return null;
        byte[] byte1 = Tools.stringToByteArray(user.getImage().trim());
        return Tools.convertByteArrayToBitmap(byte1);
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                limitLoading += 11;
                Query query = mUsers.limitToLast(limitLoading).orderByPriority();
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            User user = dataSnapshot1.getValue(User.class);
                            if(!user.getUserId().equals(mAuth.getCurrentUser().getUid()) && !ListUtil.equalUserKey(userList, user.getUserId())){
                                userList.add(user);
                            }
                        }
                        if (userList.size() > 0)
                            recyclerView.scrollToPosition(userList.size() - 1);
                        userAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                swipeRefreshLayout.setRefreshing(false);
            }

        }, 2000);
    }
}
