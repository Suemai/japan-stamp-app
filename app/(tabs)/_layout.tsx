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
    return (
        <View
            className=" flex flex-row flex-1
            items-center justify-center
            min-h-16 mt-1.5
            rounded-full"
            style={{
                backgroundColor: focused ? "#8658ff" : "transparent",
                minWidth: focused ? 90 : 44,
                paddingVertical: 6,
                paddingHorizontal: 10,
            }}
            >
            <Image
                source={icon}
                tintColor="#EEEDFE"
                className="size-5"
            />
            {focused && (
                <Text className="text-primary-background text-base ml-2 font-bold"
                      style={{
                          flexShrink: 1,
                          textAlign: 'center',
                      }}>
                    {title}
                </Text>
            )}
        </View>
    );
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
                paddingTop: 6,
                paddingBottom: 6,
                height: 55,
                position: 'absolute',
                borderWidth: 0,
                borderColor: 'transparent',
                overflow: 'hidden',
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