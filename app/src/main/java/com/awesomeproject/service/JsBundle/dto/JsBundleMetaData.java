package com.awesomeproject.service.JsBundle.dto;

public class JsBundleMetaData {
    public String name;
    public String lastVersion;

    @Override
    public String toString() {
        return "JsBundleMetaData{" +
                "name='" + name + '\'' +
                ", lastVersion='" + lastVersion + '\'' +
                '}';
    }
}
