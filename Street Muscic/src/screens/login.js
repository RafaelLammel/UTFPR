import React, { Component } from 'react';
import api from '../services/api';

import { View, Text, TextInput, StyleSheet, TouchableOpacity, AsyncStorage } from 'react-native';

export default class Login extends Component{
    constructor(){
        super();
        this.state = {
            email: "",
            password: "",
        }
    }
    static navigationOptions = {
        title: 'Login'
    }
    async onLoginPress(){
        const {navigate} = this.props.navigation;
        if (this.state.email == ""){
            alert("E-mail é obrigatório.");
            return;
        }
        else if (this.state.password == ""){
            alert("Senha é obrigatório.");
            return;
        }
        var res;
        const response = await api.get('/usuarios/' + this.state.email)
        .then(function (response){
            //console.log(response);
            res = response;
        })
        .catch(function (error){
            console.log(error);
        });
        if(res.data[0] == null){
            alert("Credenciais invalidas!");
            return;
        }
        else if (res.data[0].senha != this.state.password) {
          alert("Credenciais invalidas!");
          return;
        }
        else if(res.data[0].senha == this.state.password){
            try {
              await AsyncStorage.setItem('NAME', res.data[0].nome);
              await AsyncStorage.setItem('EMAIL', res.data[0].email);
              await AsyncStorage.setItem('ID', res.data[0].id.toString());
            } catch (error) {
              console.log(error);
            }
            return navigate("Perfil");
        }
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
                    onChangeText={(val) => this.setState({email: val})}
                />
                <TextInput
                    style={styles.input}
                    placeholder="Digite sua senha"
                    value={this.state.password}
                    onChangeText={(val) => this.setState({password: val})}
                    secureTextEntry={true}
                />
                <TouchableOpacity style={styles.button} onPress={this.onLoginPress.bind(this)}>
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
