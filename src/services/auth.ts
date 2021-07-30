import firebase from "./firebase"
import RegisterResult from "../interfaces/RegisterResult";

export async function register(email: string, name: string, password: string): Promise<RegisterResult> {
    let register: RegisterResult;
    try {
        const userAuth = await firebase.auth().createUserWithEmailAndPassword(email, password);
        firebase.firestore().collection("users").doc(userAuth.user?.uid).set({
            name: name
        });
        register = {
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
        register = { error }
    }
    return register;
}