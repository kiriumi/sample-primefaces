function openChildWindow(url, windowName, windowFeature) {

    var parentToken = document.getElementById("token").value;
    var namespace = Math.random().toString(32).substring(2);

    var childWindow = window.open(url + '?token=' + parentToken + '&childTokenNamespace=' + namespace, windowName, windowFeature);
    childWindow.focus();

    return childWindow;
}

/**
 * 子画面を閉じた後、親画面の要素をクリックしてアクションを実行する
 * ※例：<p: commandButton ajax="true" value="閉じる" action="#{sample.close()}" onsuccess="closeChildWindowWithAction('action')" />
 */
function closeChildWindow(elementId) {
    window.opener.document.getElementById(elementId).click();
    window.close();
    window.opener.focus();
}
