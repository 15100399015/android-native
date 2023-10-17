package com.awesomeproject.service;

import android.content.Context;
import android.provider.ContactsContract;

import com.awesomeproject.container.ReactNativeContainer;
import com.awesomeproject.util.Utils;
import com.google.gson.Gson;

import java.io.InputStream;
import java.util.Date;

public class JsBundleManage {
// 更新 bundle 版本
// 获取 bundle 版本
// 获取当前可用 bundle 路径
    private final Context context;
public JsBundleManage(Context context) {
    this.context = context;
}
    private class BundleMetaDto {
        String version;
        String name;
        Date upDateTime;

    }

    // bundle base 路径
    public String jsBundlePath = "jsBundle";


    public void  updateJsBundle(String bundleName){
//        调用接口 返回最新版本元数据，
//        对比metadata判断是否需要更新
//        下载最新bundle，并更新 metadata

    }
    public  void getUsableJsBundlePath(String bundleName) {
//        读取 metadata 文件，确定当前可用最新包
//        对bundle进行检查后，返回最新 bundle 地址




    }
}
