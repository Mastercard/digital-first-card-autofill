# Reference code to save payment card details in mobile browser autofill 

## Table of Contents
- [Overview](#overview)
* [The Challenge](#challenge)
* [Solution](#solution)
- [Usage Guide](#usage)
* [Prerequisites](#prerequisites)
* [Compatibility](#compatibility)
* [Create card details web page](#web)
* [iOS](#ios)
* [Android](#android)
* [Build and Run](#build-run)



## Overview <a name="overview"></a>

### The Challenge <a name="challenge"></a>
Today consumers that have a digital card in their mobile banking app on a mobile device and want to use it for eCommerce on the same device are forced to go through a sub-optimal User Experience. They have to swap multiple times between the mobile banking app where they read card details and the merchant website or app where they manually tap them in.

### Solution <a name="solution"></a>
Solution to the above issue is, if user store the card details in browser autofill, After that whenever they go to checkout page on any merchant site, browser will offer to autofill the card details. This step still requires manual entry of the card details to browser autofill settings and to perform that user need to switch multiple times between issuer app and browser settings. To avoid that we need some way by which we can directly store the card details to browser's autofill storage. Unfortunately there is no open api available in iOS & Android by which we can store the card details directly in browser's autofill storage.

This is a reference code to demonstrate how HTML page can be used in native apps to store the card details in mobile browser autofill. 

Every time when we perform checkout on merchant website, browser identifies the card details and offers to save the card details. If we hit save on the pop up, this card details will get saved into browser's autofill storage and then after every time whenever we will perform checkout, browser will offer to autofill the saved card details into checkout form.

We can use browser's this feature in our iOS and Android native apps and can store card details directly from native apps to browser's autofill storage.


## Usage Guide<a name="usage"></a>

### Prerequisites <a name="prerequisites"></a>:
1. Secure server
2. XCode
3. Android Studio

### Compatibility <a name="compatibility"></a>
iOS 12.0 onwards
Android 10.0 onwards


### 1. Create card details web page <a name="web"></a>:
To build the solution described above, 

we need to set up secure web page with the form which has prefilled card details in it. We will design it in a such way so it will look like card details page and also we have to restrict use interaction on the form so card details can't be edited.

When we submit the card details form, browser will intercept the request and asks user to save the card in browser's autofill storage.

#### Structure:
**card.html & main.css** : Contains card input fields and style
**autofill.js** : Contains script which fill the form with card details and provides functions to copy the card number.
**images** : Card type logos

#### card.html
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
        integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z" crossorigin="anonymous">

    <!-- Local CSS -->
    <link rel="stylesheet" type="text/css" href="static/css/main.css">
    <script src="static/js/autofill.js"></script>

    <!-- Optional JavaScript -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"
        integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj"
        crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"
        integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN"
        crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"
        integrity="sha384-B4gt1jrGC7Jh4AgTPSdUtOBvfO8shuf57BaghqFfPlYxofvL8/KUEfYiJOMMV+rV"
        crossorigin="anonymous"></script>

    <!-- Call JS to fill up card details -->
    <script>
        window.onload = function () { fillUpForm(); };
    </script>
</head>
<body>
    <form id="form1" autocomplete="on">
        <div style="margin-top:120px;">
            <div name="cardDetails" class="overlay-container cardBackground align-items-center">
                <div class="row justify-content-start">
                    <div class="col">
                        <input class="cardFields cardNumberField" name="cc-number" id="cc-number"
                            autocomplete="cc-number">
                    </div>
                </div>
                <div class="row justify-content-start no-gutters">
                    <div class="col-4">
                        <div class="row justify-content-start no-gutters">
                            <label class="cardFieldLabel">Expiry</label>
                        </div>
                        <div class="row justify-content-start no-gutters">
                            <input class="col-3 cardFields cardExpFieldMonth" name="cc-exp-month" id="cc-exp-month"
                                autocomplete="cc-exp-month">
                            <input class="col-5 cardFields cardExpFieldYear" name="cc-exp-year" id="cc-exp-year"
                                autocomplete="cc-exp-year">
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row justify-content-start no-gutters">
                            <label class="cardFieldLabel">CVC</label>
                        </div>
                        <div class="row justify-content-start no-gutters">
                            <input class="cardFields" name="cc-csc" id="cc-csc" autocomplete="cc-csc">
                        </div>
                    </div>
                </div>
                <img class="cardLogo" id="cardLogo" src="static/images/Mastercard.png">
                <!-- overlay div to disable user interaction -->
                <div name="transparent-overlay" class="overlay-child"></div>
            </div>
            <div style="margin-top:40px; display: block; margin-left: auto; margin-right: auto; width: 300px">
                <input class="btn btn-primary btn-block" type="submit" value="Save" onclick="save()">
                <input class="btn btn-secondary btn-block" type="button" value="Copy Card Number"
                    onclick="copyCNumber()">
                <div class="toast">
                    <div class="toast-body">
                        Copied!
                    </div>
                </div>
            </div>

        </div>
    </form>
</body>
</html>

```
#### main.css
```css
body {
    margin-bottom: 50px;
    font-family: Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
}
.cardBackground {
    padding-top: 65px;
    width: 300px;
    height: 200px;
    background-color: #1133dd;
    border: 1px solid #000000;
    border-radius: 20px;
    background-image: linear-gradient(17deg, #1e5799 0%,#1e5799 12%,#207cca 37%,#2989d8 51%,#2989d8 64%,#2989d8 83%,#7db9e8 100%);
    overflow: hidden;
}
.overlay-container {
    position: relative;
    left: 50%;
    transform: translateX(-50%);
}
.overlay-child {
    position: absolute;
    top: 0;
    left: 0;
    background-color: green;
    opacity:0.0;
    width: 300px;
    height: 200px;
}
.cardFields {
    margin-left: 20px;
    margin-bottom: 5px;
    margin-top: 5px;
    background:transparent;
    border:0;
    outline:0;
    color: white;
    font-size: large;
}
.cardExpFieldMonth {
    margin-left: 30px;
    width:30px;
}
.cardExpFieldYear {
    margin-left: 0px;
    padding-left: 0px;
    padding-right: 0px;
    width:50px;
}
.cardNumberField {
    font-size: x-large;
}
.cardFieldLabel {
    margin-left: 30px;
    margin-top: 5px;
    background:transparent;
    color: white;
    font-size: small;
    height: 5px;
    float: left;
}
.cardLogo {
    width: 67px;
    height: 43px;
    position: absolute;
    bottom: 10px;
    right: 10px;
}


```
#### autofill.js

```javascript
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
```

There are several things which we need to follow to make this solution work:

1. **Input name attributes** : Browser identifies payment card field by input element's "name" attribute. We have to use specific defined attributes as a name for each card detail field. For example : Card Number field is named with "cc-number" and Expiry-month field is named with "cc-exp-month".
2. **Enable Autocomplete** : Even though we are not asking user to enter any card details, we have to keep auto complete enable.
3. **Disable user interaction** : Since we have card details prefilled, we don't want user to edit it. We can't simply use "readonly" on the input elements. Browser don't offer a "save card" pop up if the input elements are marked as read only. To disable the user interaction, we need to add a transparent view over the card input fields. refer "cardDetails" div in above html code. After adding card input element we have added another div "transparent-overlay" which is covering all the input fields.
4. **Submit the form** : Even though we are not sending card details to backend, we need to submit the form because browser only offers "save card" pop up when it intercepts form submission. 
5. **Fetch card details securely** : For testing this reference app, Test card details are hard coded in the autofill.js file. For production app, card details must be fetched from secure server in a secure manner. For example: Encrypted card details can be fetched from backend, decrypt it and then filled in to the form fields through java script.
6. **https** : Deploy the web page on secure server. For Local html page or http url browser will not offer the "save card" pop up.

### iOS <a name="ios"></a>:
To load the page in iOS native app, we have to use SFSafariViewController. SFSafariController shares the same web engine which is being used by native safari app. So whenever we save the card through SFSafariViewController, card details will be saved into safari's autofill cache storage.
    
Below is the sample code which shows how to open the html page into native iOS app:

```
    var safariViewController:SFSafariViewController? =  SFSafariViewController.init(url: URL(string: "<<Secure URL to Card details page>>")!)
    self.safariViewController?.dismissButtonStyle = .close
    self.safariViewController?.preferredBarTintColor = .black
    self.safariViewController?.preferredControlTintColor = .white
    self.safariViewController?.delegate = self
    self.safariViewController?.modalPresentationStyle = .overFullScreen
    self.present(self.safariViewController!, animated: true, completion: nil)  
```
### Android <a name="android"></a>:
To load the page in Android native app, we have to use Chrome custom tabs. Chrome custom tab shares the same web engine which is being used by native chrome browser app. So whenever we save the card through Chrome custom tabs, card details will be saved into Chrome's autofill cache storage.

Below is the sample code which shows how to open the html page into native Android app:

```
    String url = ¨<<Secure URL to Card details page>>¨;
    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
    CustomTabsIntent customTabsIntent = builder.build();
    customTabsIntent.launchUrl(this, Uri.parse(url));
```
More details are available on here: https://developers.google.com/web/android/custom-tabs

## Build & Run <a name="build-run"></a>:

To run the sample iOS OR Android app please follow below steps:
1. Deploy the "web" directory to your secure server from where you can access the card.html page via https url.
2. For iOS, Go to the ViewController.swift and change the URL to the card.html page hosted on your server.
    OR
   For Android, Go to Strings.xml and change the URL to the card.html page.
3. Build and run the app on device. On Simulator the feature might not work.
4. First screen of the app is card list screen. Tap on the card. This will launch the Card Details HTML page.
5. Tap Save button and follow the steps.
6. Once completed, card details will be stored in safari/chrome autofill storage.
7. Oen any merchant's checkout page in phone's browser app: Safari App(iOS) OR Chrome App(Android). 
8. Saved card details will be presented to autofill the merchant checkout page.


