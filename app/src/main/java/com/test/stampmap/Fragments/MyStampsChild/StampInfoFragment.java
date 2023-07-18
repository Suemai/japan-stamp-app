package com.test.stampmap.Fragments.MyStampsChild;


import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.test.stampmap.R;

import java.util.Calendar;

public class StampInfoFragment extends Fragment {

    private ImageButton calendar, backBtn;
    private TextView staticDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stamp_info_card, container, false);

        // back button
        backBtn = view.findViewById(R.id.stamp_back_button);
        backBtn.setOnClickListener(view1 -> Navigation.findNavController(view).popBackStack());

        //  Calendar stuff
        staticDate = view.findViewById(R.id.static_date);
        calendar = view.findViewById(R.id.calendar_btn);
        calendar.setOnClickListener(view1 -> datePicker());

        return view;
    }

    private void datePicker() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(
                requireContext(),  // Pass the context of the fragment
                android.R.style.Widget_Holo_CalendarView,
                (view, year1, month1, day1) -> {
                    // Handle the selected date here
                    String selectedDate = (month1 + 1) + "/" + day1 + "/" + year1;
                    staticDate.setText(selectedDate);
                },
                year, month, day
        );
        datePicker.show();
        datePicker.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}