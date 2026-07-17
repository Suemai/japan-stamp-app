import {Image, StyleSheet, Text, TextInput, View, Alert, Platform, ScrollView, TouchableOpacity} from "react-native";
import React, {useState} from "react";
import {router} from "expo-router";
import {SafeAreaView} from "react-native-safe-area-context";
import MaterialCommunityIcons from "react-native-vector-icons/MaterialCommunityIcons";
import {FontAwesome6} from "@expo/vector-icons";
import {colours} from "@/constants/colours";
import {Dropdown} from "react-native-element-dropdown";

function SectionDividers({label, variant}: {label: string; variant: 'location' | 'stamp'}) {
    const isLocation = variant === 'location';
    return (
        <View style = {styles.sectionDivider}>
            <View style = {styles.sectionLine}/>
            <View style={[styles.sectionBadge, isLocation ? styles.badgeLocation : styles.badgeStamp]}>
                <Text style={[styles.sectionBadgeText, isLocation ? styles.badgeLocationText : styles.badgeStampText]}>
                    {label}
                </Text>
            </View>
            <View style={styles.sectionLine} />
        </View>
    );
}

function FieldLabel({ children, required }: { children: string; required?: boolean }) {
    return (
        <View style={styles.fieldLabelRow}>
            <Text style={styles.fieldLabel}>{children}</Text>
            {required && <Text style={styles.requiredDot}>*</Text>}
        </View>
    );
}

function StampPhotoPicker({photo, onAdd, onRemove,}: {
    photo: StampPhoto | null;
    onAdd:() => void;
    onRemove:() => void}) {
    if (photo) {
        return (
            <View style = {styles.photoPreview}>
                <Image source={{uri: photo.uri}} style={styles.photoImage} />
                <TouchableOpacity style = {styles.photoRemove} onPress={onRemove}>
                    <Text style = {styles.photoRemoveIcon}>X</Text>
                </TouchableOpacity>
                <TouchableOpacity style={styles.photoChange} onPress={onAdd} activeOpacity={0.8}>
                    <Text style={styles.photoChangeText}>Change Photo</Text>
                </TouchableOpacity>
            </View>
        );
    }
    return (
        <TouchableOpacity style={styles.photoEmpty} onPress={onAdd} activeOpacity={0.7}>
            <Text style={styles.photoEmptyIcon}>+</Text>
            <Text style={styles.photoEmptyTitle}>Add Photo</Text>
            <Text style={styles.photoEmptyHint}>Add a photo of the stamp</Text>
        </TouchableOpacity>
    )
}


export default function AddNewStampLocation({onSubmit}: {
    onSubmit: (location: LocationForm, stamp: StampData[]) => void}) {

    const [locationName, setLocationName] = useState('');
    const [address, setAddress] = useState('');
    const [usingCurrentLocation, setUsingCurrentLocation] = useState(true);
    const [hours, setHours] = useState<OpeningHours>({
        mon: {open: true, openTime: '09:00', closeTime: '17:00'},
        tue: {open: true, openTime: '09:00', closeTime: '17:00'},
        wed: {open: true, openTime: '09:00', closeTime: '17:00'},
        thu: {open: true, openTime: '09:00', closeTime: '17:00'},
        fri: {open: true, openTime: '09:00', closeTime: '17:00'},
        sat: {open: false, openTime: '09:00', closeTime: '17:00'},
        sun: {open: false, openTime: '09:00', closeTime: '17:00'},
    });
    const [holidayMode, setHolidayMode] = useState<HolidayMode>('none');
    const [holidayDetails, setHolidayDetails] = useState('');
    const [stampAvailable, setStampAvailable] = useState(true);
    const [hasFee, setHasFee] = useState(false);
    const [fees, setFees] = useState('');
    const [feeCurrency, setFeeCurrency] = useState('GBP');

    const [stamps, setStamps] = useState<StampData[]>([
        {stampName: '', stampPhoto: null},
    ]);

    const currencies = [
        { label: "GBP (£)", value: "GBP" },
        { label: "USD ($)", value: "USD" },
        { label: "EUR (€)", value: "EUR" },
        { label: "JPY (¥)", value: "JPY" },
        { label: "CNY (¥)", value: "CNY" },
    ];

    const handleClose = () => {
        router.back();
    }

    // Location
    function handleUseCurrentLocation() {
        // ToDo - This doesn't work yet
        setUsingCurrentLocation(true);
        setAddress('');
    }

    function handleAddressChange(text: string) {
        setAddress(text);
        setUsingCurrentLocation(false);
    }

    // Photo
    function handleAddStamp() {
        setStamps(prev => [...prev, {stampName: '', stampPhoto: null}]);
    }

    function handleRemoveStamp(index: number) {
        setStamps(prev => prev.filter((_, i) => i !== index));
    }

    function handleAddStampPhoto() {
        // ToDo - This bit also doesn't work yet
        Alert.alert("Add Stamp Photo", "This feature is not implemented yet.");
    }

    function handleRemoveStampPhoto(index: number) {
        setStamps(prev => prev.filter((_, i) => i !== index));
    }

    function handleStampNameChange(index: number, name: string) {
        setStamps(prev => prev.map((s, i) => i === index ? {...s, stampPhoto: null} : s));
    }

    // Submission
    function handleSubmit() {
        if (!locationName.trim()) {
            Alert.alert('Missing info', 'Please enter a location name.');
            return;
        }
        if (!usingCurrentLocation && !address.trim()) {
            Alert.alert('Missing info', 'Please enter an address or use your current location.');
            return;
        }
        for (let i = 0; i < stamps.length; i++) {
            if (!stamps[i].stampName.trim()) {
                Alert.alert('Missing info', `Please give stamp ${i + 1} a name.`);
                return;
            }
            if (!stamps[i].stampPhoto) {
                Alert.alert('Missing photo', `Stamp ${i + 1} needs a photo.`);
                return;
            }
        }

        const locationData: LocationForm = {
            name: locationName.trim(),
            address: usingCurrentLocation ? 'current' : address.trim(),
            usingCurrentLocation,
            hours,
            holidayMode,
            holidayDetails: holidayMode === 'known' ? holidayDetails.trim() : '',
            stampAvailable,
            hasFee,
            feeAmount: hasFee ? fees.trim() : '',
            feeCurrency: hasFee ? feeCurrency : '',
        }
        onSubmit?.(locationData, stamps);
    }

    return (
        <SafeAreaView style={{flex: 1}}>

            <View style={styles.header}>
                <Text style={styles.headerTitle}>Report a location</Text>
                <TouchableOpacity onPress={handleClose} hitSlop={{ top: 8, bottom: 8, left: 8, right: 8 }}>
                    <MaterialCommunityIcons
                        name="close"
                        size={24}
                    />
                </TouchableOpacity>
            </View>

            <ScrollView
                style={styles.scroll}
                contentContainerStyle={styles.scrollContent}
                keyboardShouldPersistTaps="always"
                showsHorizontalScrollIndicator={false}>

                {/*--- Location form --- */}
                <SectionDividers label="Location" variant="location"/>

                <FieldLabel required>Name</FieldLabel>
                <TextInput
                    style={styles.input}
                    placeholder="e.g. Gloucester Cathedral"
                    placeholderTextColor="#aaa"
                    value={locationName}
                    onChangeText={setLocationName}
                    returnKeyType="next"
                />

                <FieldLabel required>Address / pin</FieldLabel>
                <View style={styles.addressRow}>
                <TextInput
                    style={[styles.input, styles.addressInput]}
                    placeholder={usingCurrentLocation ? 'Use your current location' : 'Enter an address'}
                    placeholderTextColor="#aaa"
                    value={usingCurrentLocation ? '' : address}
                    onChangeText={handleAddressChange}
                    editable={!usingCurrentLocation}
                    returnKeyType="next"
                />
                    <TouchableOpacity
                        style={[styles.locationButton, usingCurrentLocation && styles.locationButtonActive]}
                        onPress={handleUseCurrentLocation}
                        activeOpacity={0.7}
                    >
                        {/* ToDo: button needs to open map and pick out location by pin */}
                        <FontAwesome6
                            name="location-crosshairs"
                            size={24}
                            color={"#534AB7"}
                        />

                    </TouchableOpacity>
                </View>

                <FieldLabel>Opening hours</FieldLabel>
                {/*ToDo: Placeholder for now. There'll be a summery on top with a picker below.
                The days of the week will have a switch for open and closed.
                When opened there is a time picker for opening times.
                The summery will automatically update based on the date and time picked.
                The summery will be used for the opening times, which people see on the stamp page.
                */}
                <TextInput
                    style={styles.input}
                    placeholder="e.g. Mon–Sat 9am–5pm, Sun 12–4pm"
                    placeholderTextColor="#aaa"
                    returnKeyType="next"
                />

                <FieldLabel>Holiday closures</FieldLabel>
                <View style={styles.toggleRow}>
                    {(['known', 'unknown', 'none'] as HolidayMode[]).map(mode => (
                        <TouchableOpacity
                            key={mode}
                            style={[styles.toggleButton, holidayMode === mode && styles.toggleButtonActive]}
                            onPress={() => setHolidayMode(mode)}
                        >
                            <Text style={[styles.toggleButtonText, holidayMode === mode && styles.toggleButtonTextActive]}>
                                {mode.charAt(0).toUpperCase() + mode.slice(1)}
                            </Text>
                        </TouchableOpacity>
                    ))}
                </View>
                {holidayMode === 'known' && (
                    <TextInput
                        style={styles.input}
                        placeholder="e.g. Closed Christmas Day & Boxing Day"
                        placeholderTextColor="#aaa"
                        value={holidayDetails}
                        onChangeText={setHolidayDetails}
                        returnKeyType="next"
                    />
                )}

                <FieldLabel required>Stamp available?</FieldLabel>
                <View style={styles.yayNayRow}>
                    <TouchableOpacity
                        style={[styles.yayNayButton, stampAvailable && styles.yayNayButtonActive]}
                        onPress={() => setStampAvailable(true)}
                    >
                        <Text style={[styles.yayNayButtonText, stampAvailable && styles.yayNayButtonTextActive]}>Yes</Text>
                    </TouchableOpacity>
                    <TouchableOpacity
                        style={[styles.yayNayButton, !stampAvailable && styles.yayNayButtonActive]}
                        onPress={() => setStampAvailable(false)}
                    >
                        <Text style={[styles.yayNayButtonText, !stampAvailable && styles.yayNayButtonTextActive]}>No</Text>
                    </TouchableOpacity>
                </View>

                <FieldLabel>Entry fee</FieldLabel>
                <View style={styles.yayNayRow}>
                    <TouchableOpacity
                        style={[styles.yayNayButton, hasFee && styles.yayNayButtonActive]}
                        onPress={() => setHasFee(true)}
                    >
                        <Text style={[styles.yayNayButtonText, hasFee && styles.yayNayButtonTextActive]}>Yes</Text>
                    </TouchableOpacity>
                    <TouchableOpacity
                        style={[styles.yayNayButton, !hasFee && styles.yayNayButtonActive]}
                        onPress={() => setHasFee(false)}
                    >
                        <Text style={[styles.yayNayButtonText, !hasFee && styles.yayNayButtonTextActive]}>Free</Text>
                    </TouchableOpacity>
                </View>
                {hasFee && (
                    <View style={styles.feeRow}>
                        <TextInput
                            style={[styles.input, styles.feeInput]}
                            placeholder="0.00"
                            placeholderTextColor="#aaa"
                            value={fees}
                            onChangeText={setFees}
                            keyboardType="decimal-pad"
                            returnKeyType="next"
                        />
                        <Dropdown
                            style={styles.currencyButton}
                            placeholderStyle={styles.currencyButtonText}
                            selectedTextStyle={styles.currencyButtonText}
                            data={currencies}
                            labelField="label"
                            valueField="value"
                            value={feeCurrency}
                            onChange={setFeeCurrency}
                        />
                    </View>
                )}



                {/* Add Stamp */}
                <SectionDividers label="Stamp" variant="stamp"/>

                {stamps.map((stamp, index) => (
                    <View key={index} style={styles.stampCard}>
                        <View style={styles.stampCardHeader}>
                            <Text style={styles.stampCardTitle}>Stamp {index + 1}</Text>
                            {stamps.length > 1 && (
                                <TouchableOpacity onPress={() => handleRemoveStamp(index)}>
                                    <MaterialCommunityIcons
                                        name="delete-outline"
                                        size={23}
                                    />
                                </TouchableOpacity>
                            )}
                        </View>
                        <View style={styles.stampCardBody}>
                            <FieldLabel required>Stamp name</FieldLabel>
                            <TextInput
                                style={styles.input}
                                placeholder="Name of ze stamp"
                                placeholderTextColor="#aaa"
                                value={stamp.stampName}
                                onChangeText={text => handleStampNameChange(index, text)}
                                returnKeyType="done"
                            />
                            <FieldLabel required>Add Stamp Photo</FieldLabel>
                            <StampPhotoPicker
                                photo={stamp.stampPhoto}
                                onAdd={handleAddStampPhoto}
                                onRemove={() => handleRemoveStampPhoto(index)}
                            />
                        </View>
                    </View>
                ))}

                <TouchableOpacity style={styles.addStampButton} onPress={handleAddStamp}>
                    <Text style={styles.addStampButtonText}>+ Add another stamp</Text>
                </TouchableOpacity>


                {/* Submit */}
                <TouchableOpacity
                    style={[styles.submitButton, ((stamps.some(s => !s.stampPhoto || !s.stampName)) && styles.submitButtonDisabled)]}
                    onPress={handleSubmit}
                    activeOpacity={0.85}
                >
                    <Text style={styles.submitButtonText}>Submit</Text>
                </TouchableOpacity>

            </ScrollView>
        </SafeAreaView>
    );
}

const styles = StyleSheet.create({

    // Header
    header: {
        flexDirection:'row',
        alignItems:'center',
        justifyContent:'space-between',
        paddingHorizontal: 20,
        paddingVertical: 14,
        borderBottomWidth: 0.5,
        borderBottomColor: colours.border,
    },
    headerTitle:{
      fontSize: 20,
      fontWeight: '600',
      color: colours.text.primary,
    },
    closeIcon: {
        width: 25,
        height: 25,
        tintColor: colours.text.secondary,
    },

    // Scroll
    scroll:{
        flex: 1,
    },
    scrollContent:{
        paddingHorizontal: 20,
        paddingBottom: 48
    },

    // Section divider
    sectionDivider: {
        flexDirection: 'row',
        alignItems: 'center',
        marginTop: 24,
        marginBottom: 16,
    },
    sectionLine: {
        flex: 1,
        height: 1.5,
        backgroundColor: colours.border,
    },
    sectionBadge: {
        paddingHorizontal: 10,
        paddingVertical: 5,
        borderRadius: 20,
    },
    badgeLocation: {backgroundColor: colours.secondary.light},
    badgeStamp: { backgroundColor: colours.primary.background},
    sectionBadgeText: {
        fontSize: 14,
        fontWeight: '600',
    },
    badgeLocationText: { color: colours.secondary.dark },
    badgeStampText: { color: colours.primary.dark },

    // Field labels
    fieldLabelRow: {
        flexDirection: 'row',
        alignItems: 'center',
        marginBottom: 6,
        gap: 3,
    },
    fieldLabel: {
        fontSize: 13,
        fontWeight: '600',
        letterSpacing: 0.6,
        textTransform: 'uppercase',
    },
    requiredDot: {
        fontSize: 13,
        fontWeight: '700',
        lineHeight: 16,
    },

    // Inputs
    input:{
        backgroundColor: colours.input.background,
        borderWidth: 0.5,
        borderColor: colours.border,
        borderRadius:8,
        paddingHorizontal: 12,
        paddingVertical: Platform.OS === 'ios' ? 12 : 8,
        fontSize: 15,
        color: colours.text.primary,
        marginBottom: 14,
    },

    // Location part
    addressRow: {
        flexDirection: 'row',
        gap: 8,
        marginBottom: 14,
    },
    addressInput: {
        flex: 1,
        marginBottom: 0,
    },
    locationButton: {
        width: 44,
        height: 44,
        borderRadius: 8,
        borderWidth: 0.5,
        borderColor: colours.border,
        backgroundColor: colours.input.background,
        alignItems: 'center',
        justifyContent: 'center',
    },
    locationButtonActive: {
        borderColor: colours.primary.default,
        backgroundColor: colours.primary.background,
    },
    toggleRow: {
        flexDirection: 'row',
        gap: 8,
        marginBottom: 12,
    },
    toggleButton: {
        flex: 1,
        paddingVertical: 9,
        borderRadius: 8,
        borderWidth: 0.5,
        borderColor: colours.border,
        backgroundColor: colours.input.background,
        alignItems: 'center',
    },
    toggleButtonActive: {
        backgroundColor: colours.primary.background,
        borderColor: colours.primary.default,
    },
    toggleButtonText: {
        fontSize: 13,
        fontWeight: '500',
        color: colours.text.secondary,
    },
    toggleButtonTextActive: {
        color: colours.primary.default,
    },
    yayNayRow: {
        flexDirection: 'row',
        gap: 8,
        marginBottom: 12,
    },
    yayNayButton: {
        flex: 1,
        paddingVertical: 9,
        borderRadius: 8,
        borderWidth: 0.5,
        borderColor: colours.border,
        backgroundColor: colours.input.background,
        alignItems: 'center',
    },
    yayNayButtonActive: {
        backgroundColor: colours.secondary.light,
        borderColor: colours.secondary.default,
    },
    yayNayButtonText: {
        fontSize: 13,
        fontWeight: '500',
        color: colours.text.secondary,
    },
    yayNayButtonTextActive: {
        color: colours.secondary.dark,
    },
    feeRow: {
        flexDirection: 'row',
        gap: 8,
        marginBottom: 14,
    },
    feeInput: {
        flex: 1,
        marginBottom: 0,
    },
    currencyButton: {
        paddingHorizontal: 14,
        flex: 0.2,
        height: 44,
        borderRadius: 8,
        borderWidth: 0.5,
        borderColor: colours.border,
        backgroundColor: colours.primary.background,
        alignItems: 'center',
        justifyContent: 'center',
    },
    currencyButtonText: {
        fontSize: 13,
        fontWeight: '600',
        color: colours.primary.default,
    },

    // stamps part
    stampCard: {
        borderWidth: 0.5,
        borderColor: colours.border,
        borderRadius: 10,
        overflow: 'hidden',
        marginBottom: 12,
    },
    stampCardHeader:{
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
        paddingHorizontal: 14,
        paddingVertical: 10,
        backgroundColor: colours.input.background,
        borderBottomWidth: 0.5,
        borderBottomColor: colours.border,
    },
    stampCardTitle: {
        fontSize: 13,
        fontWeight: '600',
        color: colours.text.primary,
    },
    stampCardBody:{
        padding: 14,
    },

    // photo preview
    photoPreview: {
        width: '100%',
        height: 200,
        borderRadius: 12,
        overflow: 'hidden',
        marginBottom: 20,
        position: 'relative',
    },
    photoImage: {
        width: '100%',
        height: '100%',
    },
    photoRemove:{
        position: 'absolute',
        top: 10,
        right: 10,
        width: 26,
        height: 26,
        borderRadius: 13,
        backgroundColor: 'rgba(0,0,0,0.55)',
        alignItems: 'center',
        justifyContent: 'center',
    },
    photoRemoveIcon:{
        fontSize: 11,
        color: 'white',
        fontWeight: '700',
    },
    photoChange:{
        position: 'absolute',
        bottom: 10,
        alignSelf: 'center',
        backgroundColor: 'rgba(0,0,0,0.55)',
        paddingHorizontal: 14,
        paddingVertical: 6,
        borderRadius: 20,
    },
    photoChangeText:{
        fontSize: 12,
        color: 'white',
        fontWeight: '500',
    },

    // Photo Empty
    photoEmpty:{
        borderWidth: 1,
        borderStyle: 'dashed',
        borderColor: colours.primary.default,
        borderRadius: 12,
        backgroundColor: colours.primary.background,
        paddingVertical: 28,
        alignItems: 'center',
        justifyContent: 'center',
        gap: 6,
        marginBottom: 20,
    },
    photoEmptyIcon:{
        fontSize: 28,
        marginBottom: 4,
    },
    photoEmptyTitle:{
        fontSize: 14,
        fontWeight: '600',
        color: colours.primary.default,
    },
    photoEmptyHint:{
        fontSize: 12,
        color: colours.primary.dark,
        opacity: 0.7,
        textAlign: 'center',
        paddingHorizontal: 20,
    },

    // Add stamp button
    addStampButton: {
        borderWidth: 0.5,
        borderStyle: 'dashed',
        borderColor: colours.border,
        borderRadius: 10,
        paddingVertical: 13,
        alignItems: 'center',
        marginBottom: 16,
        backgroundColor: colours.input.background,
    },
    addStampButtonText: {
        fontSize: 14,
        color: colours.text.secondary,
        fontWeight: '500',
    },

    // Submission
    submitButton: {
        backgroundColor: colours.primary.default,
        borderRadius: 12,
        paddingVertical: 15,
        alignItems: 'center',
        marginTop: 8,
    },
    submitButtonDisabled:{
        opacity: 0.45,
    },
    submitButtonText:{
        color: 'white',
        fontSize: 16,
        fontWeight: '600',
    }
});
