package com.awesomeproject.service.JsBundle;


import com.awesomeproject.service.JsBundle.dto.JsBundleInfo;

@FunctionalInterface
public interface CallBackFunction {
    void apply(JsBundleInfo jsBundleInfo);
}
