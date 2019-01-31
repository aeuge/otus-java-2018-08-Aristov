var ws;

function getWebSocketPath(appendix) {
    var host = window.location.host;
    var pathArray = window.location.pathname.split( '/' );
    pathArray.pop();
        return "ws://" + host + pathArray.join("/") + "/" + appendix;

}

init = function () {
    ws = new WebSocket(getWebSocketPath("message"));
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