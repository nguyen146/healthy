package com.htnguyen.healthy.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.htnguyen.healthy.R;
import com.htnguyen.healthy.model.Chat;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int CHAT_ME = 0;
    private static final int CHAT_USER = 1;

    private List<Chat> mChats = new ArrayList<>();

    public ChatAdapter(List<Chat> mChats) {
        this.mChats = mChats;
    }

    public void add(Chat chat) {
        mChats.add(chat);
        notifyItemInserted(mChats.size() - 1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case CHAT_ME:
                View viewChatMine = layoutInflater.inflate(R.layout.item_text_chat_me, parent, false);
                viewHolder = new MeChatViewHolder(viewChatMine);
                break;
            case CHAT_USER:
                View viewChatOther = layoutInflater.inflate(R.layout.item_text_chat_user, parent, false);
                viewHolder = new UserChatViewHolder(viewChatOther);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Chat chat = mChats.get(position);
        switch (holder.getItemViewType()){
            case CHAT_ME:
                ((MeChatViewHolder)holder).txtChatMessage.setText(chat.getSendMessage().trim());
                ((MeChatViewHolder)holder).imgUser.setImageResource(R.drawable.user_default);
                break;
            case CHAT_USER:
                ((UserChatViewHolder)holder).txtChatMessage.setText(chat.getSendMessage().trim());
                ((UserChatViewHolder)holder).imgUser.setImageResource(R.drawable.user_default);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    private static class MeChatViewHolder extends RecyclerView.ViewHolder {
        private TextView txtChatMessage;
        private ImageView imgUser;

        public MeChatViewHolder(View itemView) {
            super(itemView);
            txtChatMessage = (TextView) itemView.findViewById(R.id.chat_message);
            imgUser = (ImageView) itemView.findViewById(R.id.img_user);
        }
    }

    private static class UserChatViewHolder extends RecyclerView.ViewHolder {
        private TextView txtChatMessage;
        private ImageView imgUser;

        public UserChatViewHolder(View itemView) {
            super(itemView);
            txtChatMessage = (TextView) itemView.findViewById(R.id.chat_message);
            imgUser = (ImageView) itemView.findViewById(R.id.img_user);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mChats.get(position).getSendId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
        {
            return CHAT_ME;
        }
        return CHAT_USER;
    }
}
