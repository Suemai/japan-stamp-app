package Dialogues;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.test.stampmap.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BottomSheetDialogue extends BottomSheetDialogFragment {

    @Override
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance){
        View v = layoutInflater.inflate(R.layout.filter_drawer, container,false);

        TextView prefectureFilter = v.findViewById(R.id.prefecture_filter);
        TextView difficultyFilter = v.findViewById(R.id.difficulty_filter);
        TextView entryfeeFilter = v.findViewById(R.id.entryfee_filter);

        setTexts(prefectureFilter, FilterType.PREFECTURE);
        setTexts(difficultyFilter, FilterType.DIFFICULTY);
        setTexts(entryfeeFilter, FilterType.ENTRYFEE);

        prefectureFilter.setOnClickListener(view -> new FilterAlertBox(requireContext(), prefectureFilter, FilterType.PREFECTURE));
        difficultyFilter.setOnClickListener(view -> new FilterAlertBox(requireContext(), difficultyFilter, FilterType.DIFFICULTY));
        entryfeeFilter.setOnClickListener(view -> new FilterAlertBox(requireContext(), entryfeeFilter, FilterType.ENTRYFEE));

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


