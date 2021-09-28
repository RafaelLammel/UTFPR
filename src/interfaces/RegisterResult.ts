import firebase from "firebase";

export default interface Register {
    error?: string,
    errorType?: string,
    user: firebase.User | null,
}