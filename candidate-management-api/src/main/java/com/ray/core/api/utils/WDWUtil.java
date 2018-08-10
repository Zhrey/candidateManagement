package com.ray.core.api.utils;

public class WDWUtil {
    // @描述：是否是2003的excel，返回true是2003
    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    // @描述：是否是2007的excel，返回true是2007
    public static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

    public static String ran2fuc(String num){
        if(num.length()>2) {
            if (".0".equals(num.substring(num.length() - 2, num.length()))) {
                return num.substring(0, num.length() - 2);
            }
        }
        return num;
    }
}
