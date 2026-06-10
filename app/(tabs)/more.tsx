import { Text, View } from "react-native";

/*
Notes for Settings
- show unobtainable stamps?
- Proximity alerts
    -> radius
    -> filter only for certain stamps
    -> battery optimisation -> only when app open or background checks
*/

export default function More() {
    return (
        <View
            style={{
                flex: 1,
                justifyContent: "center",
                alignItems: "center",
            }}
        >
            <Text>More</Text>
        </View>
    );
}
