import {View, Text, Image, StyleSheet, TextInput, ScrollView, Platform, TouchableOpacity} from 'react-native'
import React, {useState} from 'react'
import { TabView, TabBar, SceneRendererProps, NavigationState } from "react-native-tab-view";
import {useLocalSearchParams, useRouter} from "expo-router";
import {PLACEHOLDER_LOCATIONS} from "@/data/tempData";
import DateTimePicker, { DateTimePickerEvent } from "@react-native-community/datetimepicker";
import MaterialCommunityIcons from "react-native-vector-icons/MaterialCommunityIcons";

/* The page that shows details of the stamp, not the stamp set!
TODO:
- Make it look nice!

- This is basically your bit partner!
- Fetch data from database to display it here
-> will only display temp data for now
    -> any data that is changed in this page on the user side won't be saved and needs logic to handle the saves
 */

type RouteKey = "notes" | "comments";

type Route = {
    key: RouteKey;
    title: string;
};

const NotesTab = ({
                      notes,
                      onChangeNotes,
                      dateObtained,
                      onChangeDateObtained,
                      obtained,
}: {
    notes: string;
    onChangeNotes: (v: string) => void;
    dateObtained: Date;
    onChangeDateObtained: (d: Date) => void;
    obtained: boolean;
}) => {
    const [showPicker, setShowPicker] = useState(false);

    const handleChange = (_event: DateTimePickerEvent, selected?: Date) => {
        // Android bit
        if (Platform.OS === "android") setShowPicker(false);
        if (selected) onChangeDateObtained(selected);
    };

    return(
        <ScrollView
            style={styles.scene}
            contentContainerStyle={styles.sceneContent}
            keyboardShouldPersistTaps="handled">

            <View style={styles.dateRow}>
                <Text style={styles.dateLabel}>Date obtained</Text>
                <View style={styles.dateRight}>
                    <Text style={[styles.dateValue, !obtained && styles.dateValueUnobtained]}>
                        {obtained
                            ? dateObtained.toLocaleDateString("en-GB", { day: "numeric", month: "short", year: "numeric" })
                            : "Unobtained"}
                    </Text>
                    {obtained && (
                        <TouchableOpacity
                            onPress={() => setShowPicker(true)}
                            hitSlop={{ top: 8, bottom: 8, left: 8, right: 8 }}
                            style={styles.dateIconButton}>

                            <MaterialCommunityIcons name="calendar-edit" size={18} color="#888" />
                        </TouchableOpacity>
                        )}
                </View>
            </View>

            {/* iOS */}
            {showPicker && Platform.OS === "ios" && (
                <View style={styles.iosPickerWrapper}>
                    <DateTimePicker
                        value={dateObtained}
                        mode="date"
                        display="inline"
                        onChange={handleChange}
                        maximumDate={new Date()}
                        locale="en-GB"
                    />
                    <TouchableOpacity style={styles.iosDoneButton} onPress={() => setShowPicker(false)}>
                        <Text style={styles.iosDoneText}>Done</Text>
                    </TouchableOpacity>
                </View>
            )}

            {/* Android */}
            {showPicker && Platform.OS === "android" && (
                <DateTimePicker
                    value={dateObtained}
                    mode="date"
                    display="default"
                    onChange={handleChange}
                    maximumDate={new Date()}
                />
            )}

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
};

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
                <Text style={styles.headerLabel}>Hours</Text>
                <Text style={styles.headerValue}>{stamp.openingHours}</Text>
            </View>

            <View style={styles.headerRow}>
                <Text style={styles.headerLabel}>Holiday</Text>
                <Text style={styles.headerValue}>{stamp.holiday}</Text>
            </View>
        </View>
    </View>
);

type StatsRowProps = {
    fee: string | number;
    availability: boolean | undefined;
    obtained: boolean;
    wishlisted: boolean;
    onToggleObtained: () => void;
    onToggleWishlisted: () => void;
};

const StatsRow = ({
                      fee,
                      availability,
                      obtained,
                      wishlisted,
                      onToggleObtained,
                      onToggleWishlisted,
                  }: StatsRowProps) => (
    <View style={styles.statsRow}>

        <View style={styles.statItem}>
            <MaterialCommunityIcons
                name="cash"
                size={22}
                color="#888"
            />
            <Text style={styles.statLabel}>
                {fee != null ? `${fee}` : "—"}
            </Text>
        </View>

        {/* Availability */}
        <View style={styles.statItem}>
            <MaterialCommunityIcons
                name={availability ? "check-bold" : "close-thick"}
                size={22}
                color={availability ? "#3bc837" : "#fb0422"}
            />
            <Text style={styles.statLabel}>
                {availability === true ? "Available" : availability === false ? "Unavailable" : "—"}
            </Text>
        </View>

        {/* Obtained */}
        <TouchableOpacity style={styles.statItem} onPress={onToggleObtained} activeOpacity={0.7}>
            <MaterialCommunityIcons
                name={obtained ? "check-circle" : "check-circle-outline"}
                size={22}
                color={obtained ? "#4caf50" : "#bbb"}
            />
            <Text style={[styles.statLabel, obtained && styles.statLabel]}>
                Obtained
            </Text>
        </TouchableOpacity>

        {/* Wishlist */}
        <TouchableOpacity style={styles.statItem} onPress={onToggleWishlisted} activeOpacity={0.7}>
            <MaterialCommunityIcons
                name={wishlisted ? "heart" : "heart-outline"}
                size={22}
                color={wishlisted ? "#e53935" : "#bbb"}
            />
            <Text style={[styles.statLabel, wishlisted && styles.statLabel]}>
                Wishlist
            </Text>
        </TouchableOpacity>
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
    const [dateObtained, setDateObtained] = useState<Date>(stamp?.dateObtained ?? new Date());
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
                return <NotesTab
                    dateObtained={dateObtained}
                    onChangeDateObtained={setDateObtained}
                    notes={notes}
                    onChangeNotes={setNotes}
                    obtained={obtained}/>
            case "comments":
                return <CommentsTab />
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

            <StatsRow
                fee={stamp.fee}
                availability={stamp.available}
                obtained={obtained}
                wishlisted={wishlisted}
                onToggleObtained={() => setObtained(v => !v)}
                onToggleWishlisted={() => setWishlisted(v => !v)}
            />

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
    statsRow: {
        flexDirection: "row",
        paddingVertical: 12,
        paddingHorizontal: 8,
        borderBottomWidth: StyleSheet.hairlineWidth,
        borderBottomColor: "#e0e0e0",
        backgroundColor: "#fff",
    },
    statItem: {
        flex: 1,
        alignItems: "center",
        gap: 4,
    },
    statLabel: {
        fontSize: 12,
        color: "#111",
        textAlign: "center",
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
    dateRow: {
        flexDirection: "row",
        justifyContent: "space-between",
        alignItems: "center",
        paddingVertical: 12,
        borderBottomWidth: StyleSheet.hairlineWidth,
        borderBottomColor: "#e0e0e0",
        marginBottom: 16,
    },
    dateLabel: {
        fontSize: 13,
        color: "#888",
    },
    dateValue: {
        fontSize: 13,
        color: "#111",
        fontWeight: "500",
    },
    dateValueUnobtained: {
        color: "#bbb",
        fontWeight: "400",
    },
    dateRight: {
        flexDirection: "row",
        alignItems: "center",
        gap: 8,
    },
    dateIconButton: {
        padding: 2,
    },
    iosPickerWrapper: {
        marginBottom: 16,
        borderRadius: 12,
        overflow: "hidden",
        borderWidth: StyleSheet.hairlineWidth,
        borderColor: "#e0e0e0",
    },
    iosDoneButton: {
        padding: 12,
        alignItems: "flex-end",
        borderTopWidth: StyleSheet.hairlineWidth,
        borderTopColor: "#e0e0e0",
        backgroundColor: "#fafafa",
    },
    iosDoneText: {
        fontSize: 14,
        color: "#111",
        fontWeight: "500",
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