import React from "react";
import {Tabs} from "expo-router";
import {Image, Text, View} from "react-native";

const icons = {
    explore: require("../../assets/images/icons/navigation.png"),
    my_stamps: require("../../assets/images/icons/stamp.png"),
    stamps: require("../../assets/images/icons/list.png"),
    more: require("../../assets/images/icons/more.png"),
}

const TabIcon = ({ focused, icon, title }: any) => {
    if(focused){
    return (
        <View
            className="flex flex-row
            flex-1 w-full items-center justify-center
            min-w-[112px] min-h-16 mt-4
            rounded-full"
            style={{
                backgroundColor: "#8658ff",
            }}
            >
            <Image
                source={icon}
                style={{
                    width: 20,
                    height: 20,
                }}
                tintColor="#EEEDFE"
                className={"size-5"}
            />
            <Text className="text-primary-background text-base ml-2 font-bold">{title}</Text>
        </View>
    );
    } else {
        return (
            <View className= "size-full justify-center items-center mt-4 rounded-full">
                <Image source={icon}
                       style={{
                           width: 20,
                           height: 20, }}
                tintColor="#EEEDFE"
                className={"size-5"}/>
            </View>
        );
    }
}

const _layout = () => {
    return (
        <Tabs
        screenOptions={{
            tabBarShowLabel: false,
            tabBarItemStyle:{
                justifyContent: 'center',
                alignItems: 'center',
            },
            tabBarStyle:{
                backgroundColor: '#ab8bff',
                borderRadius: 50,
                marginHorizontal: 20,
                marginBottom: 35,
                height: 55,
                position: 'absolute',
                overflow: 'hidden',
                borderWidth: 0,
                borderColor: 'transparent',
            },

        }}>
            <Tabs.Screen
                name="index"
                options={{
                    title: "Explore",
                    headerShown: false,
                    tabBarIcon: ({ focused }) => (
                        <TabIcon
                            focused={focused}
                            icon={icons.explore}
                            title="Explore"
                        />
                    ),
                }}
            />
            <Tabs.Screen
                name="my_stamps"
                options={{
                    title: "My Stamps",
                    headerShown: false,
                    tabBarIcon: ({ focused }) => (
                        <TabIcon
                            focused={focused}
                            icon={icons.my_stamps}
                            title="My Stamps"
                        />
                    ),
                }}
            />
            <Tabs.Screen
                name="stamps"
                options={{
                    title: "Stamps",
                    headerShown: false,
                    tabBarIcon: ({ focused }) => (
                        <TabIcon
                            focused={focused}
                            icon={icons.stamps}
                            title="All Stamps"
                        />
                    ),
                }}
            />
            <Tabs.Screen
                name="more"
                options={{
                    title: "More",
                    headerShown: false,
                    tabBarIcon: ({ focused }) => (
                        <TabIcon
                            focused={focused}
                            icon={icons.more}
                            title="More"
                        />
                    ),
                }}
            />
        </Tabs>
    );
}

export default _layout