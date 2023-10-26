package com.awesomeproject.service.jsBundle.dto;

public class JsBundleInfo {

    public Boolean lazy;
    public String bundleFilePath;

    public JsBundleVersionMetaData metaData;

    @Override
    public String toString() {
        return "JsBundleInfo{" +
                "lazy=" + lazy +
                ", bundleFilePath='" + bundleFilePath + '\'' +
                ", metaData=" + metaData +
                '}';
    }
}
