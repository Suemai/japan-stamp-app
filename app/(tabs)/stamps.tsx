import StampCard from "@/components/homeDetails/stampCard";
import { fetchStamps, StampRowMinimal } from "@/lib/stampApi";
import { useEffect, useState } from "react";
import { FlatList } from "react-native";

interface StampTile {
    id: string;
    name: string;
    image_url: string;
}

export default function Stamps() {
    const [allStamps, setAllStamps] = useState<StampTile[]>([]);

    useEffect(() => {
        fetchStamps({ minimal: true })
            .then((rows: StampRowMinimal[]) => setAllStamps(rows.map(row => ({
                id: row.id,
                name: row.name,
                image_url: row.image_url,
            }))))
            .catch(error => console.error('Failed to fetch stamps:', error));
    }, []);

    // const sorted = [...allStamps].sort((a, b) => a.name.localeCompare(b.name));
    const sorted = allStamps;

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
                paddingTop: 40,
                paddingLeft: 10,
                paddingRight: 10,
                paddingBottom: 85
            }}
        >
        </FlatList>
    );
}