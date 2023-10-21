package com.awesomeproject.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.awesomeproject.R;
import com.awesomeproject.service.JsBundleManage;

public class TestActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        JsBundleManage jsBundleManage = new JsBundleManage(TestActivity.this, "AwesomeProject");
        jsBundleManage.getLastVersionInfo();

    }

}
