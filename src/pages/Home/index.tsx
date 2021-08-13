import React from 'react';
import { Text, TouchableOpacity, SafeAreaView } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { logout } from '../../services/auth';
import firebase from 'firebase';

export default function Home() {

    const navigation = useNavigation();

    return (
        <SafeAreaView>
            <Text>Bem vindo ao Home! {firebase.auth().currentUser?.displayName}</Text>
            <TouchableOpacity onPress={() => navigation.navigate("Configuration")}>
                <Text>Configurações</Text>
            </TouchableOpacity>
            <TouchableOpacity onPress={logout}>
                <Text>Fazer Logout</Text>
            </TouchableOpacity>
        </SafeAreaView>
    )
}