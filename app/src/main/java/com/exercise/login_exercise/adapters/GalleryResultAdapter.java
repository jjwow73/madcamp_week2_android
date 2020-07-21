package com.exercise.login_exercise.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.exercise.login_exercise.R;

import java.util.ArrayList;

/**
 * Created by  on 2020-07-21.
 */
public class GalleryResultAdapter extends RecyclerView.Adapter<GalleryResultAdapter.GalleryResultHolder> {
    private ArrayList<String> urls = new ArrayList<>();

    @NonNull
    @Override
    public GalleryResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_gallery, parent, false);
        return new GalleryResultHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull GalleryResultHolder holder, int position) {
        String url = urls.get(position);

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(holder.imageView.getContext());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        String imageUrl = "http://192.249.19.243:8780/api/v1/image/" + url;

        Glide.with(holder.imageView.getContext())
                .load(imageUrl)
                .placeholder(circularProgressDrawable)
                .into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    public void setResults(ArrayList<String> urls) {
        this.urls=urls;
        notifyDataSetChanged();
    }

    public void addResult(String url){
        this.urls.add(url);
        notifyDataSetChanged();
    }

    static class GalleryResultHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public GalleryResultHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
        }
    }
}
