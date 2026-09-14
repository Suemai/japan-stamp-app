import { AvailabilityBadge } from "@/components/homeDetails/availabilityBadge";
import { colours } from "@/constants/colours";
import { fetchUserStampInfo, StampRow as StampApiRow, updateUserStampInfo } from '@/lib/stampApi';
import { supabase } from '@/lib/supabase';
import { getImageSource } from '@/utils/imageSource';
import React, { useEffect, useState } from "react";
import { Image, Pressable, ScrollView, StyleSheet, Text, View } from 'react-native';
import MaterialCommunityIcons from "react-native-vector-icons/MaterialCommunityIcons";

interface Props {
    stamp: StampApiRow | undefined;
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
    const [wishlisted, setWishlisted] = useState(false);
    const [obtained, setObtained] = useState(false);
    const [userVote, setUserVote] = useState<'up' | 'down' | null>(null);

    useEffect(() => {
        let active = true;

        const loadUserStampInfo = async () => {
            setWishlisted(false);
            setObtained(false);
            setUserVote(null);

            if (!stamp) return;

            try {
                const { data: { session } } = await supabase.auth.getSession();
                const userId = session?.user?.id;
                if (!userId) return;

                const userInfo = await fetchUserStampInfo(stamp.id, userId);
                if (!active || !userInfo) return;

                setWishlisted(userInfo.wishlisted);
                setObtained(userInfo.obtained);
                setUserVote(userInfo.vote);
            } catch (error) {
                console.error('Failed to fetch user stamp info:', error);
            }
        };

        void loadUserStampInfo();

        return () => {
            active = false;
        };
    }, [stamp]);

    if (!stamp) return null;

    const showWarning = stamp.thumbs_down > stamp.thumbs_up && stamp.thumbs_down >= 5;

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
                    <Image
                       source={getImageSource(stamp.image_url)}
                       style={styles.stampIcon} />
                </View>
                <Text style={styles.locationLabel}>{locationName}</Text>
                <Text style={styles.stampName}>{stamp.name}</Text>
                <View style={styles.badgeRow}>
                    <AvailabilityBadge available={stamp.available} />
                </View>
            </View>

            {showWarning && (
                <View style={styles.warnBox}>
                    <Text style={styles.warnText}>
                        Recent reports suggest this stamp may no longer be available at this location. Check on arrival.
                    </Text>
                </View>
            )}

            <View style={styles.actions}>
                <Pressable
                    style={[styles.actionButton, wishlisted && styles.actionWishActive]}
                    onPress={async () => {
                        const nextValue = !wishlisted;
                        setWishlisted(nextValue);
                        try {
                            await updateUserStampInfo(stamp.id, { wishlisted: nextValue });
                            onToggleWishlist();
                        } catch (error) {
                            setWishlisted(!nextValue);
                            console.error('Failed to update wishlist:', error);
                        }
                    }}
                >
                    <MaterialCommunityIcons
                        name={wishlisted ? "bookmark": "bookmark-outline"}
                        size={18}
                        color={wishlisted ? colours.primary.default2 : colours.primary.default}
                    />
                    <Text style={[
                        styles.actionLabel,
                        wishlisted && styles.actionLabelWishlisted]}>
                        {wishlisted ? 'Wishlisted' : 'Wishlist'}
                    </Text>
                </Pressable>

                <Pressable
                    style={[styles.actionButton,
                        obtained && styles.actionObtainedActive]}
                    onPress={async () => {
                        const nextValue = !obtained;
                        setObtained(nextValue);
                        try {
                            await updateUserStampInfo(stamp.id, { obtained: nextValue });
                            onToggleObtained();
                        } catch (error) {
                            setObtained(!nextValue);
                            console.error('Failed to update obtained status:', error);
                        }
                    }}
                >
                    <MaterialCommunityIcons
                        name={obtained ? "check-circle" : "check-circle-outline"}
                        size={18}
                        color={obtained ? colours.primary.default2 : colours.primary.default} />
                    <Text style={[
                        styles.actionLabel,
                        obtained && styles.actionLabelActive]}>
                        {obtained ? 'Obtained' : 'Mark obtained'}
                    </Text>
                </Pressable>
            </View>

            <Text style={styles.voteLabel}>Is this stamp still here?</Text>
            <View style={styles.votes}>
                <Pressable
                    style={[
                        styles.voteBtn,
                        userVote === 'up' && styles.voteBtnUpActive]}
                    onPress={async () => {
                        const previousVote = userVote;
                        setUserVote('up');
                        try {
                            await updateUserStampInfo(stamp.id, { vote: 'up' });
                            onVote('up');
                        } catch (error) {
                            setUserVote(previousVote);
                            console.error('Failed to save upvote:', error);
                        }
                    }}
                >
                    <MaterialCommunityIcons
                        name={userVote === 'up' ? "thumb-up" : "thumb-up-outline"}
                        size={17}
                        color={userVote === 'up' ? colours.secondary.dark2 : colours.primary.light}
                    />
                    <Text style={[
                        styles.voteCount,
                        userVote === 'up' && { color: colours.secondary.dark2 }]}>
                        {stamp.thumbs_up}
                    </Text>
                </Pressable>

                <Pressable
                    style={[
                        styles.voteBtn,
                        userVote === 'down' && styles.voteBtnDownActive]}
                    onPress={async () => {
                        const previousVote = userVote;
                        setUserVote('down');
                        try {
                            await updateUserStampInfo(stamp.id, { vote: 'down' });
                            onVote('down');
                        } catch (error) {
                            setUserVote(previousVote);
                            console.error('Failed to save downvote:', error);
                        }
                    }}
                >
                    <MaterialCommunityIcons
                        name={userVote === 'down' ? "thumb-down" : "thumb-down-outline"}
                        size={17}
                        color={userVote === 'down' ? colours.warnings.error : colours.primary.light} />
                    <Text style={[
                        styles.voteCount,
                        userVote === 'down' && { color: colours.warnings.error }]}>
                        {stamp.thumbs_down}
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