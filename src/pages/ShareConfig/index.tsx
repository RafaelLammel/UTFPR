import React from "react";
import { useEffect, useState } from "react";
import { ActivityIndicator, FlatList, TouchableOpacity, Modal, Alert } from "react-native";
import { Ionicons } from '@expo/vector-icons';
import { getBannedUsers, addBannedUser, removeBannedUser } from "../../services/user";
import { Container, Section, HeaderTitle, FloatingButton, FloatingButtonIcon, CenterView, NothingText, ListItem, ListItemText, ModalContainer, ModalText, ModalBox, ModalInput, ModalFooter } from "./styles";
import { useContext } from "react";
import AuthContext from "../../contexts/auth";

const ShareConfig: React.FC = () => {

    const [loading, setLoading] = useState(true);
    const [bannedUsers, setBannedUsers] = useState<Array<any>>();
    const [modal, setModal] = useState(false);
    const [email, setEmail] = useState('');
    const [error, setError] = useState('');

    const { user } = useContext(AuthContext);

    const getUsers = async () => {
        const res = await getBannedUsers();
        if(res.error) {
            setError(res.error);
        }
        else {
            setBannedUsers(res.data);
        }
        setLoading(false);
    }
    
    useEffect(() => {
        getUsers();
    },[]);

    const handleAddBan = async () => {
        if(validate()) {
            const res = await addBannedUser(email);
            if (res.error) {
                Alert.alert(
                    "Erro",
                    res.error
                );
            }
            else if (res.ok) {
                setLoading(true);
                setModal(false);
                setEmail('');
                await getUsers();
            }
        }
    }

    const handleRemoveBan = async (e: string) => {
        const res = await removeBannedUser(e);
        if(res.error) {
            Alert.alert(
                "Erro",
                res.error
            );
        }
        else if (res.ok) {
            setLoading(true);
            setModal(false);
            setEmail('');
            await getUsers();
        }
    }

    const validate = (): boolean => {
        if(email === '' || email === undefined || email === null) {
            Alert.alert(
                "Erro",
                "Email inválido!"
            );
            return false;
        }
        if(user) {
            if(email === user.email) {
                Alert.alert(
                    "Erro",
                    "Você não pode banir a si mesmo!"
                );
                return false;
            }
        }
        else {
            Alert.alert(
                "Erro",
                "Ocorreu algum erro com sua conta!"
            );
            return false;
        }
        return true
    }

    const UserListItem: React.FC = ({ children }) => (
        <ListItem>
            <TouchableOpacity onPress={() => handleRemoveBan(children as string)}>
                <Ionicons name="trash" size={32} color="red" style={{marginLeft: 5}} />
            </TouchableOpacity>
            <ListItemText>{children}</ListItemText>
        </ListItem>
    )

    const UserList = () => {
        if(loading)
            return (
                <CenterView style={{flex: 1}}>
                    <ActivityIndicator size="large" color='#0000FF'/>
                </CenterView>
            )
        if(error)
            return (
                <CenterView style={{flex: 1}}>
                    <NothingText>error</NothingText>
                </CenterView>
            )
        if(!bannedUsers)
            return (
                <CenterView style={{flex: 1}}>
                    <NothingText>Nenhum usuário banido! =D</NothingText>
                </CenterView>
            )
        return (
            <FlatList data={bannedUsers} renderItem={({item}) => <UserListItem>{item.key}</UserListItem>}/>
        )
    }

    return(
        <Container>
            <Modal
                animationType="slide"
                transparent={true}
                visible={modal}
            >
                <ModalContainer>
                    <ModalBox>
                        <ModalText>Digite o e-mail que deseja banir:</ModalText>
                        <ModalInput value={email} onChangeText={text => setEmail(text)} keyboardType="email-address" autoCapitalize="none"/>
                        <ModalFooter>
                            <TouchableOpacity onPress={() => setModal(false)}>
                                <Ionicons name="close" size={50} color="red" />
                            </TouchableOpacity>
                            <TouchableOpacity style={{marginLeft: 10}} onPress={handleAddBan}>
                                <Ionicons name="save" size={48} color="green" />
                            </TouchableOpacity>
                        </ModalFooter>
                    </ModalBox>
                </ModalContainer>
            </Modal>
            <Section>
                <HeaderTitle>Usuários Banidos</HeaderTitle>
            </Section>
            <UserList />
            <FloatingButton onPress={() => setModal(true)}>
                <FloatingButtonIcon>+</FloatingButtonIcon>
            </FloatingButton>
        </Container>
    );
}

export default ShareConfig;