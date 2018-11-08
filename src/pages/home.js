//Aqui importo o React;
import React, { Component } from 'react';
//Aqui importo os componentes do React Native, que criam os componentes nativos;
//StyleSheet me permite criar uma constante pra escrever um código muito parecido com
//CSS. TouchableOpacity é o botão;
import { StyleSheet, View, Text, TouchableOpacity } from 'react-native';

//Aqui cria-se a classe. Toda classe no React Native deve ser exportada;
export default class Home extends Component{
    //Definindo propriedades da navegação (Header);
    static navigationOptions = {
        title: 'Home'
    }
    render(){
        //Não entendi isso direito, mas imagino que seja para ativar a navegação
        //nessa tela;
        const { navigate } = this.props.navigation;
        return(
            <View style={styles.container}>
                <Text style={styles.maintitle}>
                    Street Music
                </Text>
                <TouchableOpacity style={styles.button} onPress={()=>navigate('Login')}>
                    <Text style={styles.buttontext}>Login</Text>
                </TouchableOpacity>
                <TouchableOpacity style={styles.button} onPress={()=>navigate('Cadastro')}>
                    <Text style={styles.buttontext}>Cadastro</Text>
                </TouchableOpacity>
            </View>
        );
    }
}
//Aqui é onde escrevo meu código "CSS", e aplico ele nas tags acima;
const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
    maintitle: {
        color: 'blue',
        fontSize: 45,
        margin: 10,
    },
    buttontext:{
        fontSize: 20
    },
    button: {
        alignItems: 'center',
        backgroundColor: '#DDDDDD',
        padding: 10,
        margin: 10
    },
});
