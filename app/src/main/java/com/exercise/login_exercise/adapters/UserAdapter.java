package com.exercise.login_exercise.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.exercise.login_exercise.R;
import com.exercise.login_exercise.models.User;

import java.util.List;

/**
 * Created by jongwow on 2020-07-18.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private Context mContext;
    private List<User> userList;

    public UserAdapter(Context mContext, List<User> userList) {
        this.mContext = mContext;
        this.userList = userList;
    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_users, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        ImageView imageView = holder.imageView;

        holder.textViewName.setText(user.getName());
        holder.textViewEmail.setText(user.getEmail());

        String imageUrl = "http://192.249.19.243:8780/api/v1/image/" + user.getImageUrl();
        Glide.with(mContext).load(imageUrl).into(imageView);
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewEmail;
        ImageView imageView;

        public UserViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textViewName);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
