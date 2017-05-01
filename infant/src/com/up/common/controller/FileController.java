package com.up.common.controller;

import com.jfinal.upload.UploadFile;
import com.up.common.base.BaseResult;
import com.up.common.base.ImageResult;
import com.up.common.def.FileType;
import com.up.common.def.ResCode;
import com.up.common.utils.TextUtils;
import com.up.infant.model.Country;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/10/21 0021. 16:13
 */
public class FileController extends BaseController {


    private boolean isImage(String format) {
        if (format.equals(".jpg") || format.equals(".png")) {
            return true;
        } else {
            return false;
        }

    }

    public void upload() {
        String fileType = getPara("type");
        UploadFile file = getFile("file");
        String temp[] = file.getFileName().split(".");
        String suffix = "." + temp[temp.length - 1];
        String path = "";
        if (fileType.equals(FileType.TEMP.getFileType())) {
            path = rootPath + FileType.TEMP.getPath();
        } else if (fileType.equals(FileType.FACE.getFileType())) {
            path = rootPath + FileType.FACE.getPath();
        } else if (fileType.equals(FileType.GOODS.getFileType())) {
            path = rootPath + FileType.GOODS.getPath();
        } else if (fileType.equals(FileType.COMMENT.getFileType())) {
            path = rootPath + FileType.COMMENT.getPath();
        } else {
            resFailure("请传递正确的文件类型！");
            return;
        }
        path+=suffix;
        boolean isSuccess = file.getFile().renameTo(new File(path + suffix));
        if (isSuccess) {
            ImageResult result=new ImageResult(path);
            resSuccess("上传成功！", result);
        } else {
            resFailure("系统异常！");
        }

    }

    /**
     * 存储临时文件
     */
    public void uploadTemp(){
        UploadFile  file = getFile("file");
        String temp[] = file.getFileName().split("\\.");
        long now = new Date().getTime();
        String uuid = UUID.randomUUID().toString();
        String suffix = uuid + "." + temp[temp.length - 1];
        boolean isSuccess = file.getFile().renameTo(new File(rootPath + "/upload/temp/" + suffix));
        String result = "upload/temp/" + suffix;
        if (isSuccess) {
            resSuccess("上传成功！", result);
        } else {
            res(ResCode.IMG_ERROR);
        }
    }

    public void editUploadTemp(){
        UploadFile  file = getFiles().get(0);
        String temp[] = file.getFileName().split("\\.");
        long now = new Date().getTime();
        String uuid = UUID.randomUUID().toString();
        String suffix = uuid + "." + temp[temp.length - 1];
        /*try {
            File newFile =new File(rootPath + "//upload//editorTemp");
            if(!newFile.exists()) {
                newFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        /*try {
            if (!(new File(rootPath + "/upload/editorTemp/").isDirectory())) {
                new File(rootPath + "/upload/editorTemp/").createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        boolean isSuccess = file.getFile().renameTo(new File(rootPath + "/upload/editorTemp/" + suffix));
        String result = "upload/editorTemp/" + suffix;
        String imgUrl = "error|图片上传失败";
        if (isSuccess) {
            try {
                String ip = InetAddress.getLocalHost().getHostAddress();
                System.out.print("本地ip:"+ip);
                imgUrl = "http://"+ip+":8080/" + result;
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
       /* PrintWriter out = null;
        try {
            out = getResponse().getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.print(imgUrl);  //返回url地址
        out.flush();
        out.close();*/
        getResponse().addHeader("Access-Control-Allow-Origin", "*");
        getResponse().setContentType("text/text;charset=utf-8");
        renderText(imgUrl);
    }
}
