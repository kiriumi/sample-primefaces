package util;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class StringJisUtils {

    private StringJisUtils() {
    }

    private static final String REGEX_NUMELIC_HALF = "^[0-9]*$";

    public static boolean isNumericHalf(String value) {
        return value.matches(REGEX_NUMELIC_HALF);
    }

    private static final String REGEX_NUMELIC_FULL = "^[０-９]*$";

    public static boolean isNumericFull(String value) {
        return value.matches(REGEX_NUMELIC_FULL);
    }

    private static final String REGEX_ALFABET_LOWER_HALF = "^[a-z]*$";

    public static boolean isAlfavetLowerHalf(String value) {
        return value.matches(REGEX_ALFABET_LOWER_HALF);
    }

    private static final String REGEX_ALFABET_UPPER_HALF = "^[A-Z]*$";

    public static boolean isAlfavetUpperHalf(String value) {
        return value.matches(REGEX_ALFABET_UPPER_HALF);
    }

    private static final String REGEX_ALFABET_HALF = "^[a-zA-Z]*$";

    public static boolean isAlfavetHalf(String value) {
        return value.matches(REGEX_ALFABET_HALF);
    }

    private static final String REGEX_ALFABET_LOWER_FULL = "^[ａ-ｚ]*$";

    public static boolean isAlfavetLowerFull(String value) {
        return value.matches(REGEX_ALFABET_LOWER_FULL);
    }

    private static final String REGEX_ALFABET_UPPER_FULL = "^[Ａ-Ｚ]*$";

    public static boolean isAlfavetUpperFull(String value) {
        return value.matches(REGEX_ALFABET_UPPER_FULL);
    }

    private static final String REGEX_ALFABET_FULL = "^[ａ-ｚＡ-Ｚ]*$";

    public static boolean isAlfavetFull(String value) {
        return value.matches(REGEX_ALFABET_FULL);
    }

    private static final String REGEX_KATAKANA_HALF = "^[｡-ﾟ]*$";

    public static boolean isKatakanaHalf(String value) {
        return value.matches(REGEX_KATAKANA_HALF);
    }

    private static final String REGEX_KATAKANA_FULL = "^[ァ-ヶ]*$";

    public static boolean isKatakanaFull(String value) {
        return value.matches(REGEX_KATAKANA_FULL);
    }

    private static final String REGEX_HIRAGANA_FULL = "^[ぁ-ゖ]*$";

    public static boolean isHiraganaFull(String value) {
        return value.matches(REGEX_HIRAGANA_FULL);
    }

    private static final String REGEX_ASCII = "^[!-~]*$";

    public static boolean isAscii(String value) {
        return value.matches(REGEX_ASCII) && isJisX0201_0208(value);
    }

    private static final String ALLOWED_CHARSET = "x-euc-jp-linux";

    private static final String ALLOWED_BAD_CHAR = "～－";

    private static final String FORBIT_CHAR = "\"$&'()*/;<>?[\\]`{|}";

    public static boolean isJisX0201_0208(String value) {
        return isJisX0201_0208(value, false);
    }

    private static final String[] HALF_WIDTH_CAHRS = {
            " ", "!", "\"", "#", "$", "%", "&", "(", ")", "*", "+", ",", "-", ".", "/",
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ":", ";", "<", "=", ">", "?", "@",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z", "[", "\\", "]", "^", "_", "`",
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u",
            "v", "w", "x", "y", "z", "{", "|", "}", "~",
            "ｧ", "ｱ", "ｨ", "ｲ", "ｩ", "ｳ", "ｪ", "ｴ", "ｫ", "ｵ", "ｶ", "ｶﾞ", "ｷ", "ｷﾞ", "ｸ", "ｸﾞ", "ｹ", "ｹﾞ", "ｺ", "ｺﾞ",
            "ｻ", "ｻﾞ", "ｼ", "ｼﾞ", "ｽ", "ｽﾞ", "ｾ", "ｾﾞ", "ｿ", "ｿﾞ", "ﾀ", "ﾀﾞ", "ﾁ", "ﾁﾞ", "ｯ", "ﾂ", "ﾂﾞ", "ﾃ", "ﾃﾞ", "ﾄ",
            "ﾄﾞ", "ﾅ", "ﾆ", "ﾇ", "ﾈ", "ﾉ", "ﾊ", "ﾊﾞ", "ﾊﾟ", "ﾋ", "ﾋﾞ", "ﾋﾟ", "ﾌ", "ﾌﾞ", "ﾌﾟ", "ﾍ", "ﾍﾞ", "ﾍﾟ", "ﾎ",
            "ﾎﾞ", "ﾎﾟ", "ﾏ", "ﾐ", "ﾑ", "ﾒ", "ﾓ", "ｬ", "ﾔ", "ｭ", "ﾕ", "ｮ", "ﾖ", "ﾗ", "ﾘ", "ﾙ", "ﾚ", "ﾛ", "ヮ", "ﾜ", "ｲ",
            "ｴ", "ｦ", "ﾝ", "ヴ", "ヵ", "ヶ"
    };

    private static final String[] FULL_WIDTH_CHARS = {
            "　", "！", "”", "＃", "＄", "％", "＆", "（", "）", "＊", "＋", "，", "－", "．", "／",
            "０", "１", "２", "３", "４", "５", "６", "７", "８", "９", "：", "；", "＜", "＝", "＞", "？", "＠",
            "Ａ", "Ｂ", "Ｃ", "Ｄ", "Ｅ", "Ｆ", "Ｇ", "Ｈ", "Ｉ", "Ｊ", "Ｋ", "Ｌ", "Ｍ", "Ｎ", "Ｏ", "Ｐ", "Ｑ", "Ｒ", "Ｓ", "Ｔ", "Ｕ",
            "Ｖ", "Ｗ", "Ｘ", "Ｙ", "Ｚ", "［", "￥", "］", "＾", "＿", "‘",
            "ａ", "ｂ", "ｃ", "ｄ", "ｅ", "ｆ", "ｇ", "ｈ", "ｉ", "ｊ", "ｋ", "ｌ", "ｍ", "ｎ", "ｏ", "ｐ", "ｑ", "ｒ", "ｓ", "ｔ", "ｕ",
            "ｖ", "ｗ", "ｘ", "ｙ", "ｚ", "｛", "｜", "｝", "～",
            "ァ", "ア", "ィ", "イ", "ゥ", "ウ", "ェ", "エ", "ォ", "オ", "カ", "ガ", "キ", "ギ", "ク", "グ", "ケ", "ゲ", "コ", "ゴ", "サ",
            "ザ", "シ", "ジ", "ス", "ズ", "セ", "ゼ", "ソ", "ゾ", "タ", "ダ", "チ", "ヂ", "ッ", "ツ", "ヅ", "テ", "デ", "ト", "ド", "ナ",
            "ニ", "ヌ", "ネ", "ノ", "ハ", "バ", "パ", "ヒ", "ビ", "ピ", "フ", "ブ", "プ", "ヘ", "ベ", "ペ", "ホ", "ボ", "ポ", "マ", "ミ",
            "ム", "メ", "モ", "ャ", "ヤ", "ュ", "ユ", "ョ", "ヨ", "ラ", "リ", "ル", "レ", "ロ", "ヮ", "ワ", "ヰ", "ヱ", "ヲ", "ン", "ヴ",
            "ヵ", "ヶ"
    };

    public static String toFullWidth(String value) {

        String converted = new String(value);

        for (int i = 0; i < HALF_WIDTH_CAHRS.length; i++) {
            converted = converted.replace(HALF_WIDTH_CAHRS[i], FULL_WIDTH_CHARS[i]);
        }
        return converted;
    }

    public static String toHalfWidth(String value) {

        String converted = new String(value);

        for (int i = 0; i < FULL_WIDTH_CHARS.length; i++) {
            converted = converted.replaceAll(FULL_WIDTH_CHARS[i], HALF_WIDTH_CAHRS[i]);
        }
        return converted;
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
}
