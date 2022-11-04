package com.test.stampmap.Filter;

import androidx.annotation.ArrayRes;
import com.test.stampmap.Filter.Filters;
import com.test.stampmap.Interface.IFilter;
import com.test.stampmap.R;

public enum FilterType {
    DIFFICULTY(R.array.difficulties, Filters.Difficulty.values(), "Difficulty"),
    PREFECTURE(R.array.prefectures, Filters.Prefecture.values(), "Prefecture"),
    SEARCH(R.array.searchtype, Filters.SearchType.values(), "Search Type"),
    ENTRYFEE(R.array.entryfee, Filters.EntryFee.values(), "Entry Fee"),
    OPENHOURS(R.array.prefectures, Filters.Prefecture.values(), "Open Hours");

    @ArrayRes public final int itemsId;
    public final IFilter[] values;
    public final String name;

    FilterType(@ArrayRes int itemsId, IFilter[] values, String name){
        this.itemsId = itemsId;
        this.values = values;
        this.name = name;
    }
}
