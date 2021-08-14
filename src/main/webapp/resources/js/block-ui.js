function showBlockUi() {
    PF('bui').show();
}

function hideBlockUi() {
    PF('bui').hide();
}

document.addEventListener("submit", function() {
    $('form').css('pointer-events', 'none');
    PF('bui').show();
});
