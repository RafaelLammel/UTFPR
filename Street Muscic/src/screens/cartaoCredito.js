import React, { Component } from 'react';
import api from '../services/api'

import { View, Text, StyleSheet, TouchableOpacity, TextInput, AsyncStorage } from 'react-native';

export default class CartaoCredito extends Component{
    constructor(){
        super();
        this.state={
          nome:'',
          numero:'',
          validade:'',
          codigoSeg:''
        }
    }
    static navigationOptions = {
        title: 'Cartão de Crédito'
    }
    async cadastraCartao(){
      if (this.state.nome == ""){
          alert("Nome é obrigatório.");
          return;
      }
      else if (this.state.numero == ""){
          alert("Numero do cartão é obrigatório.");
          return;
      }
      else if (this.state.validade == ""){
          alert("Validade do cartão é obrigatório.");
          return;
      }
      else if (this.state.codigoSeg == ""){
          alert("Código de segurança é obrigatório.");
          return;
      }
      var value;
        try{
        value = await AsyncStorage.getItem('ID');
        if (value !== null) {
          //console.log(value);
          this.setState({email: value});
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
      const response = await api.post('/cartaocredito',{
          nome: this.state.nome,
          numero: this.state.numero,
          validade: this.state.validade,
          numeroDeSeguranca: this.state.codigoSeg,
          ID: temp.data[0].ID
      })
      .then(function (response){
          console.log(response);
          alert("Cartão de crédito cadastrado com sucesso!");
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
                  placeholder="Digite o nome do titular"
                  value={this.state.nome}
                  onChangeText={(val) => this.setState({nome: val})}
              />
              <TextInput
                  style={styles.input}
                  placeholder="Digite o numero do cartão"
                  value={this.state.numero}
                  onChangeText={(val) => this.setState({numero: val})}
              />
              <TextInput
                  style={styles.input}
                  placeholder="Digite a validade do cartão"
                  value={this.state.validade}
                  onChangeText={(val) => this.setState({validade: val})}
              />
              <TextInput
                  style={styles.input}
                  placeholder="Digite o código de segurança do cartão"
                  value={this.state.codigoSeg}
                  onChangeText={(val) => this.setState({codigoSeg: val})}
              />
              <TouchableOpacity style={styles.button} onPress={this.cadastraCartao.bind(this)}>
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
