package com.awesomeproject.service.jsBundle.dto;

public class CheckLastVersionRes {
    public Integer code;
    public String message;
    public Data data;

    public Data getData() {
        return data;
    }

    @Override
    public String toString() {
        return "CheckLastVersionRes{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public static class Data {
        public String id;
        public String name;

        public String version;
        public String versionNumber;
        public String buildTime;
        public String publishTime;
        public String ossUrl;
        public String pullCount;
        public String bundleSize;
        public String description;

        @Override
        public String toString() {
            return "Data{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", version='" + version + '\'' +
                    ", versionNumber='" + versionNumber + '\'' +
                    ", buildTime='" + buildTime + '\'' +
                    ", publishTime='" + publishTime + '\'' +
                    ", ossUrl='" + ossUrl + '\'' +
                    ", pullCount='" + pullCount + '\'' +
                    ", bundleSize='" + bundleSize + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }

}
