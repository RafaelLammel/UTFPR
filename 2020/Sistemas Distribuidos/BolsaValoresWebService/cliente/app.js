// Inicializa uma conexão com o websocket do servidor
var base_url = 'http://localhost:8080'
var socket = new SockJS(`${base_url}/ws`);
var stompClient = Stomp.over(socket);

var id = 0;
var listaMensagens = document.getElementById("lista-mensagens-body");

/**
 * Primeira requisição, executada sempre que o cliente entra,
 * para gerar seu Id, suas ações iniciais e inscrever o cliente
 * no seu canal do websocket
 */
var request = new XMLHttpRequest();
request.open('POST', `${base_url}/cliente`, true);
request.onload = function() {
    if(this.status >= 200 && this.status < 400) {
        id = this.response;
        stompClient.connect({}, function (frame) {
            // Inscreve o cliente no seu canal de comunicação exclusiva do websocket,
            // dessa forma o servidor consegue enviar notificações nesse canal
            stompClient.subscribe(`/user/${id}/notifica`, function (res) {
                console.log(res);
                listaMensagens.innerHTML += `<p class="mensagem">${res.body}</p>`;
            });
        });
    }
};
request.send();

/**
 * Função que bate no endpoint GET da carteira e retorna a carteira atual do cliente
 */
function getCarteira() {
    request.open('GET', `${base_url}/cliente/${id}/carteira`, true);
    request.onload = function() {
        if(this.status >= 200 && this.status < 400) {
            document.getElementById("title-card").innerHTML = "Carteira"
            let res = JSON.parse(this.response);
            console.log(res);
            let html = `
            <table class="table table-bordered table-sm">
                <thead>
                    <tr>
                        <th scope="col">ID</th>
                        <th scope="col">Quantidade</th>
                    </tr>
                </thead>
                <tbody>`;
            res.forEach(e => {
                html += `
                <tr>
                    <td>${e.id}</td>
                    <td>${e.qtd}</td>
                </tr>`
            });
            html += `
                </tbody>
            </table>
            `
            document.getElementById("body-card").innerHTML = html;
        }
    }
    request.send();
}

/**
 * Função que bate no endpoint GET de cotações e retorna a lista de cotações
 * atual do cliente
 */
function getCotacao() {
    request.open('GET', `${base_url}/cliente/${id}/cotacao`, true);
    request.onload = function() {
        if(this.status >= 200 && this.status < 400) {
            document.getElementById("title-card").innerHTML = "Cotações"
            let res = JSON.parse(this.response);
            console.log(res);
            let html = `
            <table class="table table-bordered table-sm">
                <thead>
                    <tr>
                        <th scope="col">ID</th>
                        <th scope="col">Valor</th>
                    </tr>
                </thead>
                <tbody>`;
            for(let key in res) {
                if(res.hasOwnProperty(key))
                    html += `
                    <tr>
                        <td>${key}</td>
                        <td>${res[key]}</td>
                    </tr>`
            }
            html += `
                </tbody>
            </table>
            `
            document.getElementById("body-card").innerHTML = html;
        }
    }
    request.send();
}


/**
 * Função que bate no endpoint GET de interesse e retorna a lista de interesses atual
 * do cliente
 */
function getListaInteresse() {
    request.open('GET', `${base_url}/cliente/${id}/interesse`, true);
    request.onload = function() {
        if(this.status >= 200 && this.status < 400) {
            document.getElementById("title-card").innerHTML = "Interesses"
            let res = JSON.parse(this.response);
            console.log(res);
            let html = `
            <table class="table table-bordered table-sm">
                <thead>
                    <tr>
                        <th scope="col">ID</th>
                        <th scope="col">Teto</th>
                        <th scope="col">Piso</th>
                    </tr>
                </thead>
                <tbody>`;
            res.forEach(e => {
                html += `
                <tr>
                    <td>${e.id}</td>
                    <td>${e.teto}</td>
                    <td>${e.piso}</td>
                </tr>`
            })
            html += `
                </tbody>
            </table>
            `
            document.getElementById("body-card").innerHTML = html;
        }
    }
    request.send();
}

/**
 * Função disparada pelo form da cotação, que registra uma cotação na lista de cotações
 * do cliente
 */
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

/**
 * Função disparada pelo form da interesse, que registra uma ação na lista de interesses
 * do cliente
 */
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

/**
 * Função disparada pelo form da compra, que registra uma oferta de compra no servidor
 */
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

/**
 * Função disparada pelo form da venda, que registra uma oferta de venda no servidor
 */
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

/**
 * Função disparada pelo form da deletar cotacao, que remove uma cotação
 * da lista de cotações do cliente
 */
var removeCotacaoForm = document.getElementById("form-remove-cotacao");
removeCotacaoForm.addEventListener('submit', removeCotacao);
function removeCotacao(e) {
    e.preventDefault();
    let idCotacao = document.getElementById("id-remove-cotacao").value;
    request.open('DELETE', `${base_url}/cliente/${id}/cotacao/${idCotacao}`);
    request.send();
}