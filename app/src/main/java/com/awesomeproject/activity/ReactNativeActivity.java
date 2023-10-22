package com.awesomeproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.awesomeproject.BuildConfig;
import com.awesomeproject.ReactNativeFlipper;
import com.awesomeproject.service.JsBundle.JsBundleManage;
import com.awesomeproject.service.JsBundle.dto.JsBundleInfo;
import com.facebook.react.PackageList;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactPackage;
import com.facebook.react.ReactRootView;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.soloader.SoLoader;

import java.io.File;
import java.util.List;

public class ReactNativeActivity extends Activity implements DefaultHardwareBackBtnHandler {
    private ReactRootView mReactRootView;
    private ReactInstanceManager mReactInstanceManager;

    private final int OVERLAY_PERMISSION_REQ_CODE = 1;  // 任写一个值

    private String TAG = "ReactNativeActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JsBundleManage jsBundleManage = new JsBundleManage(ReactNativeActivity.this, "AwesomeProject");
        jsBundleManage.updateLocal((jsBundleInfo) -> {
            this.runOnUiThread(() -> {
                if (jsBundleInfo.lazy) {
                    this.recreate();
                } else {
                    this.startReactNative(jsBundleInfo);
                }
            });
        });
    }

    private void startReactNative(JsBundleInfo jsBundleInfo) {
        SoLoader.init(this, false);

        mReactRootView = new ReactRootView(this);

        setContentView(mReactRootView);

        List<ReactPackage> packages = new PackageList(getApplication()).getPackages();

        mReactInstanceManager = ReactInstanceManager
                .builder()
                .setApplication(getApplication())
                .setCurrentActivity(this)
                .setJSMainModulePath("index")
                .setJSBundleFile(jsBundleInfo.bundleFilePath)
                .addPackages(packages)
                .setUseDeveloperSupport(BuildConfig.DEBUG)
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build();

        mReactRootView.startReactApplication(mReactInstanceManager, "AwesomeProject", null);
        onResume();

        try {
            ReactNativeFlipper.initializeFlipper(ReactNativeActivity.this, mReactInstanceManager);
        } catch (Exception e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostResume(this, this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostDestroy(this);
        }
        if (mReactRootView != null) {
            mReactRootView.unmountReactApplication();
        }
    }

    @Override
    public void onBackPressed() {
        if (mReactInstanceManager != null) {
            mReactInstanceManager.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostPause(this);
        }
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        super.onBackPressed();
    }

}
