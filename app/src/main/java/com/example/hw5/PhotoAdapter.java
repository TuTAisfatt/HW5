package com.example.hw5;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private Context context;
    private ArrayList<Photo> photoList;

    public PhotoAdapter(Context context, ArrayList<Photo> photoList) {
        this.context = context;
        this.photoList = photoList;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Photo photo = photoList.get(position);

        holder.titleText.setText(photo.getTitle());
        holder.descText.setText(photo.getDescription());

        // Load image from file path
        File imgFile = new File(photo.getImagePath());
        if (imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(photo.getImagePath());
            holder.imageView.setImageBitmap(bitmap);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_launcher_background); // fallback image
        }

        // Open edit screen
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddEditPhotoActivity.class);
            intent.putExtra("id", photo.getId());
            intent.putExtra("title", photo.getTitle());
            intent.putExtra("description", photo.getDescription());
            intent.putExtra("imagePath", photo.getImagePath());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleText, descText;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView); // Make sure this matches item_photo.xml
            titleText = itemView.findViewById(R.id.textTitle);
            descText = itemView.findViewById(R.id.textDescription);
        }
    }
}
