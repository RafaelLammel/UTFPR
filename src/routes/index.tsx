import React, { useState, useContext } from 'react';
import AuthRoutes from './authRoutes';
import AppRoutes from './appRoutes';
import AuthContext from '../contexts/auth';

export default function Routes() {
    
    const { user } = useContext(AuthContext);

    return (
        <>
            {user ? <AppRoutes/> : <AuthRoutes/>}
        </> 
    )
}