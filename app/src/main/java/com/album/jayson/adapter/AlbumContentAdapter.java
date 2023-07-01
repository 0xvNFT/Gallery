package com.album.jayson.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.album.jayson.R;
import com.album.jayson.model.AlbumContentModel;
import com.bumptech.glide.Glide;

import java.util.List;

public class AlbumContentAdapter extends BaseAdapter {

    private final Context context;
    private final List<AlbumContentModel> imageList;

    public AlbumContentAdapter(Context context, List<AlbumContentModel> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_album_content, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = convertView.findViewById(R.id.album_content_image_view);
            viewHolder.fileNameTextView = convertView.findViewById(R.id.directory_name_text_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        AlbumContentModel AlbumContentModel = imageList.get(position);

        Glide.with(context)
                .load(AlbumContentModel.getFilePath())
                .placeholder(R.drawable.placeholder_icon)
                .error(R.drawable.error_image_icon)
                .centerCrop()
                .into(viewHolder.imageView);

        viewHolder.fileNameTextView.setText(AlbumContentModel.getFileName());

        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView fileNameTextView;
    }
}
