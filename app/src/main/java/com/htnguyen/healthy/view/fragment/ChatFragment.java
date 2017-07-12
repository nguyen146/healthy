package com.htnguyen.healthy.view.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.htnguyen.healthy.R;
import com.htnguyen.healthy.model.Chat;
import com.htnguyen.healthy.util.ListUtil;
import com.htnguyen.healthy.view.activity.MainActivity;
import com.htnguyen.healthy.view.adapter.ChatAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private Unbinder unbinder;
    @BindView(R.id.edit_text_message)
    EditText edtChatView;
    @BindView(R.id.btn_send)
    ImageButton btnSend;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private List<Chat> mChat = new ArrayList<>();
    private ChatAdapter chatAdapter;
    private String Uid;
    private int limitLoading = 10;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).hideFab();
        Uid = getArguments().getString(UserFragment.U_ID_USER);
        showToastMessage(Uid);
        if (Uid == null) {
            //Stop
        }
        //SwipeRefresh
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark,
                android.R.color.holo_green_dark, android.R.color.holo_blue_bright);
        swipeRefreshLayout.setOnRefreshListener(this);
        //RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        chatAdapter = new ChatAdapter(mChat);
        recyclerView.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();
        // Inflate the layout for this fragment
        getChatRoom();
        return view;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @OnClick(R.id.btn_send)
    public void sendMessage() {
        String message = edtChatView.getText().toString().trim();
        if (message.equals("")) {
            showToastMessage("Message null");
            return;
        } else {
            chat(message, Uid);
        }
        edtChatView.setText("");
        edtChatView.clearFocus();
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Refresh adapter here
                limitLoading += 11;
                Query query = mChatRoom.child(Uid).limitToLast(limitLoading).orderByPriority();
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Chat> chats = new ArrayList<Chat>();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Chat chat = dataSnapshot1.getValue(Chat.class);
                            chat.setKey(dataSnapshot1.getKey());
                            if (!ListUtil.equalChatKey(mChat, chat.getKey())) {
                                chats.add(chat);
                            }
                        }
                        for (int i = chats.size()-1; i >=0; i--){
                            mChat.add(0,chats.get(i));
                        }
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

    public interface OnBackListener {
        void onBackClick(ChatFragment chatFragment);
    }

    public void checkCreatedRoom(final String Uid) {
//        mChatRoom.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.hasChild(Uid)){
//
//                }else {
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    public void chat(String message, final String Uid) {
        final Chat chat = new Chat(getCurrentUserUid(), message.trim(), System.currentTimeMillis());
        DatabaseReference reference = getReferenceUserChatRoom(Uid).child(getCurrentUserUid());
        reference.push().setValue(chat, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError == null){
                    updateChatInRoom(chat);
                }else {
                    showToastMessage(getContext().getString(R.string.errSendData));
                }
            }
        });

    }

    String lastId;

    public void updateChatInRoom(Chat chat) {
        mChatRoom.child(Uid).push().setValue(chat);
    }

    public void getChatRoom() {
        mChatRoom.child(Uid).limitToLast(limitLoading).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Chat chat = dataSnapshot.getValue(Chat.class);
                chat.setKey(dataSnapshot.getKey());
                mChat.add(chat);
                chatAdapter.notifyDataSetChanged();
                if(mChat.size()>0)
                recyclerView.scrollToPosition(mChat.size() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
//        mChatRoom.child(Uid).limitToLast(10).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
//                    Chat chat = dataSnapshot1.getValue(Chat.class);
//                    chat.setKey(dataSnapshot1.getKey());
//                    mChat.add(chat);
//                    chatAdapter.notifyDataSetChanged();
//                    recyclerView.scrollToPosition(mChat.size() - 1);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }
}
