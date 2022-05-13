package com.example.appfoodyyy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnQuanAn, btnThucKhach;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnQuanAn = (Button) findViewById(R.id.btnLoginQuanAn);
        btnQuanAn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpUserActivity.class);
                startActivity(intent);
            }
        });
        btnThucKhach = (Button) findViewById(R.id.btnLoginUser);
        btnThucKhach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpUserActivity.class);
                startActivity(intent);
            }
        });

    }
}