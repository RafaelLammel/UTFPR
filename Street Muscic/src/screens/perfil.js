import React, { Component } from 'react';
import api from '../services/api';

import { StyleSheet, View, Text, AsyncStorage, TouchableOpacity } from 'react-native';

export default class Perfil extends Component{

    constructor(){
        super();
        this.state = {
            nome:'',
            id:'',
            email:''
        }
    }

    componentDidMount(){
        this.pegaNome();
    }

    async pegaNome() {
      try {
        var value = await AsyncStorage.getItem('NAME');
        if (value !== null) {
          //console.log(value);
          this.setState({nome: value});
        }
        value = await AsyncStorage.getItem('ID');
        if (value !== null) {
          //console.log(value);
          this.setState({id: value});
        }
        value = await AsyncStorage.getItem('EMAIL');
        if (value !== null) {
          //console.log(value);
          this.setState({email: value});
        }
       } catch (error) {
         console.log(error);
       }
    }

    async viraMusico(){
      var temp;
      const res = await api.get('/usuarios/' + this.state.email)
      .then(function (res){
          //console.log(response);
          temp = res;
      })
      .catch(function (error){
          console.log(error);
      });
      if (temp.data[0].musico == 1){
        alert("Você já é músico!!");
        return;
      }
      const response = await api.put('/usuarios/' + this.state.id,{
          musico: 1,
      })
      .then(function (response){
          console.log(response);
          alert("Agora você é musico!");
      })
      .catch(function (error){
          console.log(error);
      });
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
              <TouchableOpacity style={styles.button} onPress={() => navigate('ContaFinanceira')}>
                  <Text style={styles.buttonText}>Cadastrar Conta Financeira</Text>
              </TouchableOpacity>
              <TouchableOpacity style={styles.button} onPress={this.viraMusico.bind(this)}>
                <Text style={styles.buttonText}>Virar Musico</Text>
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
