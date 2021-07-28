import 'react-native-gesture-handler';
import React from 'react';
import { ThemeProvider } from 'styled-components';
import Routes from './src/routes';
import themes from './src/themes';

export default function App() {

  const theme = themes.dark;

  return (
    <ThemeProvider theme={theme}>
      <Routes/>
    </ThemeProvider>
  );
}