import {FlatList, Text, View} from 'react-native';
import {PLACEHOLDER_LOCATIONS} from "@/data/tempData";
import StampCard from "@/components/stampCard";

const Wishlist = ()=> {

    const sortedLocations = [...PLACEHOLDER_LOCATIONS].sort((a, b) =>
        a.name.localeCompare(b.name)
    );

    return (
        <FlatList
            data={sortedLocations.filter(s => s.wishlisted)}
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
    )
}

export default Wishlist