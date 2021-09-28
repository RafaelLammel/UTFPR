import React from "react";
import { NavigationContainer } from "@react-navigation/native";
import { createStackNavigator } from "@react-navigation/stack";
import Login from "../pages/Login";
import Register from "../pages/Register";

const AuthStack = createStackNavigator();

export default function AuthRoutes() {
    return (
        <NavigationContainer>
            <AuthStack.Navigator screenOptions={{headerShown:false}}>
                <AuthStack.Screen name="Login" component={Login}/>
                <AuthStack.Screen name="Register" component={Register}/>
            </AuthStack.Navigator>
        </NavigationContainer>
    )
}