import { createStackNavigator } from 'react-navigation';

import Home from './pages/home';
import Login from './pages/login';
import Cadastro from './pages/cadastro'

export default createStackNavigator({
    Home,
    Login,
    Cadastro
});
