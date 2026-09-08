import React from 'react';
import {View, Text, ScrollView, StyleSheet} from 'react-native';
import { StampRow } from './stampRow';
import {StampLocation} from "@/data/tempData";
import {buildHoursSummary} from "@/utils/hoursSummary";
import MaterialCommunityIcons from "react-native-vector-icons/MaterialCommunityIcons";
import {colours} from "@/constants/colours";

interface Props {
    location: StampLocation | undefined;
    onSelectStamp: (stampId: number) => void;
}

function Labels({ label }: { label: string }) {
    return (
        <View style={styles.labelRow}>
            <Text style={styles.labelText}>{label.toUpperCase()}</Text>
            <View style={styles.labelRule} />
        </View>
    );
}

function InfoItem({ icon, label, value }: { icon: React.ReactNode; label: string; value: string }) {
    return (
        <View style={styles.infoItem}>
            {icon}
            <Text style={styles.infoLabel}>{label.toUpperCase()}</Text>
            <Text style={styles.infoValue}>{value}</Text>
        </View>
    );
}

function getTodayHours(hours: OpeningHours): string {
    const dayKeys: (keyof OpeningHours)[] = [
        "sun",
        "mon",
        "tue",
        "wed",
        "thu",
        "fri",
        "sat",
    ];

    const todayKey = dayKeys[new Date().getDay()];
    const today = hours[todayKey];

    if (!today.open) {
        return "Closed";
    }

    return `${today.openTime}–${today.closeTime}`;
}

function formatHoliday(holidayMode: HolidayMode, holidayDetails: string): string {
    switch (holidayMode) {
        case "known":
            return holidayDetails
                ? `${holidayDetails}`
                : "Holiday dates known";

        case "unknown":
            return "Unknown";

        case "none":
            return "No holiday closure";

        default:
            return "";
    }
}

export function LocationSheet({
                                  location,
                                  onSelectStamp,
                              }: Props) {
    if (!location) {
        return null;
    }

    return (
        <ScrollView style={styles.content}>
            <Labels label="Location" />
            <Text style={styles.name}
                  numberOfLines={2}>
                {location.name}
            </Text>

            <View style={styles.infoStrip}>
            <InfoItem
                icon={
                <MaterialCommunityIcons
                    name={"clock-time-four-outline"}
                    size={18}
                    color={colours.primary.dark}
                />
            }
                label="Hours"
                value={getTodayHours(location.hours)}
            />
            <InfoItem
                icon={
                <MaterialCommunityIcons
                    name={"calendar-month"}
                    size={18}
                    color={colours.primary.dark}
                />
            }
                label="Holiday"
                value={formatHoliday(location.holidayMode, location.holidayDetails)}
            />
            <InfoItem
                icon={
                <MaterialCommunityIcons
                    name={"ticket"}
                    size={18}
                    color={colours.primary.dark}
                />
            }
                label="Entry fee"
                value={location.feeAmount ? `${location.feeAmount}` + ` ${location.feeCurrency}` : "Free"}
            />
        </View>

            <Labels label="Stamps here" />
            {location.stamps.map((stamp) => (
                <StampRow
                    key={stamp.id}
                    stamp={stamp}
                    onPress={onSelectStamp} />
            ))}
        </ScrollView>
    );
}

const styles = StyleSheet.create({
    content: {
        paddingHorizontal: 22,
        paddingBottom: 26,
        paddingTop: 20,
    },

    labelRow: {
        flexDirection: 'row',
        alignItems: 'center',
        gap: 8,
        marginTop: 10,
        marginBottom: 2
    },
    labelText: {
        fontSize: 15,
        letterSpacing: 1.5,
        color:colours.primary.dark,
    },

    labelRule: {
        flex: 1,
        height: 1,
        backgroundColor: colours.border,
    },

    name: {
        fontSize: 32,
        textTransform: 'uppercase',
        marginTop: 2,
        marginBottom: 14,
        color:colours.primary.default2,
    },

    infoStrip: {
        flexDirection: 'row',
        gap: 14,
        paddingVertical: 12,
        borderTopWidth: 1,
        borderBottomWidth: 1,
        marginBottom: 6,
        borderColor: colours.border
    },
    infoItem: {
        flex: 1,
        gap: 5
    },
    infoLabel: {
        fontSize: 12,
        letterSpacing: 0.6,
        color:colours.primary.default2
    },
    infoValue: {
        fontSize: 12,
        fontWeight: '600',
        lineHeight: 16,
        color:colours.primary.default2,
    },
});