import React, { useState, createContext, useEffect } from 'react';
import firebase from '../services/firebase';

interface AuthData {
    signed: boolean,
    user: firebase.User | null,
    loading: boolean,
    handleAuth(u: firebase.User | null): void
};

const AuthContext = createContext<AuthData>({} as AuthData);

export const AuthProvider: React.FC = ({children}) => {

    const [user, setUser] = useState<firebase.User | null>(null);
    const [loading, setLoading] = useState(true);

    function handleAuth(u: firebase.User | null) {
        if(u) {
            if(u.displayName)
                setUser(u);
        }
        else
            setUser(null);
        if(loading)
            setLoading(false);
    }

    useEffect(() => {
        const subscriber = firebase.auth().onAuthStateChanged(handleAuth);
        return subscriber;
    }, []);

    return (
        <AuthContext.Provider value={{signed: !!user, user, loading, handleAuth}}>
            {children}
        </AuthContext.Provider>
    )
};

export default AuthContext;