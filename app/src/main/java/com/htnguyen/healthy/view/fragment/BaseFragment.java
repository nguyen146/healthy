package com.htnguyen.healthy.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.htnguyen.healthy.util.Constants;

public class BaseFragment extends Fragment {

    protected FirebaseAuth mAuth;
    protected DatabaseReference mDatabase;
    protected DatabaseReference mCategory;
    protected DatabaseReference mUser;
    protected DatabaseReference mUsers;
    protected DatabaseReference mChatRoom;

    protected void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setting
        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabase.keepSynced(true);
        mUsers = mDatabase.child(Constants.TABLE_USER);
        mAuth = FirebaseAuth.getInstance();
        try {
            mUser = mDatabase.child(Constants.TABLE_USER).child(getCurrentUserUid());
//            mDatabase.keepSynced(true);
        }catch (NullPointerException e){
            mUser = mDatabase.child(Constants.TABLE_USER).child("OfflineMode");
//            mDatabase.keepSynced(false);
        }
        mChatRoom = mUser.child(Constants.CHAT_ROOM);
        mCategory = mUser.child(Constants.TABLE_CATEGORY);
    }

    protected DatabaseReference getReferenceUserChatRoom(String Uid){
        return mUsers.child(Uid).child(Constants.CHAT_ROOM);
    }

    protected String getCurrentUserUid(){
        return mAuth.getCurrentUser().getUid();
    }
}
