package com.base.wang.util;

import java.util.UUID;

/**
 * UUID是指在一台机器上生成的数字，它保证对在同一时空中的所有机器都是唯一的。
 */
public class UUIDUtil {
    public static String generateNumber(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
