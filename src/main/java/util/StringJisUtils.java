package util;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class StringJisUtils {

    private StringJisUtils() {
    }

    public static boolean isJisX0201_0208(String value) {
        return isJisX0201_0208(value , false);
    }


    public static boolean isJisX0201_0208(String value, boolean allowLf) {

        for (char c : value.toCharArray()) {

            if (List.of('－', '―').contains(c)) {
                continue;
            }

            String strChar = new String(new char[] { c });
            try {
                byte[] bytes = strChar.getBytes("euc-jp");
                int code = 0;
                for (byte b : bytes) {
                    code = (code << 8) + (b & 0xff);
                }

                if (code == 0x0a && allowLf) {
                    // 改行を許可する場合
                    continue;
                }

                if (0x20 <= code && code <= 0x7e) {
                    // ASCII
                    if ("\"$&'()*/;<>?[\\]`{|}".contains(strChar)) {
                        // インジェクション対策用の使用禁止文字
                        return false;

                    } else {
                        // TODO ここのコードが不明
                        String compare = new String(new byte[] { (byte) code }, "EUC_JP");
                        if (!strChar.equals(compare)) {
                            return false;
                        }
                    }
                    continue;

                } else if (0x8EA1 <= code && code <= 0x8EDF) {
                    // JIS X 0201 半角カナ
                    continue;

                } else if (0xA1A1 <= code && code <= 0xF406) {
                    // JIS X 0208
                    if (List.of('〜', '‖', '−', '¢', '£', '¬', '—').contains(c)) {
                        // ダメ文字はエラーとする
                        return false;
                    }
                    continue;
                }
                return false;

            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException();
            }
        }
        return true;
    }
}
