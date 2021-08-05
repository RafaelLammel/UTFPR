import 'react-native-gesture-handler';
import React from 'react';
import { StatusBar, LogBox } from 'react-native';
import { ThemeProvider } from 'styled-components';
import Routes from './src/routes';
import themes from './src/themes';
import { AuthProvider } from './src/contexts/auth';


LogBox.ignoreLogs(['Setting a timer']);

export default function App() {

  const theme = themes.dark;

  return (
    <AuthProvider>
      <StatusBar barStyle="light-content"/>
      <ThemeProvider theme={theme}>
        <Routes/>
      </ThemeProvider>
    </AuthProvider>
  );
}