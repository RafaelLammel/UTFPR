import React, { createContext, useState, useEffect } from 'react';
import { ThemeProvider } from 'styled-components';
import AsyncStorage from '@react-native-async-storage/async-storage';
import themes from '../themes';

interface ThemeUpdateData {
    darkTheme: boolean,
    setTheme(toogle: boolean): Promise<void>
}

const ThemeUpdateContext = createContext({} as ThemeUpdateData);

export const ThemeUpdateProvider: React.FC = ({ children }) => {

    const [darkTheme, setDarkTheme] = useState(true);
    const [theme, setThemeUpdate] = useState(themes.dark);

    const setTheme = async (toggle: boolean): Promise<void> => {
        try {
            setDarkTheme(toggle);
            await AsyncStorage.setItem('@darkTheme', String(toggle));
            if(toggle)
                setThemeUpdate(themes.dark)
            else
                setThemeUpdate(themes.light);
        }
        catch(e) {

        }
    }

    useEffect(() => {
        const startTheme = async () => {
            try {
                const storedTheme = await AsyncStorage.getItem('@darkTheme');
                if(storedTheme != null) {
                    await setTheme((storedTheme == "true"));
                }
            } catch(e) {
    
            }
        }
        startTheme();
    }, []);

    return(
        <ThemeProvider theme={theme}>
            <ThemeUpdateContext.Provider value={{darkTheme, setTheme}}>
                {children}
            </ThemeUpdateContext.Provider>        
        </ThemeProvider>
    )
}

export default ThemeUpdateContext;