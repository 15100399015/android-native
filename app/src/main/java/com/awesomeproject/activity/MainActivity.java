package com.awesomeproject.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.awesomeproject.R;
import com.awesomeproject.view.LoadingView;

public class MainActivity extends AppCompatActivity {
    private Button homeBtn;
    private Button privateBtn;
    private Button lotteryBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        homeBtn = findViewById(R.id.home_btn);
        lotteryBtn = findViewById(R.id.lottery_btn);
        privateBtn = findViewById(R.id.private_btn);
        homeBtn.setOnClickListener((view) -> {
            String url = "zymobi://liangx:8080/rn?bundle=homePage";
            Uri uri = Uri.parse(url);

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);

        });
        lotteryBtn.setOnClickListener((view) -> {
            String url = "zymobi://liangx:8080/web";
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);

        });
        privateBtn.setOnClickListener((view) -> {
            String url = "zymobi://liangx:8080/rn?bundle=privatePage";
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);

        });


    }

}
