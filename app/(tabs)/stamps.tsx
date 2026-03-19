import {Animated, FlatList, Text, View} from "react-native";
import StampCard from "@/components/stampCard";

const PLACEHOLDER_LOCATIONS = [
    { id: 1,  name: "Tokyo Tower",          image: "https://picsum.photos/seed/tokyo/200" },
    { id: 2,  name: "Eiffel Tower",          image: "https://picsum.photos/seed/paris/200" },
    { id: 3,  name: "Colosseum",             image: "https://picsum.photos/seed/rome/200" },
    { id: 4,  name: "Big Ben",               image: "https://picsum.photos/seed/london/200" },
    { id: 5,  name: "Statue of Liberty",     image: "https://picsum.photos/seed/newyork/200" },
    { id: 6,  name: "Sagrada Família",       image: "https://picsum.photos/seed/barcelona/200" },
    { id: 7,  name: "Machu Picchu",          image: "https://picsum.photos/seed/machu/200" },
    { id: 8,  name: "Great Wall of China",   image: "https://picsum.photos/seed/china/200" },
    { id: 9,  name: "Taj Mahal",             image: "https://picsum.photos/seed/india/200" },
    { id: 10, name: "Sydney Opera House",    image: "https://picsum.photos/seed/sydney/200" },
    { id: 11, name: "Acropolis",             image: "https://picsum.photos/seed/athens/200" },
    { id: 12, name: "Angkor Wat",            image: "https://picsum.photos/seed/angkor/200" },
    { id: 13, name: "Petra",                 image: "https://picsum.photos/seed/petra/200" },
    { id: 14, name: "Chichen Itza",          image: "https://picsum.photos/seed/mexico/200" },
    { id: 15, name: "Mont Saint-Michel",     image: "https://picsum.photos/seed/mont/200" },
    { id: 16, name: "Alhambra",              image: "https://picsum.photos/seed/granada/200" },
    { id: 17, name: "Burj Khalifa",          image: "https://picsum.photos/seed/dubai/200" },
    { id: 18, name: "Hagia Sophia",          image: "https://picsum.photos/seed/istanbul/200" },
    { id: 19, name: "Christ the Redeemer",   image: "https://picsum.photos/seed/rio/200" },
    { id: 20, name: "Neuschwanstein Castle", image: "https://picsum.photos/seed/castle/200" },
    { id: 21, name: "Santorini Cliffs",      image: "https://picsum.photos/seed/santorini/200" },
    { id: 22, name: "Banff National Park",   image: "https://picsum.photos/seed/banff/200" },
    { id: 23, name: "Amalfi Coast",          image: "https://picsum.photos/seed/amalfi/200" },
    { id: 24, name: "Kyoto Bamboo Forest",   image: "https://picsum.photos/seed/kyoto/200" },
    { id: 25, name: "Victoria Falls",        image: "https://picsum.photos/seed/victoria/200" },
    { id: 26, name: "Northern Lights",       image: "https://picsum.photos/seed/aurora/200" },
    { id: 27, name: "Grand Canyon",          image: "https://picsum.photos/seed/canyon/200" },
    { id: 28, name: "Zhangjiajie Mountains", image: "https://picsum.photos/seed/zhangjiajie/200" },
    { id: 29, name: "Meteora Monasteries",   image: "https://picsum.photos/seed/meteora/200" },
    { id: 30, name: "Ha Long Bay",           image: "https://picsum.photos/seed/halong/200" },
];

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