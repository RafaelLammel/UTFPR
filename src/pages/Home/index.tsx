import React from 'react';
import { Text, TouchableOpacity, SafeAreaView } from 'react-native';
import { useNavigation } from '@react-navigation/native';

export default function Home() {

    const navigation = useNavigation();

    return (
        <SafeAreaView>
            <Text>Bem vindo ao Home!</Text>
            <TouchableOpacity onPress={() => navigation.navigate('Login')}>
                <Text>Vamos para Login!</Text>
            </TouchableOpacity>
        </SafeAreaView>
    )
}