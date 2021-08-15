function showBlockUi() {
    PrimeFaces.widgets['bui'].show();
}

function hideBlockUi() {
    PrimeFaces.widgets['bui'].hide();
}

document.addEventListener("submit", function() {
    $('form').css('pointer-events', 'none');
    PrimeFaces.widgets['bui'].show();
});
