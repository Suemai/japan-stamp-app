import React from "react";
import { createMaterialTopTabNavigator } from "@react-navigation/material-top-tabs";
import { withLayoutContext } from "expo-router";
// import {Tabs} from "expo-router";

const { Navigator } = createMaterialTopTabNavigator();
export const MaterialTopTabs = withLayoutContext(Navigator);

const _layout = () => {
    return (
        <MaterialTopTabs
            screenOptions={{
                tabBarActiveTintColor: '#ffffff',
                tabBarInactiveTintColor: 'rgba(255,255,255,0.6)',
                tabBarStyle: {
                    backgroundColor: '#399FC6',
                    height: 80,
                },
                tabBarLabelStyle: {
                    paddingTop:40,
                    fontSize: 15,
                    fontWeight: 'bold',
                    textTransform: 'none',
                },
                tabBarIndicatorStyle: {
                    backgroundColor: '#ffffff',
                    height: 3,
                },
            }}>

            <MaterialTopTabs.Screen
                name="obtained"
                options={{
                    title: "Obtained",
                    headerShown: false,
                }}/>

            <MaterialTopTabs.Screen
            name="wishlist"
            options={{
                title: "Wishlist",
                headerShown: false,
            }}/>

            <MaterialTopTabs.Screen
                name="custom"
                options={{
                    title: "Custom",
                    headerShown: false,
            }}/>
        </MaterialTopTabs>
    );
}

export default _layout