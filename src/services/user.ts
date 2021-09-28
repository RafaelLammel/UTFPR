import firebase from "firebase";
import User from "../interfaces/User";

export async function addTag(tag: string) {
    try {
        const user = await firebase.firestore().collection('usuarios').doc(firebase.auth().currentUser?.uid);
        user.update({
            tags: firebase.firestore.FieldValue.arrayUnion(tag)
        });
        return { ok: 'ok' };
    }
    catch(e) {
        return { error: "Falha no servidor!" };
    }
}

export async function getBannedUsers() {
    try {
        const user = await firebase.firestore().collection('usuarios').doc(firebase.auth().currentUser?.uid).get();
        const userData: User = user.data() as User;
        if(userData.usuariosBanidos.length === 0)
            return { data: undefined }
        const bannedUsers = await firebase.firestore().collection('usuarios').where(firebase.firestore.FieldPath.documentId(), 'in', userData.usuariosBanidos).get();
        const bannedUsersData: Array<any> = [];
        bannedUsers.forEach(e => {
            const u = e.data() as User;
            bannedUsersData.push({key: u.email});
        })
        return { data: bannedUsersData }
    }
    catch(e) {
        return { error: "Falha no servidor!" };
    }
}

export async function addBannedUser(email: string) {
    try {
        const user = await firebase.firestore().collection('usuarios').where('email', '==', email).get();
        if(user.size > 0) {
            const currentUser = await firebase.firestore().collection('usuarios').doc(firebase.auth().currentUser?.uid);
            currentUser.update({
                usuariosBanidos: firebase.firestore.FieldValue.arrayUnion(user.docs[0].id)
            });
            return { ok: 'ok' };
        }
        return { error: 'Usuário inexistente na base!' };
    }
    catch(e) {
        return { error: 'Falha no servidor!' };
    }
}

export async function removeBannedUser(email: string) {
    try {
        const user = await firebase.firestore().collection('usuarios').where('email', '==', email).get();
        if(user.size > 0) {
            const currentUser = await firebase.firestore().collection('usuarios').doc(firebase.auth().currentUser?.uid);
            currentUser.update({
                usuariosBanidos: firebase.firestore.FieldValue.arrayRemove(user.docs[0].id)
            });
            return { ok: 'ok' };
        }
        return { error: 'Usuário inexistente na base!' };
    }
    catch(e) {
        return { error: 'Falha no servidor!' };
    }
}