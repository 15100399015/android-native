package com.awesomeproject.service.jsBundle;


import com.awesomeproject.service.jsBundle.dto.JsBundleInfo;

@FunctionalInterface
public interface CallBackFunction {
    void apply(JsBundleInfo jsBundleInfo);
}
