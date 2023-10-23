package com.awesomeproject.service;

import android.content.Context;

import com.awesomeproject.activity.ReactNativeActivity;
import com.awesomeproject.service.JsBundle.JsBundleManage;
import com.awesomeproject.service.JsBundle.dto.JsBundleInfo;
import com.facebook.react.bridge.JSBundleLoader;
import com.facebook.react.bridge.JSBundleLoaderDelegate;

public class CustomJSBundleLoader extends JSBundleLoader {

    private Context context;

    public CustomJSBundleLoader(Context context) {
        this.context = context;
    }


    @Override
    public String loadScript(JSBundleLoaderDelegate jsBundleLoaderDelegate) {
        JsBundleManage jsBundleManage = new JsBundleManage(context, "AwesomeProject");
        // 本地是否有可用bundle
        JsBundleInfo jsBundleInfoVerify = jsBundleManage.verifyLocalMetaData(jsBundleManage.getMetaData());
        String mScriptUrl = jsBundleInfoVerify.bundleFilePath;
        jsBundleLoaderDelegate.loadScriptFromFile(mScriptUrl, mScriptUrl, false);

        return mScriptUrl;
    }
}