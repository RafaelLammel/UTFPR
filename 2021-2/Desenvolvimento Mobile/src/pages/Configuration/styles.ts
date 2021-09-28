import styled from "styled-components/native";

export const Container = styled.View`
    flex: 1;
    background: ${props => props.theme.background};
`;

export const Section = styled.View`
    display: flex;
    flex-direction: row;
    align-items: center;
    padding: 25px 10px;
    border-bottom-width: 1px;
    border-color: ${props => props.theme.placeholderColor};
`;

export const HeaderTitle = styled.Text`
    color: ${props => props.theme.color};
    font-size: 32px;
    font-weight: bold;
`;

export const SectionTitle = styled.Text`
    color: ${props => props.theme.color};
    font-size: 20px;
`;