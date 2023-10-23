package com.awesomeproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.awesomeproject.ReactNativeFlipper;
import com.awesomeproject.service.CustomJSBundleLoader;
import com.awesomeproject.service.JsBundle.JsBundleManage;
import com.awesomeproject.service.JsBundle.dto.JsBundleInfo;
import com.awesomeproject.service.ReactInstanceManagerSingleton;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;


public class ReactNativeActivity extends Activity implements DefaultHardwareBackBtnHandler {
    private ReactRootView mReactRootView;
    private ReactInstanceManager mReactInstanceManager;

    private final int OVERLAY_PERMISSION_REQ_CODE = 1;  // 任写一个值

    private String TAG = "ReactNativeActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Uri data = intent.getData();
        String bundleName = data.getQueryParameter("bundle");
        Toast toast = Toast.makeText(this, bundleName, Toast.LENGTH_LONG);
        toast.show();

        // 拿到 manager
        mReactInstanceManager = ReactInstanceManagerSingleton.getInstance(getApplication(), this, new CustomJSBundleLoader(ReactNativeActivity.this)).manager;

        JsBundleManage jsBundleManage = new JsBundleManage(ReactNativeActivity.this, bundleName);
        // 本地是否有可用bundle
        JsBundleInfo jsBundleInfoVerify = jsBundleManage.verifyLocalMetaData(jsBundleManage.getMetaData());
        // 没有就去加载
        // 如果验证失败，咋更新后重载，否则直接初始化并尝试更新bundle
        if (jsBundleInfoVerify == null) {
            jsBundleManage.updateLocal((jsBundleInfo) -> {
                this.runOnUiThread(() -> {
                    this.startReactNative(jsBundleInfo);
                });
            });
        } else {
            this.startReactNative(jsBundleInfoVerify);
            jsBundleManage.updateLocal();
        }

    }

    private void startReactNative(JsBundleInfo jsBundleInfo) {
        try {
            mReactRootView = new ReactRootView(this);

            mReactInstanceManager.attachRootView(mReactRootView);

            mReactRootView.startReactApplication(mReactInstanceManager, "AwesomeProject", null);

            setContentView(mReactRootView);

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

        if (mReactRootView != null) {
            mReactRootView.unmountReactApplication();
        }
        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostDestroy(this);
            mReactInstanceManager.detachRootView(mReactRootView);
            mReactInstanceManager.destroy();
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
