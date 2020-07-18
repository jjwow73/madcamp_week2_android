package com.exercise.login_exercise.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.exercise.login_exercise.R;

import java.util.ArrayList;

/**
 * Created by jongwow on 2020-07-18.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private Context mContext;
    private ArrayList<String> urls;

    public GalleryAdapter(Context mContext, ArrayList<String> urls) {
        this.mContext = mContext;
        this.urls = urls;
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_gallery, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        String url = urls.get(position);

        ImageView imageView = holder.imageView;

        String imageUrl = "http://192.249.19.243:8780/api/v1/image/" + url;
        Glide.with(mContext).load(imageUrl).into(imageView);
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    public class GalleryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public GalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
