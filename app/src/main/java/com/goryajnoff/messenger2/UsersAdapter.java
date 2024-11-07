package com.goryajnoff.messenger2;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {
    private List<User> users = new ArrayList<>();
    private OnClickUserListener onClickUserListener;

    public void setOnClickUserListener(OnClickUserListener onClickUserListener) {
        this.onClickUserListener = onClickUserListener;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        String userInfo = String.format("%s %s,%s", user.getName(), user.getLastName(), user.getAge());
        holder.textViewUserInfo.setText(userInfo);
        int idDrawable;
        if (user.isOnline()) {
            idDrawable = R.drawable.circle_green;
        } else {
            idDrawable = R.drawable.circle_red;
        }
        Drawable background = ContextCompat.getDrawable(holder.itemView.getContext(), idDrawable);
        holder.viewUserStatus.setBackground(background);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickUserListener != null) {
                    onClickUserListener.onClick(user);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    interface OnClickUserListener {
        void onClick(User user);
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewUserInfo;
        private View viewUserStatus;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserInfo = itemView.findViewById(R.id.textViewUserInfo);
            viewUserStatus = itemView.findViewById(R.id.viewUserStatus);
        }
    }
}
