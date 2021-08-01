import React, { useState } from 'react';
import { ActivityIndicator, Alert } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { useTheme } from 'styled-components';
import { AppTitle, Container, ErrorText, Field, LoginButton, LoginButtonText, RegisterLink, RegisterLinkText } from './styles';
import { GrayScreen } from '../Register/styles';
import LoginResult from '../../interfaces/LoginResult';
import { login } from '../../services/auth';

export default function Login() {

    // Dados do formulário
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    // Validação do formulário
    const [emailError, setEmailError] = useState("");
    const [passwordError, setPasswordError] = useState("");

    // Símbolo de carregando
    const [isLoading, setIsLoading] = useState(false);

    const theme = useTheme();

    const navigation = useNavigation();

    const handleLogin = async () => {
        if(validate()) {
            setIsLoading(true);
            const res: LoginResult = await login(email, password);
            setIsLoading(false);
            if(res.error) {
                Alert.alert(
                    "Erro",
                    res.error
                );
            }
        }
        else {
            Alert.alert(
                "Erro",
                "Existem erros no formulário!"
            );
        }
    }

    const validate = (): boolean => {
        let noError: boolean = true;
        setEmailError("");
        setPasswordError("");
        if(email === "") {
            noError = false;
            setEmailError("Email não pode ser vazio!");
        }
        if(password === "") {
            noError = false;
            setPasswordError("Senha não pode ser vazia!");
        }
        return noError;
    }

    const LoadingIndicator = () => {
        if(isLoading)
            return (
                <GrayScreen>
                    <ActivityIndicator size="large" color="#fff"/>
                </GrayScreen>
            )
        return null;
    }

    return (
        <Container>
            <AppTitle>Notas</AppTitle>
            <Field style={emailError === "" ? null : {borderBottomColor: 'red'}} onChangeText={text => setEmail(text)} placeholder="Email" keyboardType="email-address" autoCapitalize="none" placeholderTextColor={theme.placeholderColor}/>
            {emailError === "" ? null : <ErrorText>{emailError}</ErrorText>}
            <Field style={passwordError === "" ? null : {borderBottomColor: 'red'}} onChangeText={text => setPassword(text)} placeholder="Senha" secureTextEntry={true} autoCapitalize="none" placeholderTextColor={theme.placeholderColor}/>
            {passwordError === "" ? null : <ErrorText>{passwordError}</ErrorText>}
            <LoginButton onPress={handleLogin}>
                <LoginButtonText>Login</LoginButtonText>
            </LoginButton>
            <RegisterLink onPress={() => navigation.navigate("Register")}>
                <RegisterLinkText>Não tem uma conta? Cadastre-se aqui!</RegisterLinkText>
            </RegisterLink>
            <LoadingIndicator/>
        </Container>
    )
}