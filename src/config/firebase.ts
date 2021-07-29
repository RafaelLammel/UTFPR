import firebase from "firebase";

var firebaseConfig = {
    apiKey: "AIzaSyBDeie4JnJvBDz_v9_T1K91gWagHYkontU",
    authDomain: "app-notas-6cbf3.firebaseapp.com",
    projectId: "app-notas-6cbf3",
    storageBucket: "app-notas-6cbf3.appspot.com",
    messagingSenderId: "419192102510",
    appId: "1:419192102510:web:6ae600035428d14345907f"
};

// Initialize Firebase
firebase.initializeApp(firebaseConfig);

export default firebase;