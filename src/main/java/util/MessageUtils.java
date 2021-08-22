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

        // メッセージのフォーマットを取得
        ResourceBundle bundle = ResourceBundle.getBundle(bundleName);
        String format = null;

        try {
            format = bundle.getString(id);

        } catch (MissingResourceException e) {

            String subsystemId = id.substring(0, id.indexOf("."));
            bundle = ResourceBundle.getBundle(String.join("_", subsystemId.toUpperCase(), bundleName));
            format = bundle.getString(id);
        }

        // パラメータを置換したログメッセージを返す
        return StringUtils.isBlank(format) ? "" : MessageFormat.format(format, params);
    }

}
