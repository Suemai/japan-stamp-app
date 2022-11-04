package com.test.stampmap.Filter;

import com.test.stampmap.Activity.MainActivity;
import com.test.stampmap.Dialogues.FilterSheetDialogue;
import android.content.Context;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.test.stampmap.Interface.IFilter;

import java.util.List;
import java.util.stream.Collectors;

public class FilterAlertBox extends AlertDialog.Builder{
    public FilterAlertBox(Context context, TextView textView, FilterType type) {
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
        setPositiveButton("OK", (dialogInterface, l) -> FilterSheetDialogue.setTexts(textView, type));
        setNeutralButton("Clear Filter", (dialogInterface, i) -> {
            MainActivity.filters.removeAll(MainActivity.filters.stream().filter(item -> item.filterType() == type.ordinal()).collect(Collectors.toList()));
            FilterSheetDialogue.setTexts(textView, type);
        });
        show();
    }
}
