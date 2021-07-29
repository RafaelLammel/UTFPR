import 'react-native-gesture-handler';
import React from 'react';
import { ThemeProvider } from 'styled-components';
import Routes from './src/routes';
import themes from './src/themes';
import { StatusBar } from 'react-native';

export default function App() {

  const theme = themes.dark;

  return (
    <>
      <StatusBar barStyle="light-content"/>
      <ThemeProvider theme={theme}>
        <Routes/>
      </ThemeProvider>
    </>
  );
}