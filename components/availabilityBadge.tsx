import React from 'react';
import { View, Text, StyleSheet } from 'react-native';

export function AvailabilityBadge({ available }: { available: boolean }) {
    return (
        <View style={[styles.badge, {
            backgroundColor: available ? '#4B6B4F22' : '#8A7F6822'
        }]}>
            <Text style={[styles.text, {color: available ? '#4B6B4F' : '#8A7F68' }]}>
                {available ? 'Available' : 'Unavailable'}
            </Text>
        </View>
    );
}

const styles = StyleSheet.create({
    badge: {
        alignSelf: 'flex-start',
        paddingHorizontal: 8,
        paddingVertical: 2,
        borderRadius: 100,
        marginTop: 4,
    },
    text: {
        fontSize: 14,
        letterSpacing: 0.5,
        fontWeight: 'bold',
    },
});