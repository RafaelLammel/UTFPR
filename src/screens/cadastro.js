import React, { Component } from 'react';

import { StyleSheet, View, Text, TextInput, TouchableOpacity } from 'react-native';

export default class Cadastro extends Component{
    state = {
        nome: '',
        email: '',
        senha: '',
    };
    static navigationOptions = {
        title: 'Cadastro'
    }
    render(){
        return(
            <View style={styles.container}>
                <Text style={styles.title}>Cadastro</Text>
                <TextInput
                    style={styles.input}
                    placeholder="Digite seu nome"
                    value={this.state.nome}
                    onChangeText={nome=>this.setState({ nome })}
                />
                <TextInput
                    style={styles.input}
                    placeholder="Digite seu e-mail"
                    value={this.state.email}
                    onChangeText={email=>this.setState({ email })}
                />
                <TextInput
                    style={styles.input}
                    placeholder="Digite sua senha"
                    value={this.state.senha}
                    onChangeText={senha=>this.setState({ senha })}
                />
                <TouchableOpacity style={styles.button} onPress={() => {}}>
                    <Text style={styles.buttonText}>Cadastrar</Text>
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
        backgroundColor: '#b7b7b7',
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
