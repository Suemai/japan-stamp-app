package com.test.stampmap.Dialogues;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.test.stampmap.R;

public class AddStampDialogue extends BottomSheetDialogFragment {

    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance){
        View v = layoutInflater.inflate(R.layout.add_stamp_drawer, container,false);

        EditText newStampName = v.findViewById(R.id.newStampName);
        EditText newStampLocation = v.findViewById(R.id.newStampLocation);
        EditText newStampHours = v.findViewById(R.id.newStampHours);
        EditText newStampHoliday = v.findViewById(R.id.newStampHoliday);
        EditText newStampFee = v.findViewById(R.id.newStampFees);
        RadioGroup obtainable = v.findViewById(R.id.newObtainable);


        return v;
    }
}
