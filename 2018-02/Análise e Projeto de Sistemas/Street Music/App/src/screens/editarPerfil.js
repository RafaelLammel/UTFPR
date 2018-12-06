import React, { Component } from 'react';
import api from '../services/api';
import { StyleSheet, View, Text, TextInput, AsyncStorage, TouchableOpacity } from 'react-native';

export default class EditarPerfil extends Component{
    constructor(){
        super();
        this.state = {
            id:'',
            nome:'',
            email:'',
            senha:'',
        }
    }
    componentDidMount(){
        this.pegaDados();
    }
    async pegaDados() {
      var res;
      try {
        const value = await AsyncStorage.getItem('EMAIL');
        if (value !== null) {
          //console.log(value);
          res = value;
        }
       } catch (error) {
         console.log(error);
       }
       const response = await api.get('/usuarios/' + res)
       .then(function (response){
           //console.log(response);
           res = response;
       })
       .catch(function (error){
           console.log(error);
       });
       this.setState({id:res.data[0].id});
       this.setState({nome:res.data[0].nome});
       this.setState({email:res.data[0].email});
       this.setState({senha:res.data[0].senha});
    }
    async guardaDados(){
      var index;
      const resp = await api.get('/usuarios')
      .then(function (resp){
          console.log(resp);
          index = resp;
      })
      .catch(function (error){
          console.log(error);
      });
      for (var i = 0; i < index.data.length; i++){
        if(i+1 != this.state.id){
          if(this.state.email == index.data[i].email){
            alert("Esse email já está sendo usado!");
            return;
          }
        }
      }
      const response = await api.put('/usuarios/' + this.state.id,{
          nome: this.state.nome,
          email: this.state.email,
          senha: this.state.senha,
      })
      .then(function (response){
          console.log(response);
          alert("Alteração feita com sucesso!");
      })
      .catch(function (error){
          console.log(error);
      });
    }
    static navigationOptions = {
        title: 'Editar Perfil'
    }
    render(){
        const {navigate} = this.props.navigation;
        return(
            <View style={styles.container}>
              <Text style={styles.title}>Editar Perfil</Text>
              <TextInput
                  style={styles.input}
                  placeholder={this.state.nome}
                  value={this.state.nome}
                  onChangeText={(val) => this.setState({nome: val})}
              />
              <TextInput
                  style={styles.input}
                  placeholder={this.state.email}
                  value={this.state.email}
                  onChangeText={(val) => this.setState({email: val})}
              />
              <TextInput
                  style={styles.input}
                  placeholder={this.state.senha}
                  value={this.state.senha}
                  onChangeText={(val) => this.setState({senha: val})}
                  secureTextEntry={true}
              />
              <TouchableOpacity style={styles.button} onPress={this.guardaDados.bind(this)}>
                  <Text style={styles.buttonText}>Gravar</Text>
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
