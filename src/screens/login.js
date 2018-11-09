import React, { Component } from 'react';

import { View, Text, TextInput, StyleSheet, TouchableOpacity } from 'react-native';

export default class Login extends Component{
    state = {
        email: '',
        senha: '',
    };
    static navigationOptions = {
        title: 'Login'
    }
    render(){
        const { navigate } = this.props.navigation;
        return(
            <View style={styles.container}>
                <Text style={styles.title}>Street Music</Text>
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
                    <Text style={styles.buttonText}>Logar</Text>
                </TouchableOpacity>
                <TouchableOpacity style={styles.button} onPress={() => navigate('Cadastro')}>
                    <Text style={styles.buttonText}>Cadastro</Text>
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
        color: 'blue',
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
