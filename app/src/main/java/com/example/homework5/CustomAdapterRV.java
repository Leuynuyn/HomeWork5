package com.example.homework5;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapterRV extends RecyclerView.Adapter<CustomAdapterRV.PhotoViewHolder> {

    Context context;
    ArrayList<Photo> photoList;

    public CustomAdapterRV(Context context, ArrayList<Photo> photoList) {
        this.context = context;
        this.photoList = photoList;
    }

    @NonNull
    @Override
    public CustomAdapterRV.PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_customadapter, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapterRV.PhotoViewHolder holder, int position) {
        Photo photo = photoList.get(position);
        holder.tvName.setText(photo.getName());
        holder.tvDescription.setText(photo.getDescription());

        byte[] imgByte = photo.getImage();
        if (imgByte != null && imgByte.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
            holder.imgPhoto.setImageBitmap(bitmap);
        } else {
            holder.imgPhoto.setImageResource(R.drawable.ic_launcher_background); // fallback image
        }
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public Photo getItem(int position) {
        return photoList.get(position);
    }

    public void removeItem(int position) {
        Photo photo = photoList.get(position);
        // Xóa trong database
        PhotoHandler handler = new PhotoHandler(context, PhotoHandler.DB_NAME, null, PhotoHandler.DB_VERSION);
        handler.deletePhoto(photo.getId());

        // Xóa trong danh sách
        photoList.remove(position);
        notifyItemRemoved(position);
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDescription;
        ImageView imgPhoto;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            imgPhoto = itemView.findViewById(R.id.img);
        }
    }

//    public CustomAdapterRV(@NonNull Context context, int layoutItem, @NonNull ArrayList<Photo> photoArrayList) {
//        super(context, layoutItem, photoArrayList);
//        this.context = context;
//        this.layoutItem = layoutItem;
//        this.photoArrayList = photoArrayList;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        Photo photo = photoArrayList.get(position);
//        if(convertView == null){
//            convertView = LayoutInflater.from(context).inflate(layoutItem,null);
//        }
//        TextView tvName =convertView.findViewById(R.id.tvName);
//        TextView tvDescription = convertView.findViewById(R.id.tvDescription);
//        ImageView imgPhoto = convertView.findViewById(R.id.img);
//
//        tvName.setText(photo.getName());
//        tvDescription.setText(photo.getDescription());
//        byte[] imgByte = photo.getImage();
//        if(imgByte != null && imgByte.length > 0){
//            Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
//            imgPhoto.setImageBitmap(bitmap);
//        }else{
//            imgPhoto.setImageResource(R.drawable.ic_launcher_background);
//        }
//
//        //xóa
//        convertView.setOnLongClickListener(view -> {
//            new AlertDialog.Builder(context)
//                    .setTitle("Xác nhận xóa")
//                    .setMessage("Bạn có chắc chắn muốn xóa mục này không?")
//                    .setPositiveButton("Yes", (dialog, which) -> {
//                        // Gọi DB để xóa
//                        PhotoHandler handler = new PhotoHandler(context, null, null, 1);
//                        handler.deletePhoto(photo.getId());
//
//                        // Xóa khỏi danh sách và cập nhật giao diện
//                        photoArrayList.remove(position);
//                        notifyDataSetChanged();
//                        Toast.makeText(context, "Đã xóa", Toast.LENGTH_SHORT).show();
//                    })
//                    .setNegativeButton("No", null)
//                    .show();
//
//            return true;
//        });
//
//        return convertView;
//    }
}
