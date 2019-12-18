USE anjo;

CREATE TABLE IF NOT EXISTS usuario(
	id INT AUTO_INCREMENT PRIMARY KEY,
    login VARCHAR(255) UNIQUE NOT NULL,
    nome VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS imagem(
	id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT,
    algoritmo VARCHAR(50),
    data_inicio DATETIME,
    data_termino DATETIME,
    tamanho VARCHAR(10),
    iteracoes INT,
    status int,
    caminho_imagem varchar(20),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);