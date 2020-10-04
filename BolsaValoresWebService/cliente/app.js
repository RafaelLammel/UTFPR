var base_url = 'http://localhost:8080'

var socket = new SockJS('http://localhost:8080/ws');
var stompClient = Stomp.over(socket);

var request = new XMLHttpRequest();
request.open('POST', `${base_url}/cliente`, true);
request.onload = function() {
    if(this.status >= 200 && this.status < 400) {
        let response = this.response;
        stompClient.connect({}, function (frame) {
            stompClient.subscribe(`/user/${response}/notifica`, function (greeting) {
                console.log(greeting);
            });
        });
    }
};
request.send();