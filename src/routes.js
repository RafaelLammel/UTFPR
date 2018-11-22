import { createStackNavigator } from 'react-navigation';

import Login from './screens/login';
import Cadastro from './screens/cadastro'
import Perfil from './screens/perfil'

export default createStackNavigator({
    Login,
    Cadastro,
    Perfil
});
