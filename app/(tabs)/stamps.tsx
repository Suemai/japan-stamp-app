import {FlatList} from "react-native";
import StampCard from "@/components/stampCard";
import {PLACEHOLDER_LOCATIONS} from "@/data/tempData";

export default function Stamps() {
    const allStamps = PLACEHOLDER_LOCATIONS.flatMap(location => location.stamps)
        .sort((a, b) => a.name.localeCompare(b.name));

    return (
        <FlatList
            data={allStamps}
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
                paddingTop: 40,
                paddingLeft: 10,
                paddingRight: 10,
                paddingBottom: 85
            }}
        >
        </FlatList>
    );
}