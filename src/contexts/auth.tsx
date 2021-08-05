import React, { useState, createContext, useEffect } from 'react';
import firebase from '../services/firebase';

interface AuthData {
    signed: boolean,
    user: object | null
};

const AuthContext = createContext<AuthData>({} as AuthData);

export const AuthProvider: React.FC = ({children}) => {

    const [user, setUser] = useState<object | null>(null);

    function handleAuth(u: firebase.User | null) {
        if(u)
            setUser(u);
        else
            setUser(null);
    }

    useEffect(() => {
        const subscriber = firebase.auth().onAuthStateChanged(handleAuth);
        return subscriber;
    }, []);

    return (
        <AuthContext.Provider value={{signed: !!user, user}}>
            {children}
        </AuthContext.Provider>
    )
};

export default AuthContext;