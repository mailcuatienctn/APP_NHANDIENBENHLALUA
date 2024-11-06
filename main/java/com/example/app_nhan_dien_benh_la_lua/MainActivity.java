package com.example.app_nhan_dien_benh_la_lua;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    ImageView img_lb, img_cr, img_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        img_lb = findViewById(R.id.img_lb);
        img_cr = findViewById(R.id.img_cr);
        img_chat = findViewById(R.id.img_chat);

        img_lb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Activity_Select_Library.class);
                startActivity(i);
            }
        });

        img_cr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Activity_Select_Camera.class);
                startActivity(i);
            }
        });

        img_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Activity_Sign_In.class);
                startActivity(i);
            }
        });

    }
}