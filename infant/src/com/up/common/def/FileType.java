package com.up.common.def;

/**
 * TODO:文件类型及其存储路径
 * Created by 王剑洪
 * on 2016/10/21 0021. 20:07
 */
public enum FileType {
    TEMP("0", "upload/temp/"),
    FACE("1", "upload/face/"),
    GOODS("2", "upload/goods/"),
    COMMENT("3", "upload/comment/"),
    COUNTRY("4","upload/country/"),
    ARTICLE("5","upload/article/");
    private String fileType;
    private String path;

    FileType(String type, String path) {
        this.fileType = type;
        this.path = path;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
