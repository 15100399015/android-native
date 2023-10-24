package com.awesomeproject.service;

import android.app.Activity;
import android.app.Application;

import com.awesomeproject.BuildConfig;
import com.awesomeproject.service.JsBundle.dto.JsBundleInfo;
import com.facebook.react.PackageList;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JSBundleLoader;
import com.facebook.react.common.LifecycleState;
import com.facebook.soloader.SoLoader;

import java.util.List;

public class Rim {
    private static Rim singletonHolder;

    private Rim(Application application, Activity activity, CustomJSBundleLoader jsBundleLoader) {
        SoLoader.init(application, false);

        List<ReactPackage> packages = new PackageList(application).getPackages();
        this.customJSBundleLoader = jsBundleLoader;
        this.manager = ReactInstanceManager
                .builder()
                .setApplication(application)
                .setCurrentActivity(activity)
                .setJSMainModulePath("index")
                .setJSBundleLoader(jsBundleLoader)
                .addPackages(packages)
                .setUseDeveloperSupport(BuildConfig.DEBUG)
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build();


    }

    public final ReactInstanceManager manager;
    private final CustomJSBundleLoader customJSBundleLoader;

    public void attachActivity(Activity activity) {
        customJSBundleLoader.currentActivity = activity;
    }

    public static Rim getInstance(Application application, Activity activity, CustomJSBundleLoader jsBundleLoader) {
        if (singletonHolder == null) {
            singletonHolder = new Rim(application, activity, jsBundleLoader);
        }

        return singletonHolder;
    }
}
