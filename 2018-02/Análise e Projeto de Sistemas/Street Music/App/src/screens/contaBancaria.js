import React, { Component } from 'react';
import api from '../services/api'

import { View, Text, StyleSheet, TouchableOpacity, TextInput, AsyncStorage } from 'react-native';

export default class ContaBancaria extends Component{
  constructor(){
      super();
      this.state={
        conta:'',
        agencia:''
      }
  }
  static navigationOptions = {
      title: 'Conta Bancária'
  }
  async cadastraConta(){
    if (this.state.conta == ""){
        alert("Conta é obrigatório.");
        return;
    }
    else if (this.state.agencia == ""){
        alert("Agencia é obrigatório.");
        return;
    }
    var value;
      try{
      value = await AsyncStorage.getItem('ID');
      if (value !== null) {
        //console.log(value);
      }
     } catch (error) {
       console.log(error);
    }
    console.log(value);
    var temp;

    const res = await api.get('/financeira/' + value)
    .then(function (res){
        //console.log(response);
        temp = res;
    })
    .catch(function (error){
        console.log(error);
    });
    console.log(temp.data[0].ID);
    const response = await api.post('/contabancaria',{
        conta: this.state.conta,
        agencia: this.state.agencia,
        ID: temp.data[0].ID
    })
    .then(function (response){
        console.log(response);
        alert("Conta Bancária cadastrada com sucesso!");
    })
    .catch(function (error){
        console.log(error);
    });
  }
  render(){
      const { navigate } = this.props.navigation;
      return(
          <View style={styles.container}>
            <Text style={styles.title}>Cadastro</Text>
            <TextInput
                style={styles.input}
                placeholder="Digite o numero da conta"
                value={this.state.conta}
                onChangeText={(val) => this.setState({conta: val})}
            />
            <TextInput
                style={styles.input}
                placeholder="Digite o numero do agência"
                value={this.state.agencia}
                onChangeText={(val) => this.setState({agencia: val})}
            />
            <TouchableOpacity style={styles.button} onPress={this.cadastraConta.bind(this)}>
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
