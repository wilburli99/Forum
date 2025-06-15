package cn.iocoder.forum.utils;

public class StringUtil {
    /**
     * 判断字符串是否为空
     * @param value 字符串
     * @return  true空 <br/> false非空
     */
    public static boolean isEmpty(String value) {
        return value == null || value.length() == 0;
    }
}
