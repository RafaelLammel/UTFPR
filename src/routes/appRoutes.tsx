import React from "react";
import { NavigationContainer } from "@react-navigation/native";
import { createStackNavigator } from "@react-navigation/stack";
import Home from "../pages/Home";
import Configuration from "../pages/Configuration";

const AppStack = createStackNavigator();

export default function AppRoutes() {
    return (
        <NavigationContainer>
            <AppStack.Navigator screenOptions={{headerShown:false}}>
                <AppStack.Screen name="Home" component={Home}/>
                <AppStack.Screen name="Configuration" component={Configuration}/>
            </AppStack.Navigator>
        </NavigationContainer>
    )
}