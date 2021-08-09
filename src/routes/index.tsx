import React, { useContext } from 'react';
import { View, ActivityIndicator } from 'react-native';
import { useTheme } from 'styled-components';
import AuthRoutes from './authRoutes';
import AppRoutes from './appRoutes';
import AuthContext from '../contexts/auth';

export default function Routes() {
    
    const { user, loading } = useContext(AuthContext);

    const theme = useTheme();

    if(loading)
        return(
            <View style={{flex: 1, justifyContent: 'center', alignItems: 'center', backgroundColor: theme.background}}>
                <ActivityIndicator size="large" color='#0000FF'/>
            </View>
        )

    return (
        user ? <AppRoutes/> : <AuthRoutes/>
    )
}