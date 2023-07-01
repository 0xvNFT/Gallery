package com.album.jayson.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.album.jayson.AlbumContentActivity;
import com.album.jayson.R;
import com.album.jayson.model.AlbumModel;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {

    private final Context context;
    private final List<AlbumModel> albums;

    public AlbumAdapter(Context context, List<AlbumModel> albums) {
        this.context = context;
        this.albums = albums;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_directory, parent, false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        AlbumModel album = albums.get(position);
        holder.directoryNameTextView.setText(album.getDirectoryName());

        if (album.getThumbnailPath() != null) {
            Glide.with(context)
                    .load(Uri.fromFile(new File(album.getThumbnailPath())))
                    .placeholder(R.drawable.folder_placeholder)
                    .into(holder.thumbnailImageView);
        } else {
            holder.thumbnailImageView.setImageResource(R.drawable.folder_placeholder);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AlbumContentActivity.class);
            intent.putExtra("directoryPath", album.getDirectoryPath());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public static class AlbumViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnailImageView;
        public TextView directoryNameTextView;

        public AlbumViewHolder(View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.album_thumbnail);
            directoryNameTextView = itemView.findViewById(R.id.directory_name_text_view);
        }
    }
}
