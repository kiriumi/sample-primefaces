/**
 * Enterキーによるサブミットを無効化
 */
document.addEventListener("keydown", function(event) {

    var key = event.key;

    if (key !== 13) return;

    var elem = event.target;

    if (elem.type === "button" || elem.type === "submit" || elem.type === "textarea") {
        event.returnValue = true;
        return true;
    }

    var elemTagName = elem.tagName.toLowerCase();
    if (elemTagName == "a") {
        event.returnValue = true;
        return true;
    }
    if (event.priventDefault) {
        event.priventDefault();
    }
    event.cancelBubble = true;

    event.returnValue = false;
    return false;

}, false)

/**
 * コンテキストメニュー表示の無効化
 */
/*
document.addEventListener("contextmenu", function(event) {

    var elem = event.target;
    if ((elem.type === "text" || elem.type === "textarea" || elem.type === "password") && elem.disabled === false) {
        event.returnValue = true;
        return true;
    }
    event.returnValue = false;
    return false;

}, false)
*/
