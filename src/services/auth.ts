import firebase from "./firebase"
import RegisterResult from "../interfaces/RegisterResult";
import LoginResult from "../interfaces/LoginResult";

export async function register(email: string, name: string = "", password: string): Promise<RegisterResult> {
    let registerResult: RegisterResult = {user: null};
    try {
        const userAuth = await firebase.auth().createUserWithEmailAndPassword(email, password);
        await userAuth.user?.updateProfile({
            displayName: name
        });
        await firebase.firestore().collection("users").doc(userAuth.user?.uid).set({
            notasUsuario: [],
            notasCompartilhadas: [],
            tags: []
        });
        registerResult = { user: userAuth.user }
    }
    catch (e) {
        let error: string;
        let errorType: string = "";
        if(e.code === "auth/email-already-in-use") {
            error = "E-mail já está em uso!"
            errorType = "email";
        }
        else if (e.code === "auth/invalid-email") {
            error = "E-mail inválido!"
            errorType = "email";
        }
        else if (e.code === "auth/weak-password") {
            error = "Senha fraca!";
            errorType = "password";
        }
        else
            error = "Ocorreu algum erro no servidor!"
        registerResult = { error, errorType, user: null }
    }
    return registerResult;
}

export async function login(email: string, password: string): Promise<LoginResult> {
    let loginResult: LoginResult = {};
    try {
        await firebase.auth().signInWithEmailAndPassword(email, password);
    } catch(e) {
        let error: string;
        let errorType: string = "";
        if(e.code === "auth/wrong-password") {
            error = "Senha inválida!";
            errorType = "password";
        }
        else if(e.code === "auth/user-not-found") {
            error = "E-mail não encontrado!"
            errorType = "email";
        }
        else if(e.code === "auth/invalid-email") {
            error = "E-mail inválido!"
            errorType = "email";
        }
        else
            error = "Ocorreu algum erro no servidor!";
        loginResult = { error, errorType };
    }
    return loginResult;
}

export async function logout() {
    try {
        await firebase.auth().signOut()
    }
    catch(e) {
        // TODO: Tratar erros de Logout
    }
}