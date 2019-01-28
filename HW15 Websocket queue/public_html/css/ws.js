var ws;

init = function () {
    ws = new WebSocket("ws://localhost:8080/HW15-wsq/message");
    ws.onopen = function (event) {

    }
    ws.onmessage = function (event) {
        var resultField = document.getElementById("resultws");
        resultField.innerHTML = "Результат запроса: " + event.data;
    }
    ws.onclose = function (event) {

    }
};

function sendMessage() {
    var messageField = document.getElementById("idws");
    var message = messageField.value;
    ws.send(message);
    messageField.value = '';
}