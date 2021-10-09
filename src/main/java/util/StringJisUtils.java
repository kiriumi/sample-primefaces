package util;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class StringJisUtils {

    private StringJisUtils() {
    }

    @Deprecated
    public static boolean isJisX0201_0208_old(String value) {
        return isJisX0201_0208_old(value, false);
    }

    @Deprecated
    public static boolean isJisX0201_0208_old(String value, boolean allowLf) {

        for (final char c : value.toCharArray()) {

            if (List.of('－', '―').contains(c)) {
                continue;
            }

            final String strChar = new String(new char[] { c });
            try {
                final byte[] bytes = strChar.getBytes("euc-jp");
                int code = 0;
                for (final byte b : bytes) {
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
                    }

                    final String compare = new String(new byte[] { (byte) code }, "EUC_JP");
                    if (!strChar.equals(compare)) {
                        return false;
                    }

                    continue;

                }
                if (0x8EA1 <= code && code <= 0x8EDF) {
                    // JIS X 0201 半角カナ
                    continue;

                }
                if (0xA1A1 <= code && code <= 0xF406) {
                    // JIS X 0208
                    if (List.of('〜', '‖', '−', '¢', '£', '¬', '—').contains(c)) {
                        // ダメ文字はエラーとする
                        return false;
                    }
                    continue;
                }
                return false;

            } catch (final UnsupportedEncodingException e) {
                throw new RuntimeException();
            }
        }
        return true;
    }

    private static final String ALLOWED_CHARSET = "x-euc-jp-linux";

    private static final String ALLOWED_BAD_CHAR = "～－";

    private static final String FORBIT_CHAR = "\"$&'()*/;<>?[\\]`{|}";

    public static boolean isJisX0201_0208(String value) {
        return isJisX0201_0208(value, false);
    }

    /**
     * JIS X 0201, JIS X 0208の文字かチェックする。左記文字集合の文字でも以下文字はエラーとする。<br>
     * 制御文字<br>
     * 「～」「－」を除くダメ文字（― ∥ ￠ ￡ ￢）<br>
     * オーバーライン（‾）<br>
     * .
     *
     * @param value チェック対象の文字列
     * @param allowNewLine 改行を許可するか否か
     * @return チェック結果
     */
    public static boolean isJisX0201_0208(String value, boolean allowNewLine) {

        final String str = value.replaceAll("\r\n", "\n");

        final String[] strChars = str.split("");

        for (final String strChar : strChars) {

            if (strChar.equals("\n") && allowNewLine) {
                // 改行を許可する場合
                continue;
            }

            if (FORBIT_CHAR.contains(strChar)) {
                // 禁止文字を使用している場合エラー
                return false;
            }

            if (ALLOWED_BAD_CHAR.contains(strChar)) {
                // 使用できるダメ文字を使用している場合
                continue;
            }

            try {
                final byte[] eucJpBytes = strChar.getBytes(ALLOWED_CHARSET);

                int code = 0;
                for (final byte b : eucJpBytes) {
                    code = (code << 8) + (b & 0xff);
                }

                if ((0x20 <= code && code <= 0x7e) // ASCII
                        || (0x8EA1 <= code && code <= 0x8EDF) // JIS X 0201 半角カナ
                        || (0xA1A1 <= code && code <= 0xF406)) { // JIS X 0208

                    final String encoded = new String(eucJpBytes, ALLOWED_CHARSET);
                    if (strChar.equals(encoded)) {
                        continue;
                    }
                }

                return false;

            } catch (final UnsupportedEncodingException e) {
                throw new RuntimeException();
            }
        }
        return true;
    }

}
