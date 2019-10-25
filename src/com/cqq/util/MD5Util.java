package com.cqq.util;

import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具类
 */
public class MD5Util {
    String result = null;

    public static String textToMD5(String text) {
        String result = null;
        if (StringUtils.isBlank(text)) {
            return null;
        }
        try {
            // 首先进行实例化和初始化
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 得到一个操作系统默认的字节编码格式的字节数组
            byte[] btInput = text.getBytes("UTF-8");
            // 对得到的字节数组进行处理
            md.update(btInput);
            // 进行哈希计算并返回结果
            byte[] btResult = md.digest();
            // 进行哈希计算后得到的数据的长度
            StringBuilder sb = new StringBuilder();
            for (byte b : btResult) {
                int bt = b & 0xff;
                if (bt < 16) {
                    sb.append(0);
                }
                sb.append(Integer.toHexString(bt));
            }
            result = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException");
        } catch (UnsupportedEncodingException e) {
            System.out.println("UnsupportedEncodingException");
        }
        return result;
    }


}
