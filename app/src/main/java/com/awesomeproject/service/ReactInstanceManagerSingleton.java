package com.awesomeproject.service;

import android.app.Activity;
import android.app.Application;

import com.awesomeproject.BuildConfig;
import com.facebook.react.PackageList;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JSBundleLoader;
import com.facebook.react.common.LifecycleState;
import com.facebook.soloader.SoLoader;

import java.util.List;

public class ReactInstanceManagerSingleton {
    private static ReactInstanceManagerSingleton singletonHolder;

    private ReactInstanceManagerSingleton(Application application, Activity activity, JSBundleLoader jsBundleLoader) {
        SoLoader.init(activity, false);

        List<ReactPackage> packages = new PackageList(application).getPackages();

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

    public static ReactInstanceManagerSingleton getInstance(Application application, Activity activity, JSBundleLoader jsBundleLoader) {
        if (singletonHolder == null) {
            singletonHolder = new ReactInstanceManagerSingleton(application, activity, jsBundleLoader);
        }

        return singletonHolder;
    }
}
