package com.htnguyen.healthy.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.htnguyen.healthy.R;
import com.htnguyen.healthy.model.Chat;
import com.htnguyen.healthy.model.Timer;
import com.htnguyen.healthy.model.TimerSender;
import com.htnguyen.healthy.util.Constants;
import com.htnguyen.healthy.util.DbHelper;
import com.htnguyen.healthy.util.ListUtil;
import com.htnguyen.healthy.util.Tools;
import com.htnguyen.healthy.view.adapter.ChatAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ChatDialog extends Dialog implements SwipeRefreshLayout.OnRefreshListener,
        CreateTimerDialog.OnCreateTimerListener, ChatAdapter.OnGetAdapterListener, DetailTimerDialog.OnTimerAcceptListener {

    DatabaseReference mChatRoom;
    @BindView(R.id.edit_text_message)
    EditText edtChatView;
    @BindView(R.id.btn_send)
    ImageButton btnSend;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    private Activity mActivity;
    private List<Chat> mChat = new ArrayList<>();
    private ChatAdapter chatAdapter;
    private String Uid;
    private int limitLoading = 10;

    public ChatDialog(@NonNull Context context, String Uid, Activity mActivity, Bitmap userOther, Bitmap user) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_chat);
        this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(true);
        ButterKnife.bind(this, getWindow().getDecorView());
        mChatRoom = FirebaseDatabase.getInstance().
                getReference().
                child(Constants.TABLE_USER).
                child(getCurrentUserUid()).
                child(Constants.CHAT_ROOM);
        this.Uid = Uid;
        this.mActivity = mActivity;
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark,
                android.R.color.holo_green_dark, android.R.color.holo_blue_bright);
        swipeRefreshLayout.setOnRefreshListener(this);
        //RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.setHasFixedSize(true);
        chatAdapter = new ChatAdapter(this, mChat, userOther, user, getContext());
        recyclerView.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();
        getChatRoom();
        // Inflate the layout for this fragment
    }

    @OnClick(R.id.btn_send)
    public void sendMessage() {
        String message = edtChatView.getText().toString().trim();
        if (message.equals("")) {
            Toast.makeText(mActivity, getContext().getString(R.string.null_message), Toast.LENGTH_SHORT).show();
            return;
        } else {
            chat(message, Uid);
        }
        edtChatView.setText("");
        edtChatView.clearFocus();
        final InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
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
                        for (int i = chats.size() - 1; i >= 0; i--) {
                            mChat.add(0, chats.get(i));
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

    public void chat(String message, final String Uid) {
        final Chat chat = new Chat(getCurrentUserUid(), message.trim(), System.currentTimeMillis());
        DatabaseReference reference = getReferenceUserChatRoom(Uid).child(getCurrentUserUid());
        reference.push().setValue(chat, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    updateChatInRoom(chat);
                } else {
                    Toast.makeText(mActivity, getContext().getString(R.string.errSendData), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void chatTimer(final String Uid, List<TimerSender> timer) {
        final Chat chat = new Chat(getCurrentUserUid(), getContext().getString(R.string.send_clock), System.currentTimeMillis(), timer);
        DatabaseReference reference = getReferenceUserChatRoom(Uid).child(getCurrentUserUid());
        reference.push().setValue(chat, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    updateChatInRoom(chat);
                } else {
                    Toast.makeText(mActivity, getContext().getString(R.string.errSendData), Toast.LENGTH_SHORT).show();
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
                if (mChat.size() > 0)
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
    }

    private String getCurrentUserUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    private DatabaseReference getReferenceUserChatRoom(String Uid) {
        return FirebaseDatabase.getInstance().getReference().child(Constants.TABLE_USER).child(Uid).child(Constants.CHAT_ROOM);
    }

    @OnClick(R.id.btn_send_clock)
    public void onSendClock(){
        new CreateTimerDialog(getContext(),this).show();
    }

    @Override
    public void onCreate(String title, String date, String description, String phone) {
        List<TimerSender> timers = new ArrayList<>();
        TimerSender timer = new TimerSender(title, description, Tools.convertDateToLong(Tools.convertStringToDate(date)), phone);
        timers.add(timer);
        chatTimer(Uid, timers);
    }

    @Override
    public void onReceivedTimer(TimerSender timer) {
        new DetailTimerDialog(getContext(),this, timer).show();
    }

    @Override
    public void onAccept(TimerSender timer) {
        String description;
        description = timer.getDescription() == null ? "" : timer.getDescription();
        String phone = timer.getPhoneNumber() == null ? "" : timer.getPhoneNumber();
        Timer timer1 = new Timer(timer.getTitle(),description,
                DbHelper.getRandomPendingId(),
                Tools.convertLongToDate(timer.getWakeUpTime()), phone);
        if(DbHelper.addTimer(mActivity,timer1)){
            Toast.makeText(mActivity,getContext().getString(R.string.saved),Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(mActivity,getContext().getString(R.string.errsaved),Toast.LENGTH_SHORT).show();
        }
    }
}
