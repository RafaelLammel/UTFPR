import React, { Component } from 'react';
import api from '../services/api'

import { StyleSheet, View, Text, TextInput, TouchableOpacity } from 'react-native';

export default class Cadastro extends Component{
    constructor(){
        super();
        this.state = {
            name: "",
            email: "",
            password: "",
        }
    }
    static navigationOptions = {
        title: 'Cadastro'
    }
    async onRegisterPress(){

        if (this.state.email == ""){
            alert("E-mail é obrigatório.");
            return;
        }
        else if (this.state.name == ""){
            alert("Nome é obrigatório.");
            return;
        }
        else if (this.state.password == ""){
            alert("Senha é obrigatório.");
            return;
        }
        const response = await api.post('/usuarios',{
            nome: this.state.name,
            email: this.state.email,
            senha: this.state.password,
            musico: 0
        })
        .then(function (response){
            console.log(response);
            alert("Cadastro efetuado com sucesso!");
        })
        .catch(function (error){
            console.log(error);
        });

        var temp;
        const res = await api.get('/usuarios/' + this.state.email)
        .then(function (res){
            //console.log(response);
            temp = res;
        })
        .catch(function (error){
            console.log(error);
        });
        const send = await api.post('/financeira',{
            id: temp.data[0].id
        })
        .then(function (temp){
            console.log(temp);
        })
        .catch(function (error){
            console.log(error);
        });
    }
    render(){
        return(
            <View style={styles.container}>
                <Text style={styles.title}>Cadastro</Text>
                <TextInput
                    style={styles.input}
                    placeholder="Digite seu nome"
                    value={this.state.nome}
                    onChangeText={(val) => this.setState({name: val})}
                />
                <TextInput
                    style={styles.input}
                    placeholder="Digite seu e-mail"
                    value={this.state.email}
                    onChangeText={(val) => this.setState({email: val})}
                />
                <TextInput
                    style={styles.input}
                    placeholder="Digite sua senha"
                    value={this.state.senha}
                    onChangeText={(val) => this.setState({password: val})}
                    secureTextEntry={true}
                />
                <TouchableOpacity style={styles.button} onPress={this.onRegisterPress.bind(this)}>
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
