import { createStackNavigator } from 'react-navigation';

import Login from './screens/login';
import Cadastro from './screens/cadastro';
import Perfil from './screens/perfil';
import EditarPerfil from './screens/editarPerfil';
import ContaFinanceira from './screens/contaFinanceira'
import CartaoCredito from './screens/cartaoCredito'
import ContaBancaria from './screens/contaBancaria'

export default createStackNavigator({
    Login,
    Cadastro,
    Perfil,
    EditarPerfil,
    ContaFinanceira,
    CartaoCredito,
    ContaBancaria
});
