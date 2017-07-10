/**
 * Created by NAVER on 2017-07-07.
 */

var previewForm = document.getElementById('form-print');

previewForm.onsubmit = function() {
    var w = window.open('/preview','Popup_Window','resizable=1,width=526,height=715');
    this.target = 'Popup_Window';
};




