import java.io.UnsupportedEncodingException;

/**
 * @author Eephaie
 * @version 1.0.0.0
 * @date 2018/11/13 10:57
 */
public class StringUtils {

    public static void main(String[] args) {
        String s = "测a试b字c符d串e";
        int len = 9;
        String charsetName = "utf8";
        System.out.println(subStringb(s, len, charsetName));
    }

    /**
     * 按字节长度截取字符串
     *
     * @param orgin       需要截取的字符串
     * @param blength     需要保留的字节长度
     * @param charsetName 编码
     * @return 截取后的字符串
     */
    public static String subStringb(String orgin, int blength, String charsetName) {

        String result = "";
        byte[] utf16be = new byte[]{(byte) 0xFE, (byte) 0xFF};
        byte[] utf16le = new byte[]{(byte) 0xFF, (byte) 0xFE};
        try {
            byte[] bs = orgin.getBytes(charsetName);
            if (blength >= bs.length) {
                return orgin;
            }
            if ("UTF8".equals(charsetName.toUpperCase()) || "UTF-8".equals(charsetName.toUpperCase())) {
                // utf8 encoding
                // 0000 0000 - 0000 007F 0xxxxxxx
                // 0000 0080 - 0000 07FF 110xxxxx 10xxxxxx
                // 0000 0800 - 0000 FFFF 1110xxxx 10xxxxxx 10xxxxxx
                // 0001 0000 - 0010 FFFF 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
                while (blength > 0) {
                    if ((bs[blength] | 0x7F) == 0x7F) {
                        break;
                    }
                    if ((bs[blength] & 0xC0) == 0xC0) {
                        break;
                    }
                    if ((bs[blength] & 0xE0) == 0xE0) {
                        break;
                    }
                    if ((bs[blength] & 0xF0) == 0xF0) {
                        break;
                    }
                    blength--;
                }
            } else if ("GBK".equals(charsetName.toUpperCase())) {
                boolean removLast = blength % 2 == 1;
                for (int i = 0; i < blength; i++) {
                    if ((bs[i] | 0x7F) == 0x7F) {
                        removLast = !removLast;
                    }
                }
                if (removLast) {
                    blength--;
                }
            } else if ("UNICODE".equals(charsetName.toUpperCase())) {
                if (blength % 2 == 1) {
                    blength--;
                }
            } else if ("UTF-16".equals(charsetName.toUpperCase()) || "UTF16".equals(charsetName.toUpperCase())) {
                if (blength % 2 == 1) {
                    blength--;
                }
            } else if ("UTF-16BE".equals(charsetName.toUpperCase())) {
                if (blength % 2 == 1) {
                    blength--;
                }
            } else if ("UTF-16LE".equals(charsetName.toUpperCase())) {
                if (blength % 2 == 1) {
                    blength--;
                }
            }
            result = new String(bs, 0, blength, charsetName);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return result;
    }
    
}
