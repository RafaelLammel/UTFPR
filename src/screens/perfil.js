import React, { Component } from 'react';

import { StyleSheet, View, Text, AsyncStorage, TouchableOpacity } from 'react-native';

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
          //console.log(value);
          this.setState({nome: value});
        }
       } catch (error) {
         console.log(error);
       }
    }
    static navigationOptions = {
        title: 'Perfil'
    }
    render(){
        const {navigate} = this.props.navigation;
        return(
            <View style={styles.container}>
              <Text>Seja bem vindo(a) {this.state.nome}!</Text>
              <TouchableOpacity style={styles.button} onPress={() => navigate('EditarPerfil')}>
                  <Text style={styles.buttonText}>Editar Perfil</Text>
              </TouchableOpacity>
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
