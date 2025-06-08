package com.example.homework5;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lvImage;
    Button btnAdd;
    CustomAdapterLV customAdapterLV;
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
        photoHandler = new PhotoHandler(MainActivity.this, PhotoHandler.DB_NAME, null, PhotoHandler.DB_VERSION);
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
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddImage.class);
                startActivity(intent);
            }
        });
//        lvImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Photo photo = photoArrayList.get(position);
//                photoArrayList.remove(photo);
//                customAdapterLV.notifyDataSetChanged();
//            }
//        });
    }
    void loadDataLV(){
        photoArrayList = photoHandler.getDatas();
        customAdapterLV = new CustomAdapterLV(MainActivity.this, R.layout.layout_customadapter,photoArrayList);
        lvImage.setAdapter(customAdapterLV);
    }

}