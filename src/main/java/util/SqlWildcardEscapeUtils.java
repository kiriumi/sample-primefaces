package util;

public class SqlWildcardEscapeUtils {

    public static String postgres(String sql) {
        return sql.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
    }

}
