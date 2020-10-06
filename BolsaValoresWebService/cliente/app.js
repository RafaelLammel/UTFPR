var base_url = 'http://localhost:8080'

var socket = new SockJS(`${base_url}/ws`);
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

var compraForm = document.getElementById("form-compra");
compraForm.addEventListener('submit', compra);
function compra(e) {
    e.preventDefault();
    let data = {
        id: document.getElementById("id-compra").value,
        idCliente: id,
        preco: document.getElementById("preco-compra").value,
        qtd: document.getElementById("qtd-compra").value,
        delay: document.getElementById("delay-compra").value
    }

    request.open('POST', `${base_url}/compra`, true);
    request.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    request.onload = function() {
        if(this.status >= 200 && this.status < 400) {
            console.log(this.response);
        }
    }
    request.send(JSON.stringify(data));
}

var vendaForm = document.getElementById("form-venda");
vendaForm.addEventListener('submit', venda);
function venda(e) {
    e.preventDefault();
    let data = {
        id: document.getElementById("id-venda").value,
        idCliente: id,
        preco: document.getElementById("preco-venda").value,
        qtd: document.getElementById("qtd-venda").value,
        delay: document.getElementById("delay-venda").value
    }

    request.open('POST', `${base_url}/venda`, true);
    request.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    request.onload = function() {
        if(this.status >= 200 && this.status < 400) {
            console.log(this.response);
        }
    }
    request.send(JSON.stringify(data));
}

var removeCotacaoForm = document.getElementById("form-remove-cotacao");
removeCotacaoForm.addEventListener('submit', removeCotacao);
function removeCotacao(e) {
    e.preventDefault();
    let idCotacao = document.getElementById("id-remove-cotacao").value;
    request.open('DELETE', `${base_url}/cliente/${id}/cotacao/${idCotacao}`);
    request.send();
}