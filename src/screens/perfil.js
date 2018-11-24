import React, { Component } from 'react';

import { StyleSheet, View, Text, AsyncStorage } from 'react-native';

export default class Cadastro extends Component{
    constructor(){
        super();
        this.state = {
            nome:""
        }
    }
    componentDidMount(){
        this.pegaNome();
    }
    async pegaNome() {
      try {
        const value = await AsyncStorage.getItem('NAME');
        if (value !== null) {
          // We have data!!
          console.log(value);
          this.setState({nome: value});
        }
       } catch (error) {
         // Error retrieving data
       }
    }
    static navigationOptions = {
        title: 'Perfil'
    }
    render(){
        return(
            const { navigate } = this.props.navigation;
            <View style={styles.container}>
                    <Text>Seja bem vindo(a) {this.state.nome}</Text>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        padding: 20
    },
    title: {
        fontSize: 40,
        color: 'black',
        marginBottom: 15
    },
    input: {
        height: 45,
        backgroundColor: '#FFF',
        alignSelf: 'stretch',
        borderColor: '#EEE',
        borderWidth: 1,
        paddingHorizontal: 20,
        marginBottom: 10
    },
    button: {
        height: 45,
        backgroundColor: '#990000',
        alignSelf: 'stretch',
        paddingHorizontal: 20,
        justifyContent: 'center',
        alignItems: 'center',
        marginBottom: 10
    },
    buttonText: {
        color: '#FFF',
        fontWeight: 'bold'
    },
});
