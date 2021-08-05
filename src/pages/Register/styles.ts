import styled from "styled-components/native";

export const Container = styled.View`
    flex: 1;
    align-items: center;
    justify-content: center;
    background: ${props => props.theme.background};
`;

export const AppTitle = styled.Text`
    font-size: 40px;
    color: ${props => props.theme.color};
`;

export const Field = styled.TextInput`
    border-bottom-width: 1px;
    border-bottom-color: ${props => props.theme.color};
    color: ${props => props.theme.color};
    width: 60%;
    font-size: 20px;
    margin: 10px 0;
`;

export const ErrorText = styled.Text`
    color: red;
`;

export const RegisterButton = styled.TouchableOpacity`
    background-color: ${props => props.theme.color};
    width: 50%;
    margin: 10px 0;
    height: 50px;
    border-radius: 50px;
    display: flex;
    justify-content: center;
    align-items: center;
`;

export const RegisterButtonText = styled.Text`
    font-size: 20px;
    color: ${props => props.theme.background};
`;

export const GrayScreen = styled.View`
    background: rgba(46, 49, 49, 0.7);
    flex: 1;
    justify-content: center;
    align-items: center;
    position: absolute;
    width: 100%;
    height: 100%;
`;