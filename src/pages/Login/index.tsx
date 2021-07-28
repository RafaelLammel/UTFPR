import React from 'react';
import { Text, TouchableOpacity, View } from 'react-native';
import { useNavigation } from '@react-navigation/native';

export default function Login() {

    const navigation = useNavigation();

    return (
        <View>
            <Text>Bem vindo ao Login!</Text>
            <TouchableOpacity onPress={() => navigation.navigate('Home')}>
                <Text>Vamos para Home!</Text>
            </TouchableOpacity>
        </View>
    )
}