package util;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

public class MessageUtils {

    private final static String bundleName = "Messages";

    private MessageUtils() {
    }

    public static String getMessage(final String id, Object... params) {

        ResourceBundle bundle;
        String format;

        try {
            // 業務固有のメッセージフォーマットを取得
            String subsystemId = id.substring(0, id.indexOf("."));
            bundle = ResourceBundle.getBundle(String.join("_", subsystemId.toUpperCase(), bundleName));
            format = bundle.getString(id);

        } catch (MissingResourceException e) {

            // 共通のメッセージフォーマットを取得
            bundle = ResourceBundle.getBundle(bundleName);
            format = bundle.getString(id);
        }

        // パラメータを置換したログメッセージを返す
        return StringUtils.isBlank(format) ? "" : MessageFormat.format(format, params);
    }

}
