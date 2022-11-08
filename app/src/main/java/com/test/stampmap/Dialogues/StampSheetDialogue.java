package com.test.stampmap.Dialogues;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.divider.MaterialDivider;
import com.test.stampmap.R;
import com.test.stampmap.Stamp.Stamp;
import com.test.stampmap.Stamp.StampSet;

import java.util.ArrayList;
import java.util.HashMap;

public class StampSheetDialogue extends BottomSheetDialogFragment {

    private final StampSet stampSet;

    public StampSheetDialogue(final StampSet stampSet){
        this.stampSet = stampSet;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance) {
        View v = layoutInflater.inflate(R.layout.stamp_info_drawer, container, false);
        TextView name = v.findViewById(R.id.stampName_drawer);
        TextView diff = v.findViewById(R.id.difficulty_drawer);
        TextView open = v.findViewById(R.id.hours_drawer);
        TextView holiday = v.findViewById(R.id.holiday_drawer);
        TextView fee = v.findViewById(R.id.fee_drawer);
        name.setText(stampSet.getName());
        diff.setText("Difficulty: " + stampSet.getDifficulty());
        open.setText("Open Hours: " + stampSet.getOpenHours());
        holiday.setText("Holiday: " + stampSet.getHoliday());
        fee.setText("Entry Fee: " + stampSet.getEntryFee());

        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        int i = 1;
        for (Stamp stamp : stampSet){
            HashMap<String, String> map = new HashMap<>();
            map.put("num", "Stamp " + i++);
            map.put("obtain", "Obtainable: " + (stamp.getIsObtainable() ? "Yes" : "No"));
            map.put("location", "Location: " + stamp.getLocation());
            list.add(map);
        }
        ((ListView)v.findViewById(R.id.stamp_list)).setAdapter(
                new SimpleAdapter(requireContext(), list, R.layout.stamp_element,
                    new String[]{"num", "obtain", "location"},
                    new int[]{R.id.stampNo_drawer, R.id.obtainable_drawer, R.id.location_drawer}));
        return v;
    }
}
