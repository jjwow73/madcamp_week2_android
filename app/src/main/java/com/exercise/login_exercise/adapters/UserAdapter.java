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
        holder.textViewPhone.setText(user.getPhone());

        if (user.getTemperature() != null) {
            holder.textViewTemp.setText(user.getTemperature() + "℃");
        } else {
            holder.textViewTemp.setText("체온을 측정 해주세요");
        }
        String profileUrl = "";
        if (user.getImageUrl() == null) {
            profileUrl = "default.png";
        } else {
            profileUrl = user.getImageUrl();
        }
        // 학생 프로필 업로드
        String imageUrl = "http://192.249.19.243:8780/api/v3/profile/" + profileUrl;
        Glide.with(mContext).load(imageUrl).into(imageView);
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + user.getLastChecked());
        if (user.getLastChecked() != null) {
            holder.attendanceCheck.setImageResource(R.drawable.attendance_check);
        } else {
            holder.attendanceCheck.setImageResource(R.drawable.attendance_null);
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewPhone, textViewTemp;
        ImageView imageView;
        ImageView attendanceCheck;

        public UserViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPhone = itemView.findViewById(R.id.textViewPhone);
            textViewTemp = itemView.findViewById(R.id.textViewTemp);
            imageView = itemView.findViewById(R.id.image);
            attendanceCheck = itemView.findViewById(R.id.attendanceCheck);
        }
    }
}
