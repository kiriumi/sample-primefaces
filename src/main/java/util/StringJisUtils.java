package util;

public class StringJisUtils {

    private StringJisUtils() {
    }

    private static final String EMPTY = "";
    private static final String HALF_SPACE = " ";
    private static final String FULL_SPACE = "　";

    public static String trimLeft(String value) {

        final String returnStr = new String(value);
        final String[] strChars = returnStr.split(EMPTY);

        int beginIndex = 0;

        for (int i = 0; i < strChars.length; i++) {
            if (!(strChars[i].equals(HALF_SPACE) || strChars[i].equals(FULL_SPACE))) {
                beginIndex = i;
                break;
            }
        }

        return returnStr.substring(beginIndex);
    }

    public static String trimRight(String value) {
        return trimLeft(trimRight(value));
    }

    public static String trim(String value) {

        final String returnStr = new String(value);
        final String[] strChars = returnStr.split(EMPTY);

        int endIndex = 0;

        for (int i = strChars.length - 1; i >= 0; i--) {
            if (!(strChars[i].equals(HALF_SPACE) || strChars[i].equals(FULL_SPACE))) {
                endIndex = i;
                break;
            }
        }

        return returnStr.substring(0, endIndex);
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

}
