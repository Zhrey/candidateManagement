package com.ray.core.api.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class CountFileUtil {
    /**
     * Description: 获取文件后缀名
     *
     * @param fileName
     * @return
     * @see
     */
    public static String getExtensionName(String fileName)
    {
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        return prefix;
    }

    /**
     * Description: 获取文件前缀名
     *
     * @param fileName
     * @return
     * @see
     */
    public static String getExfileName(String fileName)
    {
        String prefix = fileName.substring(0,fileName.lastIndexOf("."));
        return prefix;
    }

    /**
     * 根据path获取文件名
     *
     * @author kokJuis
     * @version 1.0
     * @date 2016-12-12
     * @param filename
     * @return
     */
    public static String getOriginalFilename(String filename)
    {
        if (filename == null) return "";
        int pos = filename.lastIndexOf(".");
        if (pos == -1) pos = filename.lastIndexOf("\\");
        if (pos != -1)
            return filename.substring(pos);
        else
            return filename;
    }

    public static File gettempfile(MultipartFile file){
        File f = null;
        try {
            String filename = file.getOriginalFilename();
            f = File.createTempFile("tmp", CountFileUtil.getExtensionName(filename));
            file.transferTo(f);
            f.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }
}
