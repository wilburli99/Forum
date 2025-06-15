package cn.iocoder.forum.utils;


import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    /**
     *  对字符串进行MD5加密
     * @param str 明文
     * @return    密文
     */
    public static String md5(String str) {
        return DigestUtils.md5Hex(str);
    }

    /**
     *  给用户密码加密
     * @param str  密码
     * @param salt 扰动字符
     * @return     密文
     */
    public static String md5Salt(String str, String salt) {
        return md5(md5(str) + salt);
    }
}
