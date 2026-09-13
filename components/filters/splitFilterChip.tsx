import React from 'react';
import { Pressable, Text, View, StyleSheet } from 'react-native';
import {colours} from "@/constants/colours";

interface Props<T extends string> {
    leftLabel: string;
    leftValue: T;
    rightLabel: string;
    rightValue: T;
    value: T | null;
    onChange: (value: T | null) => void;
}

export function SplitFilterChip<T extends string>({
                                                leftLabel, leftValue,
                                                rightLabel, rightValue,
                                                value, onChange,
                                            }: Props<T>) {
    function handlePress(pressed: T) {
        onChange(value === pressed ? null : pressed);
    }

    return (
        <View style={styles.chip}>
            <Pressable
                onPress={() => handlePress(leftValue)}
                style={[
                    styles.half,
                    value === leftValue && styles.halfActive]}
            >
                <Text style={[
                    styles.label,
                    value === leftValue && styles.labelActive
                ]}>
                    {leftLabel}
                </Text>

            </Pressable>
            <View style={styles.divider} />
            <Pressable
                onPress={() => handlePress(rightValue)}
                style={[
                    styles.half,
                    value === rightValue && styles.halfActive
                ]}
            >
                <Text style={[
                    styles.label,
                    value === rightValue && styles.labelActive]}>
                    {rightLabel}
                </Text>
            </Pressable>
        </View>
    );
}

const styles = StyleSheet.create({
    chip: {
        flexDirection: 'row',
        borderRadius: 100,
        overflow: 'hidden',
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
    half: {
        paddingHorizontal: 13,
        paddingVertical: 8
    },
    halfActive: {
        backgroundColor: colours.primary.light
    },
    divider: {
        width: 2,
        backgroundColor: colours.text.secondary
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