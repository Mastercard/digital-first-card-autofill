// Indetify the card type
function getCardType(number) {

    number = number.replace(/\s/g, '');

    // visa
    var re = new RegExp("^4");
    if (number.match(re) != null)
        return "Visa";

    // Mastercard
    // Updated for Mastercard 2017 BINs expansion
    if (/^(5[1-5][0-9]{14}|2(22[1-9][0-9]{12}|2[3-9][0-9]{13}|[3-6][0-9]{14}|7[0-1][0-9]{13}|720[0-9]{12}))$/.test(number))
        return "Mastercard";

    // AMEX
    re = new RegExp("^3[47]");
    if (number.match(re) != null)
        return "AMEX";

    // Discover
    re = new RegExp("^(6011|622(12[6-9]|1[3-9][0-9]|[2-8][0-9]{2}|9[0-1][0-9]|92[0-5]|64[4-9])|65)");
    if (number.match(re) != null)
        return "Discover";

    // Diners
    re = new RegExp("^36");
    if (number.match(re) != null)
        return "Diners";

    // Diners - Carte Blanche
    re = new RegExp("^30[0-5]");
    if (number.match(re) != null)
        return "Diners - Carte Blanche";

    // JCB
    re = new RegExp("^35(2[89]|[3-8][0-9])");
    if (number.match(re) != null)
        return "JCB";

    // Visa Electron
    re = new RegExp("^(4026|417500|4508|4844|491(3|7))");
    if (number.match(re) != null)
        return "VisaElectron";

    return "";
}

// form submit action
function save() {
    alert("Tap 'Remember this card' when asked by browser.");
}

// copy card number
function copyCNumber() {
    var cardNumberString = document.getElementById("cc-number").value;

    if (navigator.clipboard) {
        //For iOS safari 13.4 & Above
        navigator.clipboard.writeText(cardNumberString).then(function () {
            // Do something to indicate the copy succeeded
            dismissKeyboard();
            $('.toast').toast('show');
        }).catch(function () {
            // Do something to indicate the copy failed
            alert("Failed to copy the card number.")
        });
    } else {
        // Here's where you put the fallback code for older browsers.
        iosCopyToClipboardForLoweriOS(document.getElementById("cc-number"))
    }
}
//For iOS safari below 13.4
function iosCopyToClipboardForLoweriOS(el) {
    var oldContentEditable = el.contentEditable,
        oldReadOnly = el.readOnly,
        range = document.createRange();

    el.contentEditable = true;
    el.readOnly = false;
    range.selectNodeContents(el);

    var s = window.getSelection();
    s.removeAllRanges();
    s.addRange(range);

    el.setSelectionRange(0, 999999); // A big number, to cover anything that could be inside the element.

    el.contentEditable = oldContentEditable;
    el.readOnly = oldReadOnly;

    document.execCommand('copy');
    dismissKeyboard();
    $('.toast').toast('show');

}

//dissmiss the keyboard
function dismissKeyboard() {
    document.activeElement.blur();
}

// fill the form with card details
function fillUpForm() {
    // For testing hard coded card details is used. Card details should be fecthed from server in a secure manner.
    var cardNumber = "5436 0310 3060 6378";
    document.getElementById("cc-number").value = cardNumber;
    document.getElementById("cc-exp-month").value = "10";
    document.getElementById("cc-exp-year").value = "2024";
    document.getElementById("cc-csc").value = "257";
    var imageSrc = "static/images/" + getCardType(cardNumber) + ".png";
    document.getElementById("cardLogo").src = imageSrc;
}