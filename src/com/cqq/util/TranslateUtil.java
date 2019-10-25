package com.cqq.util;

import com.alibaba.fastjson.JSON;
import com.cqq.entity.DataUnit;
import com.cqq.entity.TranslateData;
import com.yourkit.util.Strings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TranslateUtil {

    public static final String SPACE = " ";

    public static void main(String[] args) throws IOException {

        String hello = translate("hello");
        System.out.println(hello);
    }

    public static String translate(String word) throws IOException {
        String appID = "20191024000343980";
        String sword = word.replace(" ", "%20");
        String from = "en";
        String to = "zh";
        if (!isEnglish(word)) {
            String temp = from;
            from = to;
            to = temp;
        }
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

    private static boolean isEnglish(String word) {
        return word.chars().allMatch(c -> ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')));
    }

    public static List<String> getTranslateList(String text) {
        try {
            String translate = translate(text);
            TranslateData translateData = JSON.parseObject(translate, TranslateData.class);
            List<DataUnit> result = translateData.getTrans_result();
            return result.stream().map(DataUnit::getDst)
                    .map(s -> Strings.join("", s.split(SPACE)))
                    .map(s -> s.endsWith("\r\n") ? s.substring(0, s.length() - 2) : s)
                    .map(s -> s.endsWith("\n") ? s.substring(0, s.length() - 1) : s)
                    .map(s -> {
                        int k = s.indexOf("\n");
                        if (k + 1 < s.length()) {
                            char c = s.charAt(k + 1);
                            if (s.contains("\n" + c)) {
                                s = s.replace("\n" + (c), "" + toUpperCase(c));
                            }
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
        if(c>='a' && c <= 'z'){
            c = (char) (c-'a'+'A');
        }
        return c;
    }


    public static String calculateSign(String appid, String q, String salt, String secret) {
        return MD5Util.textToMD5(appid + q + salt + secret);
    }
}

