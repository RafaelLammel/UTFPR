import React, { useState } from 'react';
import { Text, TouchableOpacity, SafeAreaView, Modal, Alert } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { Ionicons } from '@expo/vector-icons';
import Toast from 'react-native-root-toast';
import { logout } from '../../services/auth';
import { addTag } from '../../services/user';
import firebase from 'firebase';
import { ModalContainer, ModalBox, ModalText, ModalInput, ModalFooter } from './styles';

export default function Home() {

    const [modal, setModal] = useState(false);
    const [tag, setTag] = useState('');

    const navigation = useNavigation();

    const handleSaveTag = async () => {
        if(validate()) {
            const res = await addTag(tag);
            if (res.error) {
                Alert.alert(
                    "Erro",
                    res.error
                );
            }
            else if (res.ok) {
                setModal(false);
                setTag('');
                Toast.show('Tag criada com sucesso!', {
                    duration: Toast.durations.SHORT,
                });
            }
        }
        else {
            Alert.alert(
                'Erro',
                'Tag não pode ser vazia!'
            )
        }
    }

    const validate = (): boolean => {
        return tag !== '' && tag !== undefined && tag !== null;
    }

    return (
        <SafeAreaView>
            <Modal
                animationType="slide"
                transparent={true}
                visible={modal}
            >
                <ModalContainer>
                    <ModalBox>
                        <ModalText>Digite o título da Tag:</ModalText>
                        <ModalInput value={tag} onChangeText={text => setTag(text)}/>
                        <ModalFooter>
                            <TouchableOpacity onPress={() => setModal(false)}>
                                <Ionicons name="close" size={50} color="red" />
                            </TouchableOpacity>
                            <TouchableOpacity style={{marginLeft: 10}} onPress={handleSaveTag}>
                                <Ionicons name="save" size={48} color="green" />
                            </TouchableOpacity>
                        </ModalFooter>
                    </ModalBox>
                </ModalContainer>
            </Modal>
            <Text>Bem vindo ao Home! {firebase.auth().currentUser?.displayName}</Text>
            <TouchableOpacity onPress={() => navigation.navigate("Configuration")}>
                <Text>Configurações</Text>
            </TouchableOpacity>
            <TouchableOpacity onPress={() => setModal(true)}>
                <Text>Criar Tag</Text>
            </TouchableOpacity>
            <TouchableOpacity onPress={logout}>
                <Text>Fazer Logout</Text>
            </TouchableOpacity>
        </SafeAreaView>
    )
}