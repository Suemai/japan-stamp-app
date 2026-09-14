import StampCard from "@/components/stampCard";
import { fetchUserStamps, StampRow } from "@/lib/stampApi";
import { useFocusEffect } from 'expo-router';
import { useCallback, useState } from 'react';
import { FlatList } from 'react-native';

const Wishlist = ()=> {
    const [wishlistStamps, setWishlistStamps] = useState<StampRow[]>([]);

    useFocusEffect(useCallback(() => {
        let active = true;

        (async () => {
            try {
                const rows = await fetchUserStamps('wishlisted');
                if (active) setWishlistStamps(rows);
            } catch (error) {
                console.error('Failed to fetch wishlist stamps:', error);
                if (active) setWishlistStamps([]);
            }
        })();

        return () => {
            active = false;
        };
    }, []));

    const sorted = [...wishlistStamps].sort((a, b) => a.name.localeCompare(b.name));

    return (
        <FlatList
            data={sorted}
            keyExtractor={(item) => item.id}
            renderItem={({ item }) => (
                <StampCard
                    id={item.id}
                    name={item.name}
                    imageUri={item.image_url}
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