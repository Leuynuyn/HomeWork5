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

import java.util.ArrayList;

public class CustomAdapterLV extends ArrayAdapter {
    Context context;
    int layoutItem;
    ArrayList<Photo> photoArrayList = new ArrayList<>();


    public CustomAdapterLV(@NonNull Context context, int layoutItem, @NonNull ArrayList<Photo> photoArrayList) {
        super(context, layoutItem, photoArrayList);
        this.context = context;
        this.layoutItem = layoutItem;
        this.photoArrayList = photoArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Photo photo = photoArrayList.get(position);
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(layoutItem,null);
        }
        TextView tvName =convertView.findViewById(R.id.tvName);
        TextView tvDescription = convertView.findViewById(R.id.tvDescription);
        ImageView imgPhoto = convertView.findViewById(R.id.img);

        tvName.setText(photo.getName());
        tvDescription.setText(photo.getDescription());
        byte[] imgByte = photo.getImage();
        if(imgByte != null && imgByte.length > 0){
            Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
            imgPhoto.setImageBitmap(bitmap);
        }else{
            imgPhoto.setImageResource(R.drawable.ic_launcher_background);
        }

        //xóa
        convertView.setOnLongClickListener(view -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa mục này không?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Gọi DB để xóa
                        PhotoHandler handler = new PhotoHandler(context, null, null, 1);
                        handler.deletePhoto(photo.getId());

                        // Xóa khỏi danh sách và cập nhật giao diện
                        photoArrayList.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Đã xóa", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();

            return true;
        });

        return convertView;
    }
}
