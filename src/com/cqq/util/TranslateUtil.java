package com.cqq.util;

import com.alibaba.fastjson.JSON;
import com.cqq.entity.DataUnit;
import com.cqq.entity.TranslateData;
import com.yourkit.util.Strings;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TranslateUtil {

    private static final String SPACE = " ";

    public static void main(String[] args) {
        try {
//            String keyWord = URLDecoder.decode("%E6%96%87%E6%A1%A3", "gb2312");
            String keyWord = URLDecoder.decode("%E6%96%87%E6%A1%A3", "utf-8");
            System.out.println(keyWord);

            String urlStr = URLEncoder.encode("\r\n文档", "utf-8");
            System.out.println(urlStr);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<String> hello = TranslateUtil.getTranslateList(";\r\nhello world \r\n");
        System.out.println(hello);
//        List<String> helloWorld = getTranslateList("private static boolean isEnglish(String word) {");
//        System.out.println(helloWorld.get(0));


    }

    /**
     * @param word helloWorld
     * @return hello world
     */
    private static String splitWord(String word) {
        Pattern compile = Pattern.compile("([A-Z][^A-Z]+)|([a-z][^A-Z]+)");
        Matcher abcDefGhl = compile.matcher(word);
        StringBuilder all = new StringBuilder();
        while (abcDefGhl.find()) {
            String group = abcDefGhl.group();
            if (all.length() == 0) {
                all.append(group.toLowerCase());
            } else {
                all.append(" ").append(group.toLowerCase());
            }
        }
        return all.toString();
    }

    /**
     * This method is to translate word. if word is chinese, it will be translated into english. otherwise, it will be the opposite.
     *
     * @param word
     * @return R
     * @throws IOException
     */
    private static String translate(String word) throws IOException {
        String appID = "20191024000343980";
        String from = "en";
        String to = "zh";
        if (!isEnglish(word)) {
            String temp = from;
            from = to;
            to = temp;
        } else {
            word = splitWord(word);
        }
        System.out.println(String.format("%s to %s", from, to));
//        String sword = word;
        //TODO
        String sword = URLEncoder.encode(word, "utf-8");
/*
        while (sword.contains(" ")) {
            sword = sword.replace(" ", "%20");
        }
*/

        String salt = "1435660288";
        String sign = calculateSign(appID, word, salt, "dxg8tr4ooLWOfA3fgtCZ");//"f9d9a06f109e155c6405243d73ff2c52";
        String spec = "http://api.fanyi.baidu.com/api/trans/vip/translate?q=${q}&from=${from}&to=${to}&appid=${appId}&salt=${salt}&sign=${sign}"
                .replace("${q}", sword)
                .replace("${from}", from)
                .replace("${to}", to)
                .replace("${appId}", appID)
                .replace("${sign}", sign)
                .replace("${to}", to)
                .replace("${salt}", salt);
        URL url = new URL(spec);
        URLConnection urlConnection = url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder all = new StringBuilder();
        for (String str = bufferedReader.readLine(); str != null; str = bufferedReader.readLine()) {
            String s = FontUtil.decodeUnicode(str);
            all.append(s);
        }

        return all.toString();
    }

    static HashSet<String> set = new HashSet<>();

    static {
        for (char c = 'a'; c <= 'z'; c++) {
            set.add(c + "");
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            set.add(c + "");
        }
        String sign[] = new String[]{
                " ", "!", ",", ";", "<", ">",
                "[", "]", "%", "(", ")", "{",
                "}", ".", ":", "?", ".", "*",
                "/", "+", "-", "@", "#", "$",
                "&", "\n", "\r\n"
        };
        set.addAll(Arrays.stream(sign).collect(Collectors.toList()));
    }

    private static boolean isEnglish(String word) {
        return !JudgeContentIsChinese.containChinese(word);

    }


    public static List<String> getTranslateList(String text) {
        try {
            String translate = translate(text);
            TranslateData translateData = JSON.parseObject(translate, TranslateData.class);
            List<DataUnit> result = translateData.getTrans_result();
            return result == null ? Collections.singletonList(text) : result.stream().map(DataUnit::getDst)
                    .map(s -> Strings.join("", s.split(SPACE)))
                    .map(s -> s.endsWith("\r\n") ? s.substring(0, s.length() - 2) : s)
                    .map(s -> s.endsWith("\n") ? s.substring(0, s.length() - 1) : s)
                    .map(s -> {
                        int k = s.indexOf("\n");
                        while (k != -1 && k + 1 < s.length()) {
                            char c = s.charAt(k + 1);
                            if (s.contains("\n" + c)) {
                                s = s.replace("\n" + (c), "" + toUpperCase(c));
                            }
                            k = s.indexOf("\n");
                        }
                        return s;
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();

    }

    private static char toUpperCase(char c) {
        if (c >= 'a' && c <= 'z') {
            c = (char) (c - 'a' + 'A');
        }
        return c;
    }


    private static String calculateSign(String appid, String q, String salt, String secret) {
        return MD5Util.textToMD5(appid + q + salt + secret);
    }

}

