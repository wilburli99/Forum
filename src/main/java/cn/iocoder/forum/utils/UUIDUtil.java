package cn.iocoder.forum.utils;

import java.util.UUID;

public class UUIDUtil {
    /**
     * 获取 UUID(36位)
     * @return
     */
    public static String uuid_36() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取 UUID(32位)
     * @return
     */
    public static String uuid_32() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
