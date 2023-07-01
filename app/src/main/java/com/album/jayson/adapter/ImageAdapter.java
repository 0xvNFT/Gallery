package com.album.jayson.adapter;

import android.annotation.SuppressLint;
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

import com.album.jayson.FavoritesManager;
import com.album.jayson.R;
import com.album.jayson.model.ImageModel;
import com.bumptech.glide.Glide;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final List<ImageModel> imageList;
    private final FragmentType fragmentType;
    private OnImageClickListener onImageClickListener;

    public ImageAdapter(Context context, List<ImageModel> imageList, FragmentType fragmentType) {
        this.context = context;
        this.imageList = imageList;
        this.fragmentType = fragmentType;
        new FavoritesManager(context);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<ImageModel> updatedImageList) {
        imageList.clear();
        imageList.addAll(updatedImageList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = null;
        switch (fragmentType) {
            case YEAR:
                itemView = inflater.inflate(R.layout.item_image_year, parent, false);
                return new YearViewHolder(itemView);
            case MONTH:
                itemView = inflater.inflate(R.layout.item_image_month, parent, false);
                return new MonthViewHolder(itemView);
            case DAY:
                itemView = inflater.inflate(R.layout.item_image_day, parent, false);
                return new DayViewHolder(itemView);
            case FAVORITE:
                itemView = inflater.inflate(R.layout.item_favorite_image, parent, false);
                return new FavoriteViewHolder(itemView);
            case ALL_PHOTOS:
                itemView = inflater.inflate(R.layout.item_image_all_photos, parent, false);
                return new AllPhotosViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ImageModel image = imageList.get(position);
        switch (fragmentType) {
            case YEAR:
                ((YearViewHolder) holder).bind(image);
                break;
            case MONTH:
                ((MonthViewHolder) holder).bind(image);
                break;
            case DAY:
                ((DayViewHolder) holder).bind(image);
                break;
            case FAVORITE:
                ((FavoriteViewHolder) holder).bind(image);
                break;
            case ALL_PHOTOS:
                ((AllPhotosViewHolder) holder).bind(image);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public void setOnImageClickListener(OnImageClickListener listener) {
        this.onImageClickListener = listener;
    }

    public enum FragmentType {
        YEAR,
        MONTH,
        DAY,
        FAVORITE,
        ALL_PHOTOS
    }


    public interface OnImageClickListener {
        void onImageClick(String imagePath);
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final TextView dateTextView;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            dateTextView = itemView.findViewById(R.id.date_text_view);

            itemView.setOnClickListener(v -> {
                ImageModel image = imageList.get(getAdapterPosition());
                if (image.isVideo()) {
                    playVideo(image.getPath());
                } else {
                    if (onImageClickListener != null) {
                        onImageClickListener.onImageClick(image.getPath());
                    }
                }
            });
        }

        public void bind(ImageModel image) {
            if (image.isVideo()) {
                Glide.with(context)
                        .load(R.drawable.video_placeholder)
                        .into(imageView);
            } else {
                Glide.with(context)
                        .load(image.getPath())
                        .placeholder(R.drawable.placeholder_icon)
                        .error(R.drawable.error_image_icon)
                        .into(imageView);
            }

            dateTextView.setText(image.getDate());
        }

        private void playVideo(String videoPath) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(videoPath), "video/*");
            context.startActivity(intent);
        }
    }

    public class YearViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final TextView dateTextView;

        public YearViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            dateTextView = itemView.findViewById(R.id.date_text_view);

            itemView.setOnClickListener(v -> {
                if (onImageClickListener != null) {
                    ImageModel image = imageList.get(getAdapterPosition());
                    onImageClickListener.onImageClick(image.getPath());
                }
            });
        }

        public void bind(ImageModel image) {
            if (image.isVideo()) {
                Glide.with(context)
                        .load(R.drawable.video_placeholder)
                        .into(imageView);
            } else {
                Glide.with(context)
                        .load(image.getPath())
                        .placeholder(R.drawable.placeholder_icon)
                        .error(R.drawable.error_image_icon)
                        .into(imageView);
            }

            dateTextView.setText(image.getDate());
        }
    }

    public class MonthViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final TextView dateTextView;

        public MonthViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            dateTextView = itemView.findViewById(R.id.date_text_view);

            itemView.setOnClickListener(v -> {
                if (onImageClickListener != null) {
                    ImageModel image = imageList.get(getAdapterPosition());
                    onImageClickListener.onImageClick(image.getPath());
                }
            });
        }

        public void bind(ImageModel image) {
            if (image.isVideo()) {
                Glide.with(context)
                        .load(R.drawable.video_placeholder)
                        .into(imageView);
            } else {
                Glide.with(context)
                        .load(image.getPath())
                        .placeholder(R.drawable.placeholder_icon)
                        .error(R.drawable.error_image_icon)
                        .into(imageView);
            }

            dateTextView.setText(image.getDate());
        }
    }

    public class DayViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final TextView dateTextView;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            dateTextView = itemView.findViewById(R.id.date_text_view);

            itemView.setOnClickListener(v -> {
                if (onImageClickListener != null) {
                    ImageModel image = imageList.get(getAdapterPosition());
                    onImageClickListener.onImageClick(image.getPath());
                }
            });
        }

        public void bind(ImageModel image) {
            if (image.isVideo()) {
                Glide.with(context)
                        .load(R.drawable.video_placeholder)
                        .into(imageView);
            } else {
                Glide.with(context)
                        .load(image.getPath())
                        .placeholder(R.drawable.placeholder_icon)
                        .error(R.drawable.error_image_icon)
                        .into(imageView);
            }

            dateTextView.setText(image.getDate());
        }
    }

    public class AllPhotosViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final TextView dateTextView;

        public AllPhotosViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            dateTextView = itemView.findViewById(R.id.date_text_view);

            itemView.setOnClickListener(v -> {
                if (onImageClickListener != null) {
                    ImageModel image = imageList.get(getAdapterPosition());
                    onImageClickListener.onImageClick(image.getPath());
                }
            });
        }

        public void bind(ImageModel image) {
            if (image.isVideo()) {
                Glide.with(context)
                        .load(R.drawable.video_placeholder)
                        .into(imageView);
            } else {
                Glide.with(context)
                        .load(image.getPath())
                        .placeholder(R.drawable.placeholder_icon)
                        .error(R.drawable.error_image_icon)
                        .into(imageView);
            }

            dateTextView.setText(image.getDate());
        }
    }

}
