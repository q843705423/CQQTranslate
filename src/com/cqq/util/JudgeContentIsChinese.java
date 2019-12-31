package com.cqq.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JudgeContentIsChinese {
    //英文占1byte，非英文（可认为是中文）占2byte，根据这个特性来判断字符
    private static boolean checkChar(char ch) {
        if ((ch + "").getBytes().length == 1) {
            return true;//英文
        } else {
            return false;//中文
        }
    }

    public static boolean containChinese(String str) {
        if (str != null) {
            for (int i = 0; i < str.length(); i++) {
                if (!checkChar(str.charAt(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    //判断是不是中文
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    //判断是不是英文字母
    private static boolean isEnglish(String charaString) {
        return charaString.matches("^[a-zA-Z]*");
    }

    //根据中文unicode范围判断u4e00 ~ u9fa5不全
    private static String isChinese(String str) {
        String regEx1 = "[\\u4e00-\\u9fa5]+";
        String regEx2 = "[\\uFF00-\\uFFEF]+";
        String regEx3 = "[\\u2E80-\\u2EFF]+";
        String regEx4 = "[\\u3000-\\u303F]+";
        String regEx5 = "[\\u31C0-\\u31EF]+";
        Pattern p1 = Pattern.compile(regEx1);
        Pattern p2 = Pattern.compile(regEx2);
        Pattern p3 = Pattern.compile(regEx3);
        Pattern p4 = Pattern.compile(regEx4);
        Pattern p5 = Pattern.compile(regEx5);
        Matcher m1 = p1.matcher(str);
        Matcher m2 = p2.matcher(str);
        Matcher m3 = p3.matcher(str);
        Matcher m4 = p4.matcher(str);
        Matcher m5 = p5.matcher(str);
        if (m1.find() || m2.find() || m3.find() || m4.find() || m5.find())
            return "中文";
        else
            return "英文";
    }

    public static void main(String[] args) {
        System.out.println("使用长度判断:");
        System.out.println(containChinese("Hello++"));
        System.out.println(containChinese("Hello++。、，？"));
        System.out.println(containChinese("Hello++编程"));
        System.out.println(containChinese("编程"));

        System.out.println("\r\n使用正则表达式判断:");
        System.out.println(isChinese("Hello++"));
        System.out.println(isChinese("Hello++。、，？"));
        System.out.println(isChinese("Hello++编程"));
        System.out.println(isChinese("编程"));

        System.out.println("\r\n使用Character.UnicodeBlock");
        System.out.println(isChinese('h') ? "中文" : "英文");
        System.out.println(isChinese(',') ? "中文" : "英文");
        System.out.println(isChinese('。') ? "中文" : "英文");
        System.out.println(isChinese('编') ? "中文" : "英文");
    }
}
