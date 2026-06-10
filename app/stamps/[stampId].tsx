import {View, Text, Image, StyleSheet, TextInput, ScrollView} from 'react-native'
import React, {useState} from 'react'
import { TabView, TabBar, SceneRendererProps, NavigationState } from "react-native-tab-view";
import {useLocalSearchParams, useRouter} from "expo-router";
import {PLACEHOLDER_LOCATIONS} from "@/data/tempData";

/* The page that shows details of the stamp, not the stamp set!
TODO:
- Make it look nice!

- This is basically your bit partner!
- Fetch data from database to display it here
-> will be temp data for now

 */

type RouteKey = "notes" | "comments";

type Route = {
    key: RouteKey;
    title: string;
};

const NotesTab = ({ notes, onChangeNotes }: { notes: string; onChangeNotes: (v: string) => void }) => (
    <ScrollView
        style={styles.scene}
        contentContainerStyle={styles.sceneContent}
        keyboardShouldPersistTaps="handled">

        <TextInput
            style={styles.notesInput}
            value={notes}
            onChangeText={onChangeNotes}
            placeholder="Add your notes here..."
            placeholderTextColor="#aaa"
            multiline
            textAlignVertical="top"
        />
    </ScrollView>
);

const CommentsTab = () => (
    <ScrollView
        style={styles.scene}
        contentContainerStyle={styles.sceneContent}>

        <Text style={styles.emptyText}>
            No comments yet.
        </Text>

    </ScrollView>
);

const StampHeader = ({ stamp }: { stamp: any }) => (
    <View style={styles.header}>
        <Image source={{ uri: stamp.image }} style={styles.headerImage} />
        <View style={styles.headerInfo}>
            <Text style={styles.stampName}>{stamp.name}</Text>
            <Text style={styles.stampLocation}>{stamp.location}</Text>

            <View style={styles.headerRow}>
                <Text style={styles.headerLabel}>Address</Text>
                <Text style={styles.headerValue}>{stamp.address}</Text>
            </View>

            <View style={styles.headerRow}>
                <Text style={styles.headerLabel}>Availability</Text>
                <Text style={styles.headerValue}>{stamp.availability ?? "—"}</Text>
            </View>

            <View style={styles.headerRow}>
                <Text style={styles.headerLabel}>Hours</Text>
                <Text style={styles.headerValue}>{stamp.openingHours}</Text>
            </View>

            <View style={styles.headerRow}>
                <Text style={styles.headerLabel}>Holiday</Text>
                <Text style={styles.headerValue}>{stamp.holiday}</Text>
            </View>

            <View style={styles.headerRow}>
                <Text style={styles.headerLabel}>Fee</Text>
                <Text style={styles.headerValue}>{stamp.fee}</Text>
            </View>
        </View>
    </View>
);

const routes: Route[] = [
    { key: "notes", title: "Notes" },
    { key: "comments", title: "Comments" },
];

const StampDetails = () => {

    const {stampId} = useLocalSearchParams();
    const router = useRouter();
    const stamp = PLACEHOLDER_LOCATIONS.find(s=>s.id === Number(stampId));

    const [obtained, setObtained] = useState(stamp?.obtained ?? false);
    const [wishlisted, setWishlisted] = useState(stamp?.wishlisted ?? false);
    const [notes, setNotes] = useState(stamp?.notes ?? '');
    const [dateObtained, setDateObtained] = useState(stamp?.dateObtained ?? new Date());
    const [tabIndex, setTabIndex] = useState(0);

    if (!stamp) {
        // console.log("Placeholders: "+ PLACEHOLDER_LOCATIONS);
        // console.log(stamp);
        // console.log("id:", stampId, "typeof:", typeof stampId);
        return <Text>Stamp not found</Text>
    }

    const renderScene = ({ route }: { route: Route }) => {
        switch (route.key) {
            case "notes":
                return <NotesTab notes={notes} onChangeNotes={setNotes} />;
            case "comments":
                return <CommentsTab />;
            default:
                return null;
        }
    };

    const renderTabBar = (
        props: SceneRendererProps & { navigationState: NavigationState<Route> }
    ) => (
        <TabBar
            {...props}
            style={styles.tabBar}
            indicatorStyle={styles.tabIndicator}
            activeColor="#111"
            inactiveColor="#999"
            pressColor="transparent"
        />
    );

    return (
        <View style={styles.screen}>
            <StampHeader stamp={stamp} />

            <TabView
                navigationState={{ index: tabIndex, routes }}
                renderScene={renderScene}
                renderTabBar={renderTabBar}
                onIndexChange={setTabIndex}
            />
        </View>
    )
}

const styles = StyleSheet.create({
    container: {
        flexDirection: "row",
        padding: 16,
    },
    screen: {
        flex: 1,
        backgroundColor: "#fff",
    },
    header: {
        flexDirection: "row",
        alignItems: "center",
        padding: 16,
        borderBottomWidth: StyleSheet.hairlineWidth,
        borderBottomColor: "#e0e0e0",
    },
    headerImage: {
        width: 130,
        height: 130,
        borderRadius: 8,
    },
    headerInfo: {
        flex: 1,
        marginLeft: 14,
    },
    stampName: {
        fontSize: 20,
        fontWeight: "600",
        color: "#111",
        marginBottom: 2,
        alignSelf: "center",
    },
    stampLocation: {
        fontSize: 14,
        color: "#888",
        marginBottom: 8,
        alignSelf: "center",
    },
    headerRow: {
        flexDirection: "row",
        marginBottom: 3,
    },
    headerLabel: {
        fontSize: 14,
        color: "#aaa",
        width: 80,
    },
    headerValue: {
        fontSize: 14,
        color: "#444",
        flex: 1,
    },
    tabBar: {
        backgroundColor: "#fff",
        borderBottomWidth: StyleSheet.hairlineWidth,
        borderBottomColor: "#e0e0e0",
        elevation: 0,
        shadowOpacity: 0,
    },
    tabIndicator: {
        backgroundColor: "#111",
        height: 2,
    },
    tabLabel: {
        fontSize: 13,
        fontWeight: "500",
        textTransform: "none",
    },
    scene: {
        flex: 1,
    },
    sceneContent: {
        padding: 16,
        flexGrow: 1,
    },
    notesInput: {
        flex: 1,
        minHeight: 200,
        fontSize: 14,
        color: "#222",
        lineHeight: 22,
    },
    emptyText: {
        fontSize: 14,
        color: "#aaa",
        textAlign: "center",
        marginTop: 40,
    },
    notFound: {
        padding: 20,
        fontSize: 14,
        color: "#888",
    },
})

export default StampDetails