import React, { useState } from 'react';
import { ActivityIndicator, Alert } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { useTheme } from 'styled-components';
import RegisterResult from '../../interfaces/RegisterResult';
import { register } from '../../services/auth';
import { AppTitle, Container, ErrorText, Field, GrayScreen, RegisterButton, RegisterButtonText } from './styles';

export default function Register() {

    // Dados do formulário
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    // Validação do formulário
    const [nameError, setNameError] = useState("");
    const [emailError, setEmailError] = useState("");
    const [passwordError, setPasswordError] = useState("");

    // Símbolo de carregando
    const [isLoading, setIsLoading] = useState(false);

    const navigation = useNavigation();

    const theme = useTheme();

    const handleRegister = async () => {
        if(validate()) {
            setIsLoading(true);
            const res: RegisterResult = await register(email, name, password);
            if(res.error) {
                setIsLoading(false);
                Alert.alert(
                    "Erro",
                    res.error
                );
                if(res.errorType == "email")
                    setEmailError(res.error);
                else if(res.errorType == "password")
                    setPasswordError(res.error);
            }
            else
                Alert.alert(
                    "Sucesso",
                    "Conta criada com sucesso!",
                    [
                        {
                            text: "OK",
                            onPress: () => navigation.navigate("Login")
                        }
                    ],
                    {cancelable: false}
                )
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
        setNameError("");
        setEmailError("");
        setPasswordError("");
        if(name === "") {
            noError = false;
            setNameError("Nome não pode ser vazio!");
        }
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
            <AppTitle>Registrar</AppTitle>
            <Field style={nameError === "" ? null : {borderBottomColor: 'red'}} onChangeText={text => setName(text)} placeholder="Nome" placeholderTextColor={theme.placeholderColor}/>
            {nameError === "" ? null : <ErrorText>{nameError}</ErrorText>}
            <Field style={emailError === "" ? null : {borderBottomColor: 'red'}} onChangeText={text => setEmail(text)} placeholder="Email" autoCapitalize="none" keyboardType="email-address" placeholderTextColor={theme.placeholderColor}/>
            {emailError === "" ? null : <ErrorText>{emailError}</ErrorText>}
            <Field style={passwordError === "" ? null : {borderBottomColor: 'red'}} onChangeText={text => setPassword(text)} placeholder="Senha" autoCapitalize="none" secureTextEntry={true} placeholderTextColor={theme.placeholderColor}/>
            {passwordError === "" ? null : <ErrorText>{passwordError}</ErrorText>}
            <RegisterButton onPress={handleRegister}>
                <RegisterButtonText>Registrar</RegisterButtonText>
            </RegisterButton>
            <LoadingIndicator/>
        </Container>
    )
}