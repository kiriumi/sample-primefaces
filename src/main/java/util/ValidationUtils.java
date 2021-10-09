package util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Pattern;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class ValidationUtils {

    private static final Pattern PATTERN_NUMELIC_HALF = Pattern.compile("^[0-9]*$");
    private static final Pattern PATTERN_NUMELIC_FULL = Pattern.compile("^[０-９]*$");
    private static final Pattern PATTERN_ALPHABET_LOWER_HALF = Pattern.compile("^[a-z]*$");
    private static final Pattern PATTERN_ALPHABET_UPPER_HALF = Pattern.compile("^[A-Z]*$");
    private static final Pattern PATTERN_ALPHABET_HALF = Pattern.compile("^[a-zA-Z]*$");
    private static final Pattern PATTERN_ALPHABET_LOWER_FULL = Pattern.compile("^[ａ-ｚ]*$");
    private static final Pattern PATTERN_ALPHABET_UPPER_FULL = Pattern.compile("^[Ａ-Ｚ]*$");
    private static final Pattern PATTERN_ALPHABET_FULL = Pattern.compile("^[ａ-ｚＡ-Ｚ]*$");
    private static final Pattern PATTERN_KATAKANA_HALF = Pattern.compile("^[｡-ﾟ]*$");
    private static final Pattern PATTERN_KATAKANA_FULL = Pattern.compile("^[ァ-ヶ]*$");
    private static final Pattern PATTERN_HIRAGANA_FULL = Pattern.compile("^[ぁ-ゖ]*$");
    private static final Pattern PATTERN_ASCII = Pattern.compile("^[!-~]*$");

    private ValidationUtils() {
    }

    public static boolean isBlank(String value) {
        return value == null ? true : StringJisUtils.trim(value).isEmpty();
    }

    public static boolean isNotBlank(String value) {
        return !isBlank(value);
    }

    public static boolean isNumericHalf(String value) {
        return PATTERN_NUMELIC_HALF.matcher(value).matches();

    }

    public static boolean isNumericFull(String value) {
        return PATTERN_NUMELIC_FULL.matcher(value).matches();
    }

    public static boolean isAlphabetLowerHalf(String value) {
        return PATTERN_ALPHABET_LOWER_HALF.matcher(value).matches();
    }

    public static boolean isAlphabetUpperHalf(String value) {
        return PATTERN_ALPHABET_UPPER_HALF.matcher(value).matches();
    }

    public static boolean isAlphabetHalf(String value) {
        return PATTERN_ALPHABET_HALF.matcher(value).matches();
    }

    public static boolean isAlphabetLowerFull(String value) {
        return PATTERN_ALPHABET_LOWER_FULL.matcher(value).matches();
    }

    public static boolean isAlphabetUpperFull(String value) {
        return PATTERN_ALPHABET_UPPER_FULL.matcher(value).matches();
    }

    public static boolean isAlphabetFull(String value) {
        return PATTERN_ALPHABET_FULL.matcher(value).matches();
    }

    public static boolean isKatakanaHalf(String value) {
        return PATTERN_KATAKANA_HALF.matcher(value).matches();
    }

    public static boolean isKatakanaFull(String value) {
        return PATTERN_KATAKANA_FULL.matcher(value).matches();
    }

    public static boolean isHiraganaFull(String value) {
        return PATTERN_HIRAGANA_FULL.matcher(value).matches();
    }

    public static boolean isAscii(String value) {
        return PATTERN_ASCII.matcher(value).matches() && isJisX0201_0208(value);
    }

    public static boolean isDigits(String value) {
        return isDigits(value, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public static boolean isDigits(String value, int integerLength) {
        return isDigits(value, integerLength, 0);
    }

    public static boolean isDigits(String value, int integerLength, int fractionLength) {

        final BigDecimal decimal;

        try {
            decimal = new BigDecimal(value);

        } catch (final NumberFormatException e) {
            return false;
        }

        final int integer = decimal.precision() - decimal.scale();

        if (integer > integerLength) {
            return false;
        }

        if (decimal.scale() > fractionLength) {
            return false;
        }

        return true;
    }

    public static final boolean isDateTime(String value, String format) {

        try {
            LocalDateTime.parse(value, DateTimeFormatter.ofPattern(format));

        } catch (final DateTimeParseException e) {
            return false;
        }

        return false;
    }

    public static boolean isEmail(String value) {

        try {
            new InternetAddress(value, true);

        } catch (final AddressException e) {
            return false;
        }
        return true;
    }

    private static final String ALLOWED_CHARSET = "x-euc-jp-linux";

    private static final String ALLOWED_BAD_CHAR = "～－";

    private static final String FORBIT_BAD_CHAR = "〜‖−¢£¬—";

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

            if (FORBIT_CHAR.contains(strChar) || FORBIT_BAD_CHAR.contains(strChar)) {
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
