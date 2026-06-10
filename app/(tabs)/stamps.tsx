import {Animated, FlatList, Text, View} from "react-native";
import StampCard from "@/components/stampCard";
import {PLACEHOLDER_LOCATIONS} from "@/data/tempData";

export default function Stamps() {
    return (
        <FlatList
            data={PLACEHOLDER_LOCATIONS}
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
            }}
        >
        </FlatList>
    );
}