import 'react-native-gesture-handler';
import React from 'react';
import { StatusBar, LogBox } from 'react-native';
import Routes from './src/routes';
import { AuthProvider } from './src/contexts/auth';
import { ThemeUpdateProvider } from './src/contexts/themeUpdate';
import { RootSiblingParent } from 'react-native-root-siblings';

LogBox.ignoreLogs(['Setting a timer']);

export default function App() {
  return (
    <RootSiblingParent>
      <AuthProvider>
        <StatusBar barStyle="light-content"/>
        <ThemeUpdateProvider>
          <Routes/>
        </ThemeUpdateProvider>
      </AuthProvider>
    </RootSiblingParent>
  );
}