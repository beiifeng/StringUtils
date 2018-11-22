package com.yifei.demo.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author Eephaie
 * @version 1.0.0.0
 * @date 2018/11/13 10:57
 */
public class StringUtils {

    public static final String original = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ+/";

    public static void main(String[] args) {
        String s = "中afghi文b测c试d字e符f串g";
        s = getChinese(1000);
        int len = 100;
        String charsetName = "utf8";

        try {
            List<String> splitedString = subStringb2(s, len, charsetName);
            splitedString.forEach(System.out::println);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 按字节长度截取字符串
     *
     * @param orgin       需要截取的字符串
     * @param blength     需要保留的字节长度
     * @param charsetName 编码,对于字符集为UTF-8的数据库,请指定编码为UTF-8;字符集为GBK的数据库,请指定编码GBK
     * @return 截取后的字符串
     * @throws UnsupportedEncodingException 不支持的字符编码
     */
    public static String subStringb(String orgin, int blength, String charsetName) throws UnsupportedEncodingException {
        return subStringb2(orgin, blength, charsetName).get(0);
    }

    /**
     * 按字节长度分割字符串
     *
     * @param orgin       需要截取的字符串
     * @param blength     需要保留的字节长度
     * @param charsetName 编码,对于字符集为UTF-8的数据库,请指定编码为UTF-8;字符集为GBK的数据库,请指定编码GBK
     * @return 分割后的字符串
     * @throws UnsupportedEncodingException 不支持的字符编码
     */
    public static List<String> subStringb2(String orgin, int blength, String charsetName) throws UnsupportedEncodingException {

        List<String> result = new ArrayList<>();
        int length;

        charsetName = charsetName.toUpperCase();
        byte[] bs = orgin.getBytes(charsetName);

        while (bs.length > 0) {
            length = blength;
            if (length >= bs.length) {
                result.add(new String(bs, 0, bs.length, charsetName));
                break;
            }
            if ("UTF8".equals(charsetName) || "UTF-8".equals(charsetName)) {
                // utf8 encoding
                // 0000 0000 - 0000 007F 0xxxxxxx
                // 0000 0080 - 0000 07FF 110xxxxx 10xxxxxx
                // 0000 0800 - 0000 FFFF 1110xxxx 10xxxxxx 10xxxxxx
                // 0001 0000 - 0010 FFFF 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
                while (length > 0) {
                    if ((bs[length] | 0x7F) == 0x7F) {
                        break;
                    }
                    if ((bs[length] & 0xC0) == 0xC0) {
                        break;
                    }
                    length--;
                }
            } else if ("GBK".equals(charsetName)) {
                boolean removLast = length % 2 == 1;
                for (int i = 0; i < length; i++) {
                    if ((bs[i] | 0x7F) == 0x7F) {
                        removLast = !removLast;
                    }
                }
                if (removLast) {
                    length--;
                }
            } else if ("UNICODE".equals(charsetName)) {
                if (length % 2 == 1) {
                    length--;
                }
            } else if ("UTF-16".equals(charsetName) || "UTF16".equals(charsetName)) {
                if (length % 2 == 1) {
                    length--;
                }
            } else if ("UTF-16BE".equals(charsetName)) {
                if (length % 2 == 1) {
                    length--;
                }
            } else if ("UTF-16LE".equals(charsetName)) {
                if (length % 2 == 1) {
                    length--;
                }
            }
            result.add(new String(bs, 0, length, charsetName));
            bs = Arrays.copyOfRange(bs, length, bs.length);
        }

        if (result.size() == 0) {
            result.add("");
        }
        return result;
    }


    /**
     * 获取随机长度的随机字符串[a-zA-Z0-9\+/]
     *
     * @param maxLength 字符串最长长度
     * @return 随机字符串
     */
    public static String getRadomString(int maxLength) {
        if (maxLength <= 0) {
            throw new IllegalArgumentException("length must be positive integer!");
        }
        StringBuilder sb = new StringBuilder();
        int ol = original.length();
        Random seed = new Random();
        int length = seed.nextInt(maxLength);
        for (int i = 0; i < length; i++) {
            char c = original.charAt(seed.nextInt(ol));
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * 获取固定长度的随机字符串[a-zA-Z0-9\+/]
     *
     * @param length 字符串长度
     * @return 随机字符串
     * @throws IllegalArgumentException 输入长度小于等于零
     */
    public static String getFixString(int length) throws IllegalArgumentException {
        if (length <= 0) {
            throw new IllegalArgumentException("length must be positive integer!");
        }
        StringBuilder sb = new StringBuilder();
        int ol = original.length();
        Random seed = new Random();

        for (int i = 0; i < length; i++) {
            char c = original.charAt(seed.nextInt(ol));
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * 获取随机中文字符串
     *
     * @param length 所需长度
     * @return 随机中文字符串
     */
    public static String getChinese(int length) {
        StringBuilder sb = new StringBuilder();

        // u4e00-u9fa5 中文范围
        int min = 0x4e00;
        int max = 0x9fa5;
        Random seed = new Random();

        for (int i = 0; i < length; i++) {
            int n = min + seed.nextInt(max - min);
            char c = (char) n;
            sb.append(Character.toString(c));
        }

        return sb.toString();
    }

    /**
     * ???
     *
     * @param dataStr ???
     * @return ???
     */
    public static String decodeUnicode(final String dataStr) {
        int start = 0;
        int end = 0;
        final StringBuilder buffer = new StringBuilder();
        while (start > -1) {
            end = dataStr.toLowerCase().indexOf("\\u", start + 2);
            String charStr;
            if (end == -1) {
                charStr = dataStr.substring(start + 2, dataStr.length());
            } else {
                charStr = dataStr.substring(start + 2, end);
            }
            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
            buffer.append(Character.toString(letter));
            start = end;
        }
        return buffer.toString();
    }

}
