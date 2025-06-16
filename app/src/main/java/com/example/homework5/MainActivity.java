package com.example.homework5;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView lvImage;
    Button btnAdd;
    CustomAdapterRV customAdapterRV;
    ArrayList<Photo> photoArrayList = new ArrayList<>();
    PhotoHandler photoHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addControl();
        photoHandler = new PhotoHandler(this, PhotoHandler.DB_NAME, null, PhotoHandler.DB_VERSION);
        loadDataLV();
        addEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataLV();
    }

    private void addControl() {
        lvImage = findViewById(R.id.lvImage);
        btnAdd = findViewById(R.id.btnAdd);
    }

    private void addEvent() {
        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddImage.class);
            startActivity(intent);
        });
    }

    private void loadDataLV() {
        photoArrayList = photoHandler.getDatas();
        customAdapterRV = new CustomAdapterRV(this, photoArrayList);

        lvImage.setLayoutManager(new LinearLayoutManager(this));
        lvImage.setAdapter(customAdapterRV);

        // Swipe to delete
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                try {
                    Photo photo = customAdapterRV.getItem(position);
                    Log.d("SWIPE_DELETE", "Đang xử lý vuốt tại vị trí: " + position + ", ID: " + photo.getId());

                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Xác nhận xóa")
                            .setMessage("Bạn có chắc muốn xóa ảnh này?")
                            .setPositiveButton("Có", (dialog, which) -> {
                                try {
                                    photoHandler.deletePhoto(photo.getId());
                                    customAdapterRV.removeItem(position);
                                    Log.d("SWIPE_DELETE", "Đã xóa ảnh ID: " + photo.getId());
                                    Toast.makeText(MainActivity.this, "Đã xóa", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Log.e("SWIPE_DELETE", "Lỗi khi xóa ảnh: " + e.getMessage());
                                    Toast.makeText(MainActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Không", (dialog, which) -> {
                                customAdapterRV.notifyItemChanged(position);
                                Log.d("SWIPE_DELETE", "Hủy xóa ảnh ở vị trí: " + position);
                            })
                            .setCancelable(false)
                            .show();

                } catch (Exception e) {
                    Log.e("SWIPE_DELETE", "Lỗi khi xử lý vuốt: " + e.getMessage(), e);
                    Toast.makeText(MainActivity.this, "Đã xảy ra lỗi!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(lvImage);
    }
}
