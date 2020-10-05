var base_url = 'http://localhost:8080'

var socket = new SockJS('http://localhost:8080/ws');
var stompClient = Stomp.over(socket);

var id = 0;

var request = new XMLHttpRequest();
request.open('POST', `${base_url}/cliente`, true);
request.onload = function() {
    if(this.status >= 200 && this.status < 400) {
        id = this.response;
        stompClient.connect({}, function (frame) {
            stompClient.subscribe(`/user/${id}/notifica`, function (res) {
                console.log(res);
            });
        });
    }
};
request.send();

function getCarteira() {
    request.open('GET', `${base_url}/cliente/${id}/carteira`, true);
    request.onload = function() {
        if(this.status >= 200 && this.status < 400) {
            console.log(this.response);
        }
    }
    request.send();
}

function getCotacao() {
    request.open('GET', `${base_url}/cliente/${id}/cotacao`, true);
    request.onload = function() {
        if(this.status >= 200 && this.status < 400) {
            console.log(this.response);
        }
    }
    request.send();
}

function getListaInteresse() {
    request.open('GET', `${base_url}/cliente/${id}/interesse`, true);
    request.onload = function() {
        if(this.status >= 200 && this.status < 400) {
            console.log(this.response);
        }
    }
    request.send();
}

var cotacaoForm = document.getElementById("form-cotacao");
cotacaoForm.addEventListener('submit', registraCotacao);
function registraCotacao(e) {
    e.preventDefault();
    let data = {
        id: document.getElementById("id-cotacao").value
    }

    request.open('POST', `${base_url}/cliente/${id}/cotacao`, true);
    request.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    request.onload = function() {
        if(this.status >= 200 && this.status < 400) {
            console.log(this.response);
        }
    }
    request.send(JSON.stringify(data));
}

var interesseForm = document.getElementById("form-interesse");
interesseForm.addEventListener('submit', registraInteresse);
function registraInteresse(e) {
    e.preventDefault();
    let data = {
        id: document.getElementById("id-interesse").value,
        teto: document.getElementById("teto").value,
        piso: document.getElementById("piso").value,
    }

    request.open('POST', `${base_url}/cliente/${id}/interesse`, true);
    request.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    request.onload = function() {
        if(this.status >= 200 && this.status < 400) {
            console.log(this.response);
        }
    }
    request.send(JSON.stringify(data));
}