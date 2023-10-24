package com.awesomeproject.service.JsBundle;

import android.content.Context;
import android.util.Log;

import com.awesomeproject.service.JsBundle.dto.CheckLastVersionReq;
import com.awesomeproject.service.JsBundle.dto.CheckLastVersionRes;
import com.awesomeproject.service.JsBundle.dto.JsBundleInfo;
import com.awesomeproject.service.JsBundle.dto.JsBundleMetaData;
import com.awesomeproject.service.JsBundle.dto.JsBundleVersionMetaData;
import com.awesomeproject.service.JsBundle.dto.PullLastBundleReq;
import com.google.gson.Gson;

import org.apache.commons.io.FileUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JsBundleManager {

    String TAG = "JsBundleManage";

    private final Context context;
    private final Gson gson = new Gson();
    private final String bundleName;
    private String jsBundleDirPath;
    private String jsBundleMetaDataFilePath;
    private String bundleServerBaseUrl = "http://103.144.3.110:8080/api/bundle";

    public JsBundleManager(Context context, String bundleName) {
        this.context = context;
        // bundle 名称
        this.bundleName = bundleName;
        // jsBundle 文件存放路径
        this.jsBundleDirPath = context.getFilesDir() + "/jsBundle/" + bundleName;
        // 本地meta.json 存放路径
        this.jsBundleMetaDataFilePath = this.jsBundleDirPath + "/meta.json";
    }

    /**
     * 更新本地bundle
     */
    public void updateLocal() {
        this.updateLocal(null);
    }

    public void updateLocal(CallBackFunction callback) {
        new Thread(() -> {
            try {
                JsBundleMetaData jsBundleMetaData = this.getMetaData();
                CheckLastVersionRes.Data checkLastVersionRes = this.getLastVersionInfo();

                if (checkLastVersionRes.name.equals(jsBundleMetaData.name)) {
                    if (!checkLastVersionRes.version.equals(jsBundleMetaData.lastVersion)) {
                        this.syncBundle(checkLastVersionRes);
                        if (callback != null) {
                            JsBundleInfo newJsBundleInfo = this.verifyLocalMetaData(this.getMetaData());
                            if (newJsBundleInfo != null) {
                                callback.apply(newJsBundleInfo);
                            }
                        }

                    }
                }
            } catch (Exception e) {
                String message = "更新bundle失败";
                Log.e(TAG, message + "Exception occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    private void syncBundle(CheckLastVersionRes.Data checkLastVersionRes) {
        try {
            String outputDir = this.jsBundleDirPath + "/" + checkLastVersionRes.version; // 解压后的输出

            URL url = new URL(this.bundleServerBaseUrl + "/pullLastBundle");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());

            PullLastBundleReq reqData = new PullLastBundleReq(this.bundleName);

            String jsonString = gson.toJson(reqData);

            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                File outputDirFile = new File(outputDir);
                if (outputDirFile.exists()) FileUtils.deleteQuietly(outputDirFile);

                BufferedInputStream in = new BufferedInputStream(conn.getInputStream());

                // ZIP解压
                ZipInputStream zipInputStream = new ZipInputStream(in);
                ZipEntry zipEntry = null;

                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    String entryName = zipEntry.getName();
                    String filePath = outputDir + "/" + entryName;

                    if (zipEntry.isDirectory()) {
                        // 创建目录
                        new File(filePath).mkdirs();
                    } else {
                        // 创建文件目录
                        new File(filePath).getParentFile().mkdirs();

                        // 缓冲输出流
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(filePath));

                        // 写入文件
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = zipInputStream.read(buffer)) > 0) {
                            bufferedOutputStream.write(buffer, 0, length);
                        }

                        // 关闭输出流
                        bufferedOutputStream.close();
                    }

                    // 关闭ZIP条目
                    zipInputStream.closeEntry();
                }
                // 关闭ZIP输入流
                zipInputStream.close();
                // 断开连接
                conn.disconnect();

                JsBundleMetaData jsBundleMetaData = new JsBundleMetaData();
                jsBundleMetaData.name = checkLastVersionRes.name;
                jsBundleMetaData.lastVersion = checkLastVersionRes.version;

                File metaDataFile = new File(this.jsBundleMetaDataFilePath);

                FileOutputStream fos = new FileOutputStream(metaDataFile);

                String metaDataStr = gson.toJson(jsBundleMetaData);

                fos.write(metaDataStr.getBytes());
                fos.close();

            } else {
                throw new ConnectException("接口请求失败");
            }
        } catch (Exception e) {
            String message = "下载文件失败";
            Log.e(TAG, message + "Exception occurred: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(message);
        }

    }

    public JsBundleInfo verifyLocalMetaData(JsBundleMetaData jsBundleMetaData) {
        if (!this.bundleName.equals(jsBundleMetaData.name)) return null;
        File versionMeta = new File(this.jsBundleDirPath + "/" + jsBundleMetaData.lastVersion + "/meta.json");
        File bundle = new File(this.jsBundleDirPath + "/" + jsBundleMetaData.lastVersion + "/bundle/index.bundle");
        if (!versionMeta.exists() || !bundle.exists()) {
            return null;
        }
        JsBundleInfo jsBundleInfo = new JsBundleInfo();
        jsBundleInfo.bundleFilePath = bundle.toString();
        try {

            FileInputStream fis = new FileInputStream(versionMeta);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            String inputLine;
            StringBuilder metaDataStr = new StringBuilder();

            while ((inputLine = reader.readLine()) != null) {
                metaDataStr.append(inputLine);
            }

            jsBundleInfo.metaData = gson.fromJson(metaDataStr.toString(), JsBundleVersionMetaData.class);

            return jsBundleInfo;
        } catch (Exception e) {
            String message = "验证 version meta.json失败";
            Log.e(TAG, message + "Exception occurred: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(message);
        }

    }


    public JsBundleMetaData getMetaData() throws RuntimeException {
        try {
            File metaDataFile = new File(this.jsBundleMetaDataFilePath);

            if (metaDataFile.exists()) {
                FileInputStream fis = new FileInputStream(metaDataFile);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

                String inputLine;
                StringBuilder metaDataStr = new StringBuilder();

                while ((inputLine = reader.readLine()) != null) {
                    metaDataStr.append(inputLine);
                }

                reader.close();
                return gson.fromJson(metaDataStr.toString(), JsBundleMetaData.class);

            } else {
                metaDataFile.getParentFile().mkdirs();
                metaDataFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(metaDataFile);

                JsBundleMetaData jsBundleMetaData = new JsBundleMetaData();
                jsBundleMetaData.name = this.bundleName;
                jsBundleMetaData.lastVersion = "";
                String metaDataStr = gson.toJson(jsBundleMetaData);

                fos.write(metaDataStr.getBytes());
                fos.close();
                return jsBundleMetaData;
            }
        } catch (Exception e) {
            String message = "获取本地元数据失败";
            Log.e(TAG, message + "Exception occurred: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(message);

        }
    }


    public CheckLastVersionRes.Data getLastVersionInfo() throws RuntimeException {
        try {
            URL url = new URL(this.bundleServerBaseUrl + "/checkLastVersion");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());

            CheckLastVersionReq reqData = new CheckLastVersionReq(this.bundleName);

            String jsonString = gson.toJson(reqData);

            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                // 断开连接
                conn.disconnect();
                // 打印结果
                CheckLastVersionRes bundleInfo = gson.fromJson(response.toString(), CheckLastVersionRes.class);

                if (bundleInfo.code == 0) {
                    return bundleInfo.data;
                } else {
                    throw new ConnectException("接口请求失败");
                }
            } else {
                throw new ConnectException("接口请求失败");
            }

        } catch (Exception e) {
            String message = "获取获取远程数据失败";
            Log.e(TAG, message + "Exception occurred: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(message);
        }
    }
}
