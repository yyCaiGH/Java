package com.up.infant.controller;

import com.jfinal.core.Const;
import com.jfinal.upload.UploadFile;
import com.up.common.base.ImageResult;
import com.up.common.controller.BaseController;
import com.up.common.def.FileType;

import java.io.File;

/**
 * TODO:测试
 * Created by 王剑洪
 * on 2016/10/18 0018. 20:51
 */
public class AdminController extends BaseController{

    public void login(){
        String userName=getPara("userName");
        String password=getPara("password");
        if (userName.equals("admin")&&password.equals("111111")){
            resSuccess("登陆成功");
        }
        else{
            resFailure("登陆失败");
        }
    }

    public void imgTest(){
        String fileType = getPara("type");
        System.out.println("------------------22-----------11-------------fileType----"+fileType);
        UploadFile file = getFile("file","imgs",100* Const.DEFAULT_MAX_POST_SIZE);
        System.out.println("---------------22233------------------------------"+file.getFileName());
        resSuccess("请求成功");

        /*String temp[] = file.getFileName().split(".");
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
        }*/
    }
    public void index1(){
        render("index.html");
    }
}
