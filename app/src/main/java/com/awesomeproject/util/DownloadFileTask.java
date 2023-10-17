package com.awesomeproject.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Arrays;

public class DownloadFileTask {

    public static void downloadFileToAssets(Context context, String urlString, String fileName) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String TAG = "downloadFileToAssets";
                Log.w(TAG, "downFileStart");

                try {
                    // 创建URL对象
                    URL url = new URL(urlString);

                    // 打开连接
                    URLConnection connection = url.openConnection();
                    connection.connect();
                    // 获取输入流
                    InputStream inputStream = connection.getInputStream();


                    // 创建文件输出流
                    File file = new File(context.getFilesDir(), fileName);
                    if(!file.exists()){
                        File parent = new File(file.getParent());
                        parent.mkdirs();
                        file.createNewFile();
                    }
                    OutputStream outputStream = new FileOutputStream(file);
                    // 缓冲区
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        Log.w(TAG, "downFile: " + bytesRead);
                        outputStream.write(buffer, 0, bytesRead);
                    }

                    // 关闭流
                    inputStream.close();
                    outputStream.close();

                } catch (IOException e) {
                    Log.w(TAG, "downFileError");

                    e.printStackTrace();
                }
            }

        });

        thread.start();


    }


}