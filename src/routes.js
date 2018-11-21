const express = require('express');
const routes = express.Router();

//Rotas para usuarios;
const usuariosController = require('./controllers/UsersController');
//Retorna todos os usuarios;
routes.get('/usuarios',usuariosController.index);
routes.post('/usuarios',usuariosController.store);
routes.get('/usuarios/:email',usuariosController.login);
routes.put('/usuarios/:id',usuariosController.update);


module.exports = routes;
