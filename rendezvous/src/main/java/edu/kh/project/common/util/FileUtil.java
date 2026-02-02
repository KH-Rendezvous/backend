package edu.kh.project.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {
    public static String rename(String originFileName) {
        int ranNum = (int) (Math.random() * 100000);
        String str = "_" + String.format("%05d", ranNum);
        String ext = originFileName.substring(originFileName.lastIndexOf("."));
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + str + ext;
    }
}