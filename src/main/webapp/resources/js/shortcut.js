/**
 * カスタマイズするショートカットキー
 */

/**
 * 現在のページの再読込みを無効化
 */
shortcut.add("F5", function() {
    return false;
});

/**
 * 現在のページの再読込みを無効化
 */
shortcut.add("Ctrl+R", function() {
    return false;
});

/**
 * キャッシュを無視した現在のページの再読込みを無効化
 */
shortcut.add("Ctrl+F5", function() {
    return false;
});

/**
 * キャッシュを無視した現在のページの再読込みを無効化
 * ※Chrome, Edge
 */
shortcut.add("Shift+F5", function() {
    return false;
});

/**
 * キャッシュを無視した現在のページの再読込みを無効化
 * FireFox
 */
shortcut.add("Ctrl+Shift+R", function() {
    return false;
});
