package com.up.common.utils;

import com.jfinal.upload.UploadFile;
import org.apache.log4j.Logger;
import sun.misc.BASE64Decoder;

import java.io.*;

/**
 * TODO:文件操作工具
 * Created by 王剑洪
 * on 2016/10/21 0021. 17:12
 */
public class FileUtils {
    private static Logger logger = Logger.getLogger(FileUtils.class);

    /**
     * 复制单个文件
     *
     * @param fromPath
     * @param toPath
     */
    public static void copyFile(String fromPath, String toPath) {
        int byteSum = 0;
        int byteRead = 0;
        File from = new File(fromPath);
        if (from.exists()) {//文件存在
            try {
                InputStream inStream = new FileInputStream(fromPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(toPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteRead = inStream.read(buffer)) != -1) {
                    byteSum += byteRead; //字节数 文件大小
                    System.out.println(byteSum);
                    fs.write(buffer, 0, byteRead);
                }
                inStream.close();
            } catch (Exception e) {
                logger.error(e);
                e.printStackTrace();
            }
        }

    }

    /**
     * 复制整个文件夹内容
     *
     * @param fromPath
     * @param toPath
     * @return boolean
     */
    public static void copyFolder(String fromPath, String toPath) {
        try {
            (new File(fromPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
            File a = new File(fromPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (fromPath.endsWith(File.separator)) {
                    temp = new File(fromPath + file[i]);
                } else {
                    temp = new File(fromPath + File.separator + file[i]);
                }
                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(toPath + "/" +
                            (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {//如果是子文件夹
                    copyFolder(fromPath + "/" + file[i], toPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();

        }
    }


    public static boolean base64ToImage(String base64, String path) {
        if (null == base64) {
            return false;
        }
        OutputStream out = null;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(base64);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据
                    b[i] += 256;
                }
            }
            //生成jpeg图片
            out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


    }
}
