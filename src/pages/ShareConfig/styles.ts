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
    border-bottom-width: 5px;
    border-color: ${props => props.theme.placeholderColor};
`;

export const HeaderTitle = styled.Text`
    color: ${props => props.theme.color};
    font-size: 32px;
    font-weight: bold;
`;

export const FloatingButton = styled.TouchableOpacity`
    position: absolute;
    width: 70px;
    height: 70px;
    align-items: center;
    justify-content: center;
    right: 20px;
    bottom: 30px;
    border-radius: 35px;
    background-color: blue;
`;

export const FloatingButtonIcon = styled.Text`
    color: white;
    font-weight: bold;
    font-size: 30px;
`;

export const CenterView = styled.View`
    justify-content: center;
    align-items: center;
`;

export const NothingText = styled.Text`
    color: ${props => props.theme.placeholderColor};
    font-size: 24px;
`;

export const ListItem = styled.View`
    padding-top: 10px;
    padding-bottom: 10px;
    border-bottom-width: 1px;
    border-color: ${props => props.theme.color};
    display: flex;
    align-items: center;
    flex-direction: row;
`;

export const ListItemText = styled.Text`
    color: ${props => props.theme.color};
    font-size: 24px;
    margin-left: 5px;
`;

export const ModalContainer = styled.View `
    background: rgba(46, 49, 49, 0.7);
    flex: 1;
    justify-content: center;
    align-items: center;
    position: absolute;
    width: 100%;
    height: 100%;
`;

export const ModalBox = styled.View `
    background-color: #e8e8e8;
    width: 90%;
    height: 180px;
    border-radius: 20px;
    padding: 10px;
`;

export const ModalText = styled.Text `
    color: black;
    font-size: 20px;
`;

export const ModalInput = styled.TextInput `
    background-color: white;
    border-radius: 10px;
    border-width: 1px;
    margin-top: 20px;
    margin-bottom: 10px;
    font-size: 20px;
    padding: 10px;
`;

export const ModalFooter = styled.View `
    display: flex;
    flex-direction: row;
    justify-content: flex-end;
`;