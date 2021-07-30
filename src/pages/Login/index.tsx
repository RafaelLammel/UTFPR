import React from 'react';
import { useNavigation } from '@react-navigation/native';
import { useTheme } from 'styled-components';
import { AppTitle, Container, Field, LoginButton, LoginButtonText, RegisterLink, RegisterLinkText } from './styles';

export default function Login() {

    const theme = useTheme();

    const navigation = useNavigation();

    return (
        <Container>
            <AppTitle>Notas</AppTitle>
            <Field placeholder="Login" placeholderTextColor={theme.placeholderColor}/>
            <Field placeholder="Senha" placeholderTextColor={theme.placeholderColor} secureTextEntry={true}/>
            <LoginButton>
                <LoginButtonText>Login</LoginButtonText>
            </LoginButton>
            <RegisterLink onPress={() => navigation.navigate("Register")}>
                <RegisterLinkText>Não tem uma conta? Cadastre-se aqui!</RegisterLinkText>
            </RegisterLink>
        </Container>
    )
}