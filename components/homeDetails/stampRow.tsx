import { AvailabilityBadge } from "@/components/homeDetails/availabilityBadge";
import { colours } from "@/constants/colours";
import { StampRow as StampApiRow } from '@/lib/stampApi';
import { getImageSource } from '@/utils/imageSource';
import React from 'react';
import { Image, Pressable, StyleSheet, Text, View } from 'react-native';
import MaterialCommunityIcons from "react-native-vector-icons/MaterialCommunityIcons";

interface Props {
    stamp: StampApiRow;
    onPress: (stampId: string) => void;
}

export function StampRow({ stamp, onPress }: Props) {

    return (
        <Pressable
            onPress={() => onPress(stamp.id)}
            style={({ pressed }) => [
                pressed && styles.pressed
            ]}
        >
            <View style={[styles.row]}>
                <View style={styles.mark}>
                    <Image
                        source={getImageSource(stamp.image_url)}
                        style={styles.markImage} />
                </View>

                <View style={styles.mid}>
                    <Text style={styles.name}>
                        {stamp.name}
                    </Text>
                    <AvailabilityBadge available={stamp.available} />
                </View>

                <MaterialCommunityIcons
                    name="chevron-right"
                    size={30}
                    color={colours.primary.default}
                />

            </View>
        </Pressable>
    );
}

const styles = StyleSheet.create({
    row: {
        flexDirection: 'row',
        alignItems: 'center',
        gap: 12,
        paddingVertical: 13,
        borderBottomWidth: 1,
        borderStyle: 'dashed',
        borderBottomColor: colours.border,
    },
    pressed: { opacity: 0.6 },
    mark: {
        width: 60,
        height: 60,
        borderRadius: 10,
        borderWidth: 1,
        overflow: 'hidden',
    },
    markImage: {
        width: '100%',
        height: '100%'
    },

    mid: { flex: 1 },

    text: {
        fontSize: 18,
    },

    name: {
        fontSize: 16,
        fontWeight: 'bold',
        color: colours.primary.default2 },
});