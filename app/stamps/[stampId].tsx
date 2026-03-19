import {View, Text} from 'react-native'
import React from 'react'
import {useLocalSearchParams} from "expo-router";

/* The page that shows details of the stamp, not the stamp set!
TODO:
- Make it look nice!

- This is basically your bit partner!
- Fetch data from database to display it here
-> will be temp data for now

 */


const StampDetails = () => {

    const {id} = useLocalSearchParams();

    return (
        <View>
            <Text>Stamp details: {id}</Text>
        </View>
    )
}
export default StampDetails