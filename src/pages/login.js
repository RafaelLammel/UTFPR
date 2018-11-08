import React, { Component } from 'react';

import { View, Text } from 'react-native';

export default class Main extends Component{
    static navigationOptions = {
        title: 'Login'
    }
    render(){
        return(
            <View>
                <Text>Pagina Login</Text>
            </View>
        );
    }
}
