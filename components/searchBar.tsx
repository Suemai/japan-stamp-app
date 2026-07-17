import {View, Image, TextInput} from 'react-native';
import React from 'react';
import MaterialCommunityIcons from "react-native-vector-icons/MaterialCommunityIcons";
import {FontAwesome6} from "@expo/vector-icons";

interface Props {
    placeholder: string;
    // onPress?: () => void;
}

const SearchBar = ({placeholder}:Props) => {

    return (
        <View style={{
            flexDirection: "row",
            alignItems: "center",
            backgroundColor: "#ffffff",
            borderRadius: 50,
            paddingHorizontal: 15,
            paddingVertical: 8
        }}>
            <FontAwesome6 name="magnifying-glass" size={20} color="#ab8bff" />
            <TextInput
                onPress={() => {}}
                placeholder={placeholder}
                value=""
                onChangeText={()=>{}}
                placeholderTextColor={"#a8b5db"}
                className="flex-1 ml-2 text-light-text">
            </TextInput>
        </View>
    );
}

export default SearchBar;