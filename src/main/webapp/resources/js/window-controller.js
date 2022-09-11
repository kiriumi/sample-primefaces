/**
 * Enterキーによるサブミットを無効化
 */
document.addEventListener("keydown", function(event) {

    var key = event.key;

    if (key !== "Enter") return;

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

/**
 * ページを離れる際に、入力項目に変更があったら確認する
 * 使用方法
 * ・form要素のclass属性に「confirm-leavepage」を指定する
 * ・確認が不要な処理を行う要素のclass属性に「not-confirm」を指定する
 */
$(function() {

    // フラグの初期化
    var confirm = false;
    var changed = false;
    var notConfirm = false;

    if ($(".confirm-leavepage").length != 0) confirm = true;

    // ページの移動・更新を検知した時の処理
    $(window).on('beforeunload', function() {
        if (confirm && changed && !notConfirm) {
            $('form').css('pointer-events', ''); // ダブルクリック抑止の解除
            return '';
        }
    });

    // 文字入力項目に変更があった時の処理
    // ※changeだと、文字入力が未確定でも変更されたと判断するため、keydownとkeyupにする
    $('input, textarea').on('keydown keyup', function() {
        changed = true;
    });

    // その他入力項目に変更があった時の処理
    $('input, select').change(function() {
        changed = true;
    });

    // 送信項目クリック時の処理
    $(".not-confirm").on('click', function() {
        notConfirm = true;
    });

});

function showBlockUi(widgetVar) {
    PrimeFaces.widgets[widgetVar].show();
}

function hideBlockUi(widgetVar) {
    PrimeFaces.widgets[widgetVar].hide();
}

/**
 * ダブルクリック抑止
 */
document.addEventListener("submit", function() {

    $('form').css('pointer-events', 'none');

    var url = location.pathname;
    var suffix = url.substring(url.indexOf("/", 1)).replaceAll("/", "_");
    var downloadCookieName = "primefaces.download" + suffix;

    const downloadTimer = window.setInterval(function () {
        const downloading = getCookie(downloadCookieName);
        if (downloading === "null") {
            $('form').css('pointer-events', '');
            clearInterval(downloadTimer);
        }
    }, 1000 );
});

/**
 * Cookie取得
 */
function getCookie(name) {
    return document.cookie.split('; ').find(row => row.startsWith(name)).split('=')[1];
}

/**
 * PrimeFacesの日本語カレンダー
 */
PrimeFaces.locales['ja'] = {
    closeText: '閉じる',
    prevText: '前月',
    nextText: '翌月',
    monthNames: ['１月', '２月', '３月', '４月', '５月', '６月', '７月', '８月', '９月', '１０月', '１１月', '１２月'],
    monthNamesShort: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
    dayNames: ['日曜', '月曜', '火曜', '水曜', '木曜', '金曜', '土曜'],
    dayNamesShort: ['日', '月', '火', '水', '木', '金', '土'],
    dayNamesMin: ['日', '月', '火', '水', '木', '金', '土'],
    weekHeader: '週',
    firstDay: 1,
    isRTL: false,
    showMonthAfterYear: true,
    yearSuffix: '年',
    timeOnlyTitle: '時間のみ',
    timeText: '時間',
    hourText: '時',
    minuteText: '分',
    secondText: '秒',
    currentText: '今日',
    ampm: false,
    month: '月',
    week: '週',
    day: '日',
    allDayText: '全日'
};
