package com.test.stampmap;

import androidx.annotation.ArrayRes;

public enum FilterType {
    DIFFICULTY(R.array.difficulties, Filters.Difficulty.values(), "Difficulty"),
    PREFECTURE(R.array.prefectures, Filters.Prefecture.values(), "Prefecture"),
    SEARCH(R.array.searchtype, Filters.SearchType.values(), "Search Type"),
    ENTRYFEE(R.array.entryfee, Filters.EntryFee.values(), "Entry Fee"),
    OPENHOURS(R.array.prefectures, Filters.Prefecture.values(), "Open Hours");

    @ArrayRes final int itemsId;
    final IFilter[] values;
    public final String name;

    FilterType(@ArrayRes int itemsId, IFilter[] values, String name){
        this.itemsId = itemsId;
        this.values = values;
        this.name = name;
    }
}
