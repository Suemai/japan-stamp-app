package Dialogues;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.test.stampmap.*;

public class BottomSheetDialogue extends BottomSheetDialogFragment {

    @Override
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance){
        View v = layoutInflater.inflate(R.layout.filter_drawer, container,false);

        TextView prefectureFilter = v.findViewById(R.id.prefecture_filter);
        TextView difficultyFilter = v.findViewById(R.id.difficulty_filter);
        TextView entryfeeFilter = v.findViewById(R.id.entryfee_filter);

        prefectureFilter.setOnClickListener(view -> new FilterAlertBox(requireContext(), FilterType.PREFECTURE));
        difficultyFilter.setOnClickListener(view -> new FilterAlertBox(requireContext(), FilterType.DIFFICULTY));
        entryfeeFilter.setOnClickListener(view -> new FilterAlertBox(requireContext(), FilterType.ENTRYFEE));

        return v;
    }
}


