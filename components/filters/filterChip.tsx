import React from 'react';
import { Pressable, Text, View, StyleSheet } from 'react-native';
import { colours } from '@/constants/colours';

interface Props {
    label: string;
    active: boolean;
    onPress: () => void;
}

export function FilterChip({ label, active, onPress}: Props) {
    return (
        <Pressable
            onPress={onPress}
            style={[styles.chip, active && styles.chipActive]}>
            <Text
                style={[
                    styles.label,
                    active && styles.labelActive]}>
                {label}
            </Text>
        </Pressable>
    )
}

const styles = StyleSheet.create({
    chip: {
        flexDirection: 'row',
        alignItems: 'center',
        gap: 8,
        paddingHorizontal: 14,
        paddingVertical: 9,
        borderRadius: 100,
        borderWidth: 1,
        borderColor: colours.border,
        backgroundColor: colours.primary.background,
        shadowColor: '#000',
        shadowOffset: {
            width: 0,
            height: 2
        },
        shadowOpacity: 0.18,
        shadowRadius: 6,
        elevation: 5,
    },
    chipActive: {
        backgroundColor: colours.primary.light,
    },
    label: {
        fontSize: 13,
        letterSpacing: 0.3,
        fontWeight: 'bold',
        color: colours.text.primary,
    },
    labelActive: {
        color: colours.primary.background,
    },
});

