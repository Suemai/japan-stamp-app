import {View, Text, Image} from 'react-native'
import React, {useState} from 'react'
import {useLocalSearchParams, useRouter} from "expo-router";
import {PLACEHOLDER_LOCATIONS} from "@/data/tempData";

/* The page that shows details of the stamp, not the stamp set!
TODO:
- Make it look nice!

- This is basically your bit partner!
- Fetch data from database to display it here
-> will be temp data for now

 */


const StampDetails = () => {

    const {stampId} = useLocalSearchParams();
    const router = useRouter();
    const stamp = PLACEHOLDER_LOCATIONS.find(s=>s.id === Number(stampId));

    const [obtained, setObtained] = useState(stamp?.obtained ?? false);
    const [wishlisted, setWishlisted] = useState(stamp?.wishlisted ?? false);
    const [notes, setNotes] = useState(stamp?.notes ?? '');
    const [dateObtained, setDateObtained] = useState(stamp?.dateObtained ?? new Date()
    );

    if (!stamp) {
        // console.log("Placeholders: "+ PLACEHOLDER_LOCATIONS);
        // console.log(stamp);
        // console.log("id:", stampId, "typeof:", typeof stampId);
        return <Text>Stamp not found</Text>
    }

    return (
        <View style={styles.container}>
            <Image
                source={{uri: stamp?.image}}
                style={styles.image}/>
            <View style={styles.infoColumn}>
                <Text>{stamp.name}</Text>

                <Text>{stamp.address}</Text>

                <View>
                    <Text>Location: </Text>
                    <Text>{stamp.location}</Text>
                </View>

                <Text>
                    Opening hours:
                </Text>
                <Text>
                    {stamp.openingHours}
                </Text>

                <View>
                    <Text>Holiday: </Text>
                    <Text>
                        {stamp.holiday}
                    </Text>
                </View>
            </View>
        </View>
    )
}

const styles = {
    container: {
        flex: 1,
    },
    image: {
        width: 180,
        height: 180
    },
    infoColumn: {
        flex: 1,
        marginLeft: 10
    },
}

export default StampDetails