import React from 'react';
import { StatusBar } from 'react-native';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import Login from '../pages/Login';
import Home from '../pages/Home';

const AppStack = createStackNavigator();

export default function Routes() {
    return (
        <>
            <NavigationContainer>
                <AppStack.Navigator screenOptions={{headerShown:false}}>
                    <AppStack.Screen name="Login" component={Login}/>
                    <AppStack.Screen name="Home" component={Home}/>
                </AppStack.Navigator>
            </NavigationContainer>
        </>
    )
}