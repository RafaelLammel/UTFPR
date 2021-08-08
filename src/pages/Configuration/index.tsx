import React, { useContext } from "react";
import { Switch } from "react-native";
import ThemeUpdateContext from "../../contexts/themeUpdate";
import { Container, Section, SectionTitle } from "./styles";

export default function Configuration() {

    const { darkTheme, setTheme } = useContext(ThemeUpdateContext);

    return (
        <Container>
            <Section>
                <SectionTitle>Tema Escuro? </SectionTitle>
                <Switch
                    trackColor={{ false: "#767577", true: "#81b0ff" }}
                    thumbColor={darkTheme ? "#0000FF" : "#f4f3f4"}
                    onValueChange={() => setTheme(!darkTheme)}
                    value={darkTheme}
                />
            </Section>
        </Container>
    )
}