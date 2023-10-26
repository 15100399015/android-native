package com.awesomeproject.service.JsBundle.dto;

public class JsBundleVersionMetaData {
    public String name;
    public String version;
    public String description;
    public String buildTime;

    @Override
    public String toString() {
        return "JsBundleVersionMetaData{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", description='" + description + '\'' +
                ", buildTime='" + buildTime + '\'' +
                '}';
    }
}
