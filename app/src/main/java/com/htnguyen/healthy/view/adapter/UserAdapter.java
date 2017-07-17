package com.htnguyen.healthy.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.htnguyen.healthy.R;
import com.htnguyen.healthy.model.User;
import com.htnguyen.healthy.util.Tools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHoler>{

    List<User> userList = new ArrayList<>();
    private OnUserClickListener onUserClickListener;
    private Context context;

    public UserAdapter(List<User> userList, OnUserClickListener onUserClickListener, Context context) {
        this.userList = userList;
        this.onUserClickListener = onUserClickListener;
        this.context = context;
    }

    @Override
    public MyViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,
                parent, false);
        return new MyViewHoler(view);
    }

    @Override
    public void onBindViewHolder(MyViewHoler holder, int position) {
        User user = userList.get(holder.getAdapterPosition());
        Bitmap img = getImageUser(user);
        if (img !=null) holder.imgUserView.setImageBitmap(img);
        else {
            holder.imgUserView.setImageResource(R.drawable.user_default);
        }
        holder.nameUserView.setText(user.getUserName());
        if(user.getGender() == 1){
            holder.sexView.setText(context.getString(R.string.female));
        }else {
            holder.sexView.setText(context.getString(R.string.male));
        }
        holder.onlineView.setText("Online");

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHoler extends RecyclerView.ViewHolder implements OnClickListener{

        @BindView(R.id.imgUser)
        CircleImageView imgUserView;
        @BindView(R.id.txtName)
        TextView nameUserView;
        @BindView(R.id.txtSex)
        TextView sexView;
        @BindView(R.id.txtOnline)
        TextView onlineView;

        public MyViewHoler(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onUserClickListener.onUserClick(getAdapterPosition());
        }
    }

    public interface OnUserClickListener{
        void onUserClick(int position);
    }

    public Bitmap getImageUser(User user){
        if (user.getImage() == null) return null;
        byte[] byte1 = Tools.stringToByteArray(user.getImage().trim());
        return Tools.convertByteArrayToBitmap(byte1);
    }
}
