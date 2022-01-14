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

import org.apache.commons.lang3.StringUtils;

public class ValidationUtils {

    private static final Pattern PATTERN_NUMELIC_HALF = Pattern.compile("^[0-9]*$");
    private static final Pattern PATTERN_NUMELIC_FULL = Pattern.compile("^[０-９]*$");

    private static final Pattern PATTERN_ALPHABET_LOWER_HALF = Pattern.compile("^[ a-z]*$");
    private static final Pattern PATTERN_ALPHABET_UPPER_HALF = Pattern.compile("^[ A-Z]*$");
    private static final Pattern PATTERN_ALPHABET_HALF = Pattern.compile("^[ a-zA-Z]*$");
    private static final Pattern PATTERN_ALPHABET_LOWER_FULL = Pattern.compile("^[　ａ-ｚ]*$");
    private static final Pattern PATTERN_ALPHABET_UPPER_FULL = Pattern.compile("^[　Ａ-Ｚ]*$");
    private static final Pattern PATTERN_ALPHABET_FULL = Pattern.compile("^[　ａ-ｚＡ-Ｚ]*$");

    private static final Pattern PATTERN_ALPHANUMERIC_HALF = Pattern.compile("^[ 0-9A-Za-z]*$");
    private static final Pattern PATTERN_ALPHANUMERIC_FULL = Pattern.compile("^[　０-１Ａ-Ｚａ-ｚ]*$");

    private static final Pattern PATTERN_ALPHANUMERIC_SYMBOL_HALF = Pattern.compile("^[ -~]*$");
    private static final Pattern PATTERN_ALPHANUMERIC_SYMBOL_FULL = Pattern.compile("^[　！-～]*$");

    private static final Pattern PATTERN_KATAKANA_HALF = Pattern.compile("^[ ｦ-ﾟ]*$");
    private static final Pattern PATTERN_KATAKANA_FULL = Pattern.compile("^[　ァ-ヶー]*$");
    private static final Pattern PATTERN_HIRAGANA = Pattern.compile("^[　ぁ-ゖー]*$");

    private static final Pattern PATTERN_HANKAKU = Pattern.compile("^[ -~｡-ﾟ]*$"); // 禁止記号を除外する正規表現：^(?!.*["$&'\(\)*/;<>?\[\\\]`{\|}])[ -~｡-ﾟ]*$

    private ValidationUtils() {
    }

    public static boolean isBlank(String value) {
        return StringUtils.isBlank(value);
    }

    public static boolean isNotBlank(String value) {
        return StringUtils.isNotBlank(value);
    }

    public static boolean isNumericHalf(String value) {
        return (value == null) ? false : PATTERN_NUMELIC_HALF.matcher(value).matches();

    }

    public static boolean isNumericFull(String value) {
        return (value == null) ? false : PATTERN_NUMELIC_FULL.matcher(value).matches();
    }

    public static boolean isAlphabetLowerHalf(String value) {
        return (value == null) ? false : PATTERN_ALPHABET_LOWER_HALF.matcher(value).matches();
    }

    public static boolean isAlphabetUpperHalf(String value) {
        return (value == null) ? false : PATTERN_ALPHABET_UPPER_HALF.matcher(value).matches();
    }

    public static boolean isAlphabetHalf(String value) {
        return (value == null) ? false : PATTERN_ALPHABET_HALF.matcher(value).matches();
    }

    public static boolean isAlphabetLowerFull(String value) {
        return (value == null) ? false : PATTERN_ALPHABET_LOWER_FULL.matcher(value).matches();
    }

    public static boolean isAlphabetUpperFull(String value) {
        return (value == null) ? false : PATTERN_ALPHABET_UPPER_FULL.matcher(value).matches();
    }

    public static boolean isAlphabetFull(String value) {
        return (value == null) ? false : PATTERN_ALPHABET_FULL.matcher(value).matches();
    }

    public static boolean isAlphanumericHalf(String value) {
        return (value == null) ? false : PATTERN_ALPHANUMERIC_HALF.matcher(value).matches();
    }

    public static boolean isAlphanumericFull(String value) {
        return (value == null) ? false : PATTERN_ALPHANUMERIC_FULL.matcher(value).matches();
    }

    public static boolean isAlphanumericSymbolHalf(String value) {
        return (value == null) ? false
                : PATTERN_ALPHANUMERIC_SYMBOL_HALF.matcher(value).matches() && isJisX0201_0208(value);
    }

    public static boolean isAlphanumericSymbolFull(String value) {
        return (value == null) ? false
                : PATTERN_ALPHANUMERIC_SYMBOL_FULL.matcher(value).matches() && isJisX0201_0208(value);
    }

    public static boolean isKatakanaHalf(String value) {
        return (value == null) ? false : PATTERN_KATAKANA_HALF.matcher(value).matches();
    }

    public static boolean isKatakanaFull(String value) {
        return (value == null) ? false : PATTERN_KATAKANA_FULL.matcher(value).matches();
    }

    public static boolean isHiraganaFull(String value) {
        return (value == null) ? false : PATTERN_HIRAGANA.matcher(value).matches();
    }

    public static boolean isHalfWidth(String value) {
        return (value == null) ? false : PATTERN_HANKAKU.matcher(value).matches() && isJisX0201_0208(value);
    }

    public static boolean isFullWidth(String value) {
        return (value == null) ? false : !PATTERN_HANKAKU.matcher(value).matches() && isJisX0201_0208(value);
    }

    public static boolean isDigits(String value) {
        return (value == null) ? false : isDigits(value, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public static boolean isDigits(String value, int integerLength) {
        return isDigits(value, integerLength, 0);
    }

    public static boolean isDigits(String value, int integerLength, int fractionLength) {

        if (value == null) {
            return false;
        }

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

        if (value == null) {
            return false;
        }

        try {
            LocalDateTime.parse(value, DateTimeFormatter.ofPattern(format));

        } catch (final DateTimeParseException e) {
            return false;
        }

        return false;
    }

    public static boolean isEmail(String value) {

        if (value == null) {
            return false;
        }

        try {
            new InternetAddress(value, true);

        } catch (final AddressException e) {
            return false;
        }

        return true;
    }

    private static final String ALLOWED_CHARSET = "x-euc-jp-linux";

    private static final String ALLOWED_UNMAPPING_CHAR = "～－";

    private static final String FORBITTEN_UMAPPING_CHAR = "〜‖−¢£¬—";

    private static final String FORBITTEN_CHAR = "\"$&'()*/;<>?[\\]`{|}";

    private static final String CRLF = "\r\n";

    private static final String LF = "\n";

    private static final String EMPTY = "";

    private static boolean containSurrogate(String value) {
        return value.chars().anyMatch(c -> {
            return Character.isSurrogate((char) c);
        });
    }

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

        if (value == null) {
            return false;
        }

        final String[] strChars = value.split(EMPTY);

        for (final String strChar : strChars) {

            if (containSurrogate(value)) {
                return false;
            }

            if ((strChar.equals(LF) || strChar.equals(CRLF)) && allowNewLine) {
                // 改行を許可する場合
                continue;
            }

            if (FORBITTEN_CHAR.contains(strChar) || FORBITTEN_UMAPPING_CHAR.contains(strChar)) {
                // 禁止文字を使用している場合エラー
                return false;
            }

            if (ALLOWED_UNMAPPING_CHAR.contains(strChar)) {
                // 使用できるダメ文字を使用している場合
                continue;
            }

            try {
                final byte[] bytes = strChar.getBytes(ALLOWED_CHARSET);

                int code = 0;
                for (final byte b : bytes) {
                    code = (code << 8) + (b & 0xff);
                }

                if ((0x20 <= code && code <= 0x7e) // ASCII
                        || (0x8EA1 <= code && code <= 0x8EDF) // JIS X 0201 半角カナ
                        || (0xA1A1 <= code && code <= 0xF406)) { // JIS X 0208

                    final String encoded = new String(bytes, ALLOWED_CHARSET);
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
