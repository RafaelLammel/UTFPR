import React from "react";
import { NavigationContainer } from "@react-navigation/native";
import { createStackNavigator } from "@react-navigation/stack";
import Home from "../pages/Home";
import Configuration from "../pages/Configuration";
import ShareConfig from "../pages/ShareConfig";

const AppStack = createStackNavigator();

export default function AppRoutes() {
    return (
        <NavigationContainer>
            <AppStack.Navigator screenOptions={{headerShown:false}}>
                <AppStack.Screen name="Home" component={Home}/>
                <AppStack.Screen name="Configuration" component={Configuration}/>
                <AppStack.Screen name="ShareConfig" component={ShareConfig} />
            </AppStack.Navigator>
        </NavigationContainer>
    )
}