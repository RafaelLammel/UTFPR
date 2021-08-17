import styled from "styled-components/native";

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