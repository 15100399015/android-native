package com.awesomeproject.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.awesomeproject.R;
import com.awesomeproject.util.DownloadFileTask;

public class TestActivity extends AppCompatActivity {

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test);


    DownloadFileTask.downloadFileToAssets(TestActivity.this,
            "https://liangx-gallery.oss-cn-beijing.aliyuncs.com/jsBundle/AwesomeProject/0.0.26.zip",
            "AwesomeProject/0.0.26.zip");

  }

}
