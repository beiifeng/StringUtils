public class StringUtils {

    public static void main(String[] args) {
        String s = "测a试b字c符d串e";
        int len = 4;
        String charsetName = "utf-8";
        System.out.println(subStringb(s, len, charsetName));
    }

    /**
     * 按字节长度截取字符串
     *
     * @param orgin       需要截取的字符串
     * @param length      需要保留的字节长度
     * @param charsetName 编码
     * @return 截取后的字符串
     */
    public static String subStringb(String orgin, int length, String charsetName) {

        String result = "";
        try {
            byte[] bs = orgin.getBytes(charsetName);
            if (length >= bs.length) {
                return orgin;
            }
            if ("UTF8".equals(charsetName.toUpperCase()) || "UTF-8".equals(charsetName.toUpperCase())) {

                // utf8 encoding
                // 0000 - 007F 0xxxxxxx
                // 0080 - 07FF 110xxxxx 10xxxxxx
                // 0800 - FFFF 1110xxxx 10xxxxxx 10xxxxxx
                while (length > 0) {
                    if ((bs[length] | 0x7F) == 0x7F) {
                        break;
                    }
                    if ((bs[length] & 0xC0) == 0xC0) {
                        break;
                    }
                    if ((bs[length] & 0xE0) == 0xE0) {
                        break;
                    }
                    length--;
                }
            } else if ("GBK".equals(charsetName.toUpperCase()) || "GBK".equals(charsetName.toUpperCase())) {
                boolean removLast = length % 2 == 1;
                for (int i = 0; i < length; i++) {
                    if ((bs[i] | 0x7F) == 0x7F) {
                        removLast = !removLast;
                    }
                }
                if (removLast) {
                    length--;
                }
            } else if ("UNICODE".equals(charsetName.toUpperCase())) {
                length += 2;
            } else if ("UTF-16".equals(charsetName.toUpperCase()) || "UTF16".equals(charsetName.toUpperCase())) {
                length += 2;
            }
            result = new String(bs, 0, length, charsetName);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return result;
    }
    
}
