package com.test.stampmap.Dialogues;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.test.stampmap.R;
import com.test.stampmap.Stamp.Stamp;
import com.test.stampmap.Stamp.StampCollection;
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

        ListView stampList = v.findViewById(R.id.stamp_list);
        ArrayList<HashMap<String, String>> dataSet = new ArrayList<>();
        String[] keys = {"num", "obtain", "location", "Owned"};
        int[] ids = {R.id.stampNo_drawer, R.id.obtainable_drawer, R.id.location_drawer, R.id.owned_drawer};
        int i = 1;
        for (Stamp stamp : stampSet){
            HashMap<String, String> map = new HashMap<>();
            map.put(keys[0], "Stamp " + i++);
            map.put(keys[1], "Obtainable: " + (stamp.getIsObtainable() ? "Yes" : "No"));
            map.put(keys[2], "Location: " + stamp.getLocation());
            map.put(keys[3], "Owned: " + (stamp.getIsObtained() ? "Yes" : "No"));
            dataSet.add(map);
        }
        stampList.setOnItemClickListener((parent, view, position, id) -> {
            Stamp stamp = stampSet.getStamps().get(position);
            AlertDialog.Builder popup = new AlertDialog.Builder(requireContext());
            LinearLayout ll = new LinearLayout(requireContext());
            ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            ll.setOrientation(LinearLayout.VERTICAL);
            ll.setGravity(Gravity.CENTER_HORIZONTAL);
            int padding = ((int)getResources().getDisplayMetrics().density)*12;
            ll.setPadding(0, padding, 0, padding);
            TextView title = new TextView(requireContext());
            title.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            title.setText(stamp.getName());
            CheckBox obtained = new CheckBox(requireContext());
            obtained.setText("Owned");
            obtained.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            obtained.setChecked(stamp.getIsObtained());
            obtained.setOnCheckedChangeListener((buttonView, isChecked) -> {
                StampCollection.getInstance().setObtainedStamp(stamp, isChecked);
                StampCollection.getInstance().saveMyStamps(requireContext());
                ((TextView)view.findViewById(R.id.owned_drawer)).setText("Owned: " + (stamp.getIsObtained() ? "Yes" : "No"));
                dataSet.get(position).put(keys[3], "Owned: " + (stamp.getIsObtained() ? "Yes" : "No"));
            });
            ll.addView(title); ll.addView(obtained); popup.setView(ll); popup.show();
        });
        stampList.setAdapter(new SimpleAdapter(requireContext(), dataSet, R.layout.stamp_element, keys, ids));
        return v;
    }
}
