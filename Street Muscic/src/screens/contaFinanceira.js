import React, { Component } from 'react';
import { View, Text, StyleSheet, TouchableOpacity} from 'react-native';

export default class ContaFinanceira extends Component{
    constructor(){
        super();
    }
    static navigationOptions = {
        title: 'Contas Financeiras'
    }
    render(){
        const { navigate } = this.props.navigation;
        return(
            <View style={styles.container}>
              <TouchableOpacity style={styles.button} onPress={() => navigate('CartaoCredito')}>
                  <Text style={styles.buttonText}>Cartão de Crédito</Text>
              </TouchableOpacity>
              <TouchableOpacity style={styles.button} onPress={() => navigate('ContaBancaria')}>
                <Text style={styles.buttonText}>Conta Bancária</Text>
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
