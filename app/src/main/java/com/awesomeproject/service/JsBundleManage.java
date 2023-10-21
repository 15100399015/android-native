package com.awesomeproject.service;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import com.awesomeproject.container.ReactNativeContainer;
import com.awesomeproject.util.Utils;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

public class JsBundleManage {

    String TAG = "JsBundleManage";

    private final Context context;
    private final String bundleName;

    private String jsBundleDirPath = "jsBundle";

    public JsBundleManage(Context context, String bundleName) {
        this.context = context;
        this.bundleName = bundleName;
    }

    /**
     * 更新本地bundle
     */
    public void updateLocal() {

    }

    public void getLastVersionInfo() {

        new Thread(() -> {
            try {
                URL url = new URL("http://103.144.3.110:8080/api/bundle/checkLastVersion");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.connect();

                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                Gson gson = new Gson();

                Log.d(TAG, "gson：" + gson);

                class ReqData {
                    public String name;

                    public ReqData(String bundleName) {
                        this.name = bundleName;
                    }

                }
                ReqData reqData = new ReqData("asdasdsa");
                Log.d(TAG, "格式化之前是：" + reqData);
                Log.d(TAG, "格式化之前是：" + reqData.name);
                String jsonString = gson.toJson(reqData);
                Log.d(TAG, "格式化数据是：" + jsonString);

//
//                wr.writeBytes(jsonString);
//                wr.flush();
//                wr.close();
//
//                int responseCode = conn.getResponseCode();
//                if (responseCode == HttpURLConnection.HTTP_OK) {
//                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                    String inputLine;
//                    StringBuilder response = new StringBuilder();
//
//                    while ((inputLine = in.readLine()) != null) {
//                        response.append(inputLine);
//                    }
//                    in.close();
//                    // 断开连接
//                    conn.disconnect();
//                    // 打印结果
//                    Log.d(TAG, response.toString());

//                } else {
//                    Log.e(TAG, String.valueOf(responseCode));
//                }

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }).start();
    }
}
