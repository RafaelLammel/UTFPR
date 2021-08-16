import React, { useContext } from "react";
import { Switch, TouchableOpacity } from "react-native";
import { useNavigation } from "@react-navigation/native";
import ThemeUpdateContext from "../../contexts/themeUpdate";
import { Container, HeaderTitle, Section, SectionTitle } from "./styles";

export default function Configuration() {

    const { darkTheme, setTheme } = useContext(ThemeUpdateContext);

    const navigation = useNavigation();

    return (
        <Container>
            <Section>
                <HeaderTitle>Configurações</HeaderTitle>
            </Section>
            <Section>
                <SectionTitle>Tema Escuro? </SectionTitle>
                <Switch
                    trackColor={{ false: "#767577", true: "#81b0ff" }}
                    thumbColor={darkTheme ? "#0000FF" : "#f4f3f4"}
                    onValueChange={() => setTheme(!darkTheme)}
                    value={darkTheme}
                />
            </Section>
            <Section>
                <TouchableOpacity onPress={() => navigation.navigate('ShareConfig')}>
                    <SectionTitle>Gerenciar Compartilhamento</SectionTitle>
                </TouchableOpacity>
            </Section>
        </Container>
    )
}