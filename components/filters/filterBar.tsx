import React, {useState} from "react";
import {View, StyleSheet, ScrollView} from "react-native";
import {FilterChip} from "@/components/filters/filterChip";
import {SplitFilterChip} from "@/components/filters/splitFilterChip";

export function FilterBar() {
    // This bit below can go, it's just to make the component work for now,
    // until we implement the filter state properly
    const [selectedFilter, setSelectedFilter] = useState<
        "obtained" | "unobtained" | "wishlist" | "fee" | "free" | "openNow" | null
    >(null);

    return (
        <View style={styles.wrap}>
            <ScrollView horizontal
                        showsHorizontalScrollIndicator={false}
                        contentContainerStyle={styles.row}>

                <SplitFilterChip
                    leftLabel="Obtained"
                    leftValue="obtained"
                    rightLabel="Unobtained"
                    rightValue="unobtained"
                    value={selectedFilter === "obtained" ||
                    selectedFilter === "unobtained"
                        ? selectedFilter
                        : null}
                    onChange={(value) => {
                        setSelectedFilter(value)}}
                />

                <FilterChip
                    label="Wishlist"
                    // dotColor={colors.amber}
                    active={selectedFilter === "wishlist"}
                    onPress={() => {
                        setSelectedFilter(
                            selectedFilter === "wishlist"
                                ? null
                                : "wishlist"
                        );
                    }}
                />

                <SplitFilterChip
                    leftLabel="Fee"
                    leftValue="fee"
                    rightLabel="Free"
                    rightValue="free"
                    value={selectedFilter === "fee" ||
                    selectedFilter === "free"
                        ? selectedFilter
                        : null}
                    onChange={(value) => {
                        setSelectedFilter(value)}}
                />

                <FilterChip
                    label="Open now"
                    active={selectedFilter === "openNow"}
                    onPress={() => {
                        setSelectedFilter(
                            selectedFilter === "openNow"
                                ? null
                                : "openNow"
                        );
                    }}
                />
            </ScrollView>
        </View>
    )
}

const styles = StyleSheet.create({
    wrap: {
        gap: 10
    },
    row: {
        flexDirection: 'row',
        gap: 8,
        paddingHorizontal: 16,
        paddingVertical: 5
    },
});