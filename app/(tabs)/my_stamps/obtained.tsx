import {FlatList, Text, View} from "react-native";
import StampCard from "@/components/stampCard";
import {PLACEHOLDER_LOCATIONS} from "@/data/tempData";

const Obtained = () => {

    const obtainedStamps = PLACEHOLDER_LOCATIONS.flatMap(location => location.stamps)
        .filter(stamp => stamp.obtained)
        .sort((a, b) => a.name.localeCompare(b.name));

    return (
        <FlatList
            data={obtainedStamps}
            keyExtractor={(item) => item.id.toString()}
            renderItem={({ item }) => (
                <StampCard
                    id={item.id}
                    name = {item.name}
                    imageUri = {item.image}
                />
            )}
            numColumns={3}
            columnWrapperStyle={{
                justifyContent: 'flex-start',
                gap: 20,
                padding: 5,
                marginBottom: 10
            }}
            contentContainerStyle={{
                paddingTop: 20,
                paddingLeft: 10,
                paddingRight: 10,
                paddingBottom: 85
            }}
        >
        </FlatList>
    );
}

export default Obtained