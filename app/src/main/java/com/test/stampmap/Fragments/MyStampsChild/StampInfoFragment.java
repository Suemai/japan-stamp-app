package com.test.stampmap.Fragments.MyStampsChild;


import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.google.android.material.textfield.TextInputEditText;
import com.test.stampmap.R;
import com.test.stampmap.Stamp.Stamp;
import com.test.stampmap.Stamp.StampCollection;
import com.test.stampmap.Stamp.StampSet;

import java.util.Calendar;

public class StampInfoFragment extends Fragment {

    private ImageButton calendar, backBtn;
    private TextView staticDate, stampName, address, location, opening, holiday, fees;
    private ImageView image, availability, obtained, wishlist;
    private LinearLayout availability_btn, obtained_btn, wishlist_btn;
    private TextInputEditText notes;

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

        // Retrieve the arguments
        Stamp stamp = (Stamp)getArguments().get("stamp");

        //Stamp information
        stampName = view.findViewById(R.id.stamp_name_for_card);
        stampName.setText(stamp.getName());

        address = view.findViewById(R.id.addressTxt);
        address.setText(stamp.getAddress());

        location = view.findViewById(R.id.locationTxt);
        location.setText(stamp.getLocation());

        // TODO cus I dumb お願いね
        opening = view.findViewById(R.id.openingTxt);

        holiday = view.findViewById(R.id.holidayTxt);

        fees = view.findViewById(R.id.prices);

        // Images
        availability = view.findViewById(R.id.not_available_img);
        availability_btn = view.findViewById(R.id.availability_btn);
        availability_btn.setOnClickListener(view12 -> {
            if(stamp.getIsObtainable()){
                availability.setImageResource(R.drawable.available_check_foreground);
            }
        });

        obtained = view.findViewById(R.id.obtained_img);
        obtained_btn = view.findViewById(R.id.obtained_btn);
        obtained_btn.setOnClickListener(view13 -> {
            if (stamp.getIsObtained()){
                obtained.setImageResource(R.drawable.checkbox_obtained_foreground);
            }
        });

        wishlist = view.findViewById(R.id.wishlist_img);
        wishlist_btn = view.findViewById(R.id.wishlist_btn);
        wishlist_btn.setOnClickListener(view14 -> {
            if (stamp.getIsOnWishlist()){
                wishlist.setImageResource(R.drawable.in_wishlist_star);
            }
        });

        // Stamp image
        image = view.findViewById(R.id.stamp_image_for_card);
        StampCollection.loadImage(view, stamp, image);

        //  Calendar/date stuff
        staticDate = view.findViewById(R.id.static_date);
        calendar = view.findViewById(R.id.calendar_btn);
        calendar.setOnClickListener(view1 -> datePicker());

        // TODO cus I dumb お願いね
        // notes stuff
        notes = view.findViewById(R.id.extra_notes);

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
                    String selectedDate = day1  + "/" + (month1 + 1) + "/" + year1;
                    staticDate.setText(selectedDate);
                },
                year, month, day
        );
        datePicker.show();
        datePicker.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}