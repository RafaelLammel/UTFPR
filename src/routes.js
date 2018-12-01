const express = require('express');
const routes = express.Router();

//Rotas para usuarios;
const usersController = require('./controllers/UsersController');
routes.get('/usuarios',usersController.index);
routes.post('/usuarios',usersController.store);
routes.get('/usuarios/:email',usersController.login);
routes.put('/usuarios/:id',usersController.update);

//Rotas para conta financeira
const financialsController = require('./controllers/FinancialsController');
routes.post('/financeira',financialsController.store);
routes.get('/financeira/:id',financialsController.getFinancial);

//Rotas para conta bancária;
const creditCardController = require('./controllers/CreditCardController');
routes.post('/cartaocredito',creditCardController.store);

//Rotas para cartão de crédito;
const bankAccountController = require('./controllers/bankAccountController');
routes.post('/contabancaria',bankAccountController.store);


module.exports = routes;
