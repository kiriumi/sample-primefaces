/**
 * ページを離れる際に、入力項目に変更があったら確認する
 * 使用方法
 * ・form要素のclass属性に「confirm-onunload」を指定する
 * ・確認が不要な処理を行う要素のclass属性に「not-confitm」を指定する
 */
$(function() {

    // フラグの初期化
    var confirm = false;
    var changed = false;
    var notConfirm = false;

    if ($(".confirm-beforeunload").length != 0) confirm = true;

    // ページの移動・更新を検知した時の処理
    $(window).on('beforeunload', function() {
        if (confirm && changed && !notConfirm) {
            return 'このページを離れます。\nよろしいですか？';
        }
        submit = false;
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
