import firebase from "./firebase"
import RegisterResult from "../interfaces/RegisterResult";
import LoginResult from "../interfaces/LoginResult";

export async function register(email: string, name: string, password: string): Promise<RegisterResult> {
    let registerResult: RegisterResult;
    try {
        const userAuth = await firebase.auth().createUserWithEmailAndPassword(email, password);
        firebase.firestore().collection("users").doc(userAuth.user?.uid).set({
            name: name
        });
        registerResult = {
            userToken: userAuth.user?.uid
        };
    }
    catch (e) {
        let error: string;
        if(e.code === "auth/email-already-in-use")
            error = "E-mail já está em uso!"
        else if (e.code === "auth/invalid-email")
            error = "E-mail inválido!"
        else
            error = "Ocorreu algum erro no servidor!"
        registerResult = { error }
    }
    return registerResult;
}

export async function login(email: string, password: string): Promise<LoginResult> {
    let loginResult: LoginResult = {};
    try {
        const userAuth = await firebase.auth().signInWithEmailAndPassword(email, password);
        console.log(userAuth);
    } catch(e) {
        let error: string;
        if(e.code === "auth/wrong-password") 
            error = "Senha inválida!";
        else if(e.code === "auth/user-not-found")
            error = "E-mail não encontrado!"
        else if(e.code === "auth/invalid-email")
            error = "E-mail inválido!"
        else
            error = "Ocorreu algum erro no servidor!";
        loginResult = { error };
    }
    return loginResult;
}