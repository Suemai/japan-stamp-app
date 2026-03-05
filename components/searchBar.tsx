import {View, Image, TextInput} from 'react-native';
import React from 'react';

interface Props {
    placeholder: string;
    // onPress?: () => void;
}

const SearchBar = ({placeholder}:Props) => {

    const searchIcon = require("../assets/images/icons/search.png");


    return (
        <View style={{
            flexDirection: "row",
            alignItems: "center",
            backgroundColor: "#ffffff",
            borderRadius: 50,
            paddingHorizontal: 15,
            paddingVertical: 8
        }}>
            <Image source={searchIcon} className="size-5" resizeMode="contain" tintColor="#ab8bff"/>
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