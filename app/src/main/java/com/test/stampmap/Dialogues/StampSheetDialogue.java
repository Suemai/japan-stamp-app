package com.test.stampmap.Dialogues;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.divider.MaterialDivider;
import com.test.stampmap.R;
import com.test.stampmap.Stamp.Stamp;
import com.test.stampmap.Stamp.StampSet;

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

        LinearLayout stampList = v.findViewById(R.id.stamp_list);
        int i = 1;
        for (Stamp stamp : stampSet) {
            View element = layoutInflater.inflate(R.layout.stamp_element, null, false);
            ((TextView)element.findViewById(R.id.stampNo_drawer)).setText("Stamp: " + i++);
            ((TextView)element.findViewById(R.id.obtainable_drawer)).setText("Obtainable: " + (stamp.getIsObtainable() ? "Yes" : "No"));
            ((TextView)element.findViewById(R.id.location_drawer)).setText("Location: " + stamp.getLocation());
            stampList.addView(element);
        }
        return v;
    }
}
