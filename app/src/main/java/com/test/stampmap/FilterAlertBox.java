package com.test.stampmap;

import android.content.Context;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;

import java.util.List;
import java.util.stream.Collectors;

public class FilterAlertBox extends AlertDialog.Builder{
    public FilterAlertBox(Context context, FilterType type) {
        super(context);
        setTitle("Select " + type.name);
        setCancelable(false);
        boolean[] selectedPrefectures = new boolean[context.getResources().getStringArray(type.itemsId).length];
        List<IFilter> activeFilters = MainActivity.filters.stream().filter(item -> item.filterType() == type.ordinal()).collect(Collectors.toList());
        for (IFilter filter : activeFilters) selectedPrefectures[((Enum<?>)filter).ordinal()] = true;

        setMultiChoiceItems(type.itemsId, selectedPrefectures, (dialogInterface, index, selected) -> {
            if (selected) MainActivity.filters.add(type.values[index]);
            else MainActivity.filters.remove(type.values[index]);
        });
        setPositiveButton("OK", (dialogInterface, i) -> {});
        setNeutralButton("Clear Filter", (dialogInterface, i) -> MainActivity.filters.removeAll(MainActivity.filters.stream().filter(item -> item.filterType() == type.ordinal()).collect(Collectors.toList())));
        show();
    }
}
