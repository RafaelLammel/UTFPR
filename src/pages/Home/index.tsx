import React from 'react';
import { Text, TouchableOpacity, SafeAreaView } from 'react-native';
import { logout } from '../../services/auth';

export default function Home() {

    return (
        <SafeAreaView>
            <Text>Bem vindo ao Home!</Text>
            <TouchableOpacity onPress={logout}>
                <Text>Fazer Logout</Text>
            </TouchableOpacity>
        </SafeAreaView>
    )
}