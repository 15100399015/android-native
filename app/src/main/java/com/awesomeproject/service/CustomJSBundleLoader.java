package com.awesomeproject.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.awesomeproject.service.JsBundle.JsBundleManager;
import com.awesomeproject.service.JsBundle.dto.JsBundleInfo;
import com.facebook.react.bridge.JSBundleLoader;
import com.facebook.react.bridge.JSBundleLoaderDelegate;

public class CustomJSBundleLoader extends JSBundleLoader {

    private Context context;
    public Activity currentActivity;

    public CustomJSBundleLoader(Context context) {
        this.context = context;
    }


    @Override
    public String loadScript(JSBundleLoaderDelegate jsBundleLoaderDelegate) {

        Intent intent = currentActivity.getIntent();
        Uri data = intent.getData();
        String bundleName = data.getQueryParameter("bundle");

        JsBundleManager jsBundleManager = new JsBundleManager(context, bundleName);
        // 本地是否有可用bundle
        JsBundleInfo jsBundleInfoVerify = jsBundleManager.verifyLocalMetaData(jsBundleManager.getMetaData());
        String mScriptUrl = jsBundleInfoVerify.bundleFilePath;
        jsBundleLoaderDelegate.loadScriptFromFile(mScriptUrl, mScriptUrl, false);

        return mScriptUrl;
    }
}