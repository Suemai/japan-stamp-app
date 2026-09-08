import React from "react";
import {View, Text, ScrollView, StyleSheet, Pressable, Image} from 'react-native';
import {Stamp} from "@/data/tempData";
import {colours} from "@/constants/colours";
import MaterialCommunityIcons from "react-native-vector-icons/MaterialCommunityIcons";
import {AvailabilityBadge} from "@/components/availabilityBadge";

interface Props {
    stamp: Stamp | undefined;
    locationName: string;
    onBack: () => void;
    onClose: () => void;
    onToggleWishlist: () => void;
    onToggleObtained: () => void;
    onVote: (direction: 'up' | 'down') => void;
}



export function StampSheet({ stamp, locationName,
                               onBack, onClose, onToggleWishlist,
                               onToggleObtained, onVote}: Props) {
    if (!stamp) return null;

    const showWarning = stamp.thumbsDown > stamp.thumbsUp && stamp.thumbsDown >= 5;

    return (
        <ScrollView contentContainerStyle={styles.content}>
            <View style={styles.topRow}>
                <Pressable onPress={onBack}
                           style={styles.iconButton}>
                    <MaterialCommunityIcons
                        name="chevron-left"
                        size={24}
                        color="black" />
                </Pressable>
                <Pressable style={styles.iconButton} onPress={onClose}>
                    <MaterialCommunityIcons
                        name="close-thick"
                        size={15}
                        color="black" />
                </Pressable>
            </View>

            <View style={styles.stampWrapper}>
                <View style={styles.stampImageContainer}>
                    <Image source={{uri: stamp.image}}
                       style={styles.stampIcon} />
                </View>
                <Text style={styles.locationLabel}>{locationName}</Text>
                <Text style={styles.stampName}>{stamp.name}</Text>
                <View style={styles.badgeRow}>
                    <AvailabilityBadge available={stamp.available} />
                </View>
            </View>

            {stamp.obtained && stamp.dateObtained && (
                <Text style={styles.obtainedNote}>
                    Collected {new Date(stamp.dateObtained).toLocaleDateString(undefined, { month: 'long', day: 'numeric', year: 'numeric' })}
                </Text>
            )}

            {showWarning && (
                <View style={styles.warnBox}>
                    <Text style={styles.warnText}>
                        Recent reports suggest this stamp may no longer be available at this location. Check on arrival.
                    </Text>
                </View>
            )}

            <View style={styles.actions}>
                <Pressable
                    style={[styles.actionButton, stamp.wishlisted && styles.actionWishActive]}
                    onPress={onToggleWishlist}
                >
                    <MaterialCommunityIcons
                        name={stamp.wishlisted ? "bookmark": "bookmark-outline"}
                        size={18}
                        color={stamp.wishlisted ? colours.primary.default2 : colours.primary.default}
                    />
                    <Text style={[
                        styles.actionLabel,
                        stamp.wishlisted && styles.actionLabelWishlisted]}>
                        {stamp.wishlisted ? 'Wishlisted' : 'Wishlist'}
                    </Text>
                </Pressable>

                <Pressable
                    style={[styles.actionButton,
                        stamp.obtained && styles.actionObtainedActive]}
                    onPress={onToggleObtained}
                >
                    <MaterialCommunityIcons
                        name={stamp.obtained ? "check-circle" : "check-circle-outline"}
                        size={18}
                        color={stamp.obtained ? colours.primary.default2 : colours.primary.default} />
                    <Text style={[
                        styles.actionLabel,
                        stamp.obtained && styles.actionLabelActive]}>
                        {stamp.obtained ? 'Obtained' : 'Mark obtained'}
                    </Text>
                </Pressable>
            </View>

            <Text style={styles.voteLabel}>Is this stamp still here?</Text>
            <View style={styles.votes}>
                <Pressable
                    style={[
                        styles.voteBtn,
                        stamp.userVote === 'up' && styles.voteBtnUpActive]}
                    onPress={() => onVote('up')}
                >
                    <MaterialCommunityIcons
                        name={stamp.userVote === 'up' ? "thumb-up" : "thumb-up-outline"}
                        size={17}
                        color={stamp.userVote === 'up' ? colours.secondary.dark2 : colours.primary.light}
                    />
                    <Text style={[
                        styles.voteCount,
                        stamp.userVote === 'up' && { color: colours.secondary.dark2 }]}>
                        {stamp.thumbsUp}
                    </Text>
                </Pressable>

                <Pressable
                    style={[
                        styles.voteBtn,
                        stamp.userVote === 'down' && styles.voteBtnDownActive]}
                    onPress={() => onVote('down')}
                >
                    <MaterialCommunityIcons
                        name={stamp.userVote === 'down' ? "thumb-down" : "thumb-down-outline"}
                        size={17}
                        color={stamp.userVote === 'down' ? colours.warnings.error : colours.primary.light} />
                    <Text style={[
                        styles.voteCount,
                        stamp.userVote === 'down' && { color: colours.warnings.error }]}>
                        {stamp.thumbsDown}
                    </Text>
                </Pressable>
            </View>
        </ScrollView>
    );
}

const styles = StyleSheet.create({
    content: {
        paddingHorizontal: 22,
        paddingBottom: 30,
        paddingTop:20,
    },
    topRow: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        marginTop: 4,
        marginBottom: 6
    },
    iconButton: {
        width: 32,
        height: 32,
        borderRadius: 16,
        borderWidth: 1,
        alignItems: 'center',
        justifyContent: 'center',
        borderColor: colours.border,
    },
    stampWrapper: {
        alignItems: 'center',
        marginVertical: 10,
    },
    stampImageContainer: {
        width: 100,
        height: 100,
        borderRadius: 50,
        borderWidth: 1,
        overflow: 'hidden',
    },
    stampIcon:{
        width: '100%',
        height: '100%'
    },
    locationLabel: {
        textAlign: 'center',
        fontSize: 13,
        color: colours.primary.default,
        marginBottom: 2
    },
    stampName: {
        textAlign: 'center',
        fontSize: 26,
        color: colours.primary.default,
        textTransform: 'uppercase',
        marginBottom: 8
    },
    badgeRow: {
        alignItems: 'center',
        marginBottom: 6
    },
    obtainedNote: {
        textAlign: 'center',
        fontSize: 15,
        color: colours.secondary.dark,
        marginBottom: 10
    },

    warnBox: {
        backgroundColor: '#B23B2E14',
        borderColor: '#B23B2E33',
        borderWidth: 1,
        borderRadius: 10,
        padding: 10,
        marginBottom: 16,
    },
    warnText: {
        fontSize: 12,
        color: colours.warnings.error,
        lineHeight: 17
    },

    actions: {
        flexDirection: 'row',
        gap: 10,
        marginBottom: 20
    },
    actionButton: {
        flex: 1,
        alignItems: 'center',
        gap: 6,
        paddingVertical: 12,
        borderRadius: 14,
        borderWidth: 1.5,
        borderColor: colours.border,
        backgroundColor: colours.primary.background,
    },
    actionWishActive: {
        backgroundColor: colours.primary.lighter,
        borderColor: colours.primary.default
    },
    actionObtainedActive: {
        backgroundColor: colours.secondary.light,
        borderColor: colours.secondary.dark2
    },
    actionLabel: {
        fontSize: 14,
        color: colours.primary.default,
        textTransform: 'uppercase',
        fontWeight: "bold"
    },
    actionLabelActive: {
        color: colours.primary.default2
    },
    actionLabelWishlisted: {
        color: colours.primary.default
    },

    voteLabel: {
        textAlign: 'center',
        fontSize: 14,
        color: colours.primary.dark,
        textTransform: 'uppercase',
        marginBottom: 8,
        fontWeight: "bold",
    },
    votes: {
        flexDirection: 'row',
        gap: 10
    },
    voteBtn: {
        flex: 1,
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'center',
        gap: 8,
        paddingVertical: 11,
        borderRadius: 12,
        borderWidth: 1.5,
        borderColor: colours.border,
        backgroundColor: colours.primary.background,
    },
    voteBtnUpActive: {
        borderColor: colours.secondary.default,
        backgroundColor: colours.secondary.light
    },
    voteBtnDownActive: {
        borderColor: colours.warnings.error,
        backgroundColor: '#B23B2E1a'
    },
    voteCount: {
        fontSize: 15,
        fontWeight: 'bold',
        color: colours.primary.default
    },
});