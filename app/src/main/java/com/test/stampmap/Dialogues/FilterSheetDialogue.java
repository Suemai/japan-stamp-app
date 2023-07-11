package com.test.stampmap.Dialogues;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.slider.Slider;
import com.test.stampmap.*;
import com.test.stampmap.Activity.MainActivity;
import com.test.stampmap.Filter.FilterAlertBox;
import com.test.stampmap.Filter.FilterType;
import com.test.stampmap.Fragments.ExploreFragment;
import com.test.stampmap.Interface.IFilter;

import java.util.List;
import java.util.stream.Collectors;

public class FilterSheetDialogue extends BottomSheetDialogFragment {

    @Override
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance){
        View v = layoutInflater.inflate(R.layout.filter_drawer, container,false);

        TextView prefectureFilter = v.findViewById(R.id.prefecture_filter);
        TextView difficultyFilter = v.findViewById(R.id.difficulty_filter);
        TextView entryfeeFilter = v.findViewById(R.id.entryfee_filter);
        Slider distanceSlider = v.findViewById(R.id.distance_slider);

        setTexts(prefectureFilter, FilterType.PREFECTURE);
        setTexts(difficultyFilter, FilterType.DIFFICULTY);
        setTexts(entryfeeFilter, FilterType.ENTRYFEE);

        prefectureFilter.setOnClickListener(view -> new FilterAlertBox(requireContext(), prefectureFilter, FilterType.PREFECTURE));
        difficultyFilter.setOnClickListener(view -> new FilterAlertBox(requireContext(), difficultyFilter, FilterType.DIFFICULTY));
        entryfeeFilter.setOnClickListener(view -> new FilterAlertBox(requireContext(), entryfeeFilter, FilterType.ENTRYFEE));

        distanceSlider.setValue(ExploreFragment.distanceSliderValue);
        distanceSlider.addOnChangeListener((slider, value, fromUser) -> ExploreFragment.distanceSliderValue = value);

        Spinner spinner = v.findViewById(R.id.distance_unit);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.disance_units, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(ExploreFragment.isKilometres ? 0 : 1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ExploreFragment.isKilometres = position == 0;
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        v.findViewById(R.id.filter_search).setOnClickListener(v1 -> {
            Fragment host = requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            host.getChildFragmentManager().getFragments().stream().filter(ExploreFragment.class::isInstance).findFirst().ifPresent(fragment -> ((ExploreFragment)fragment).performSearch(null));
        });
        v.findViewById(R.id.clear_filters).setOnClickListener(v1 -> {
            MainActivity.filters.clear();
            setTexts(prefectureFilter, FilterType.PREFECTURE);
            setTexts(difficultyFilter, FilterType.DIFFICULTY);
            setTexts(entryfeeFilter, FilterType.ENTRYFEE);
            ExploreFragment.distanceSliderValue = 0;
            dismiss();
        });
        return v;
    }

    public static void setTexts(TextView textView, FilterType type){
        StringBuilder stringBuilder = new StringBuilder();
        List<IFilter> currentFilters = MainActivity.filters.stream().filter(item -> item.filterType() == type.ordinal()).collect(Collectors.toList());
        if (currentFilters.isEmpty()) {
            stringBuilder.append("Select ").append(type.name);
            textView.setText(stringBuilder.toString());
            return;
        }
        String[] values = textView.getResources().getStringArray(type.itemsId);
        stringBuilder.append(type.name).append(": ");
        for (int i = 0; i < currentFilters.size(); i++) {
            stringBuilder.append(values[((Enum<?>)currentFilters.get(i)).ordinal()]);
            if (i != currentFilters.size() - 1) stringBuilder.append(", ");
        }
        textView.setText(stringBuilder.toString());
    }
}


