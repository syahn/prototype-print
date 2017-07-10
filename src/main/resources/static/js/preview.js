/**
 * Created by NAVER on 2017-07-07.
 */

var myForm = document.getElementById('form-print');
var printScreenButton = document.getElementById('button__print-screen');
var printButton = document.getElementById('button-print');
var printCancelButton = document.getElementById('button__print-cancel');

myForm.onsubmit = function() {
    var w = window.open('about:blank','Popup_Window','toolbar=0,scrollbars=0,location=0,statusbar=0,menubar=0,resizable=0,width=400,height=300,left = 312,top = 234');
    this.target = 'Popup_Window';
};

printScreenButton.onclick = function () {
    window.print();
}

printButton.onclick = function() {
    window.close();
    console.log('click');
};

printCancelButton.onclick = function() {
    window.close();
    console.log('click');
};