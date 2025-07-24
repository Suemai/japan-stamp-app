package com.test.stampmap.Fragments.MyStampsChild;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.google.android.material.textfield.TextInputEditText;
import com.test.stampmap.R;
import com.test.stampmap.Settings.ConfigValue;
import com.test.stampmap.Settings.SupportedLocale;
import com.test.stampmap.Stamp.Stamp;
import com.test.stampmap.Stamp.StampCollection;
import com.test.stampmap.Stamp.StampSet;

import java.text.DateFormat;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.Date;

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
        int[] hashes = requireArguments().getIntArray("stamp");
        StampSet parentStampSet = StampCollection.getInstance().getStampSetByHash(hashes[0]);
        Stamp stamp = parentStampSet.getStamps().stream().filter(s -> s.hashCode() == hashes[1]).findFirst().orElse(null);

        //Stamp information
        stampName = view.findViewById(R.id.stamp_name_for_card);
        stampName.setText(stamp.getName());

        address = view.findViewById(R.id.addressTxt);
        address.setText(stamp.getAddress());

        location = view.findViewById(R.id.locationTxt);
        location.setText(stamp.getLocation());

        // TODO cus I dumb お願いね
        opening = view.findViewById(R.id.openingTxt);
        opening.setText(parentStampSet.getOpenHours());

        holiday = view.findViewById(R.id.holidayTxt);
        holiday.setText(parentStampSet.getHoliday());

        fees = view.findViewById(R.id.prices);
        fees.setText(parentStampSet.getEntryFee());

        // Images
        availability = view.findViewById(R.id.not_available_img);
        if (stamp.getIsObtainable()) availability.setImageResource(R.drawable.available_check_foreground);


        obtained = view.findViewById(R.id.obtained_img);
        obtained_btn = view.findViewById(R.id.obtained_btn);
        //default is not obtained
        if (stamp.getIsObtained()) obtained.setImageResource(R.drawable.checkbox_obtained_foreground);


        obtained_btn.setOnClickListener(view13 -> {
            StampCollection.getInstance().setStampObtained(stamp, parentStampSet, !stamp.getIsObtained());
            if (!stamp.getIsObtained()) obtained.setImageResource(R.drawable.checkbox_foreground);
            else obtained.setImageResource(R.drawable.checkbox_obtained_foreground);
        });

        wishlist = view.findViewById(R.id.wishlist_img);
        wishlist_btn = view.findViewById(R.id.wishlist_btn);
        //default is not in wishlist
        if (stamp.getIsOnWishlist()){
            wishlist.setImageResource(R.drawable.in_wishlist_star);
            // do some stuff
        }

        wishlist_btn.setOnClickListener(view14 -> {
            StampCollection.getInstance().setStampOnWishlist(stamp, parentStampSet, !stamp.getIsOnWishlist());
            if (!stamp.getIsOnWishlist()) {
                wishlist.setImageResource(R.drawable.wishlist_star_outline);
                // do some stuff
            }else{
                wishlist.setImageResource(R.drawable.in_wishlist_star);
                // do more stuff
            }
        });

        // Stamp image
        image = view.findViewById(R.id.stamp_image_for_card);
        StampCollection.loadImage(view, stamp, image);

        //  Calendar/date stuff
        staticDate = view.findViewById(R.id.static_date);
        if (!stamp.getIsObtained()) staticDate.setText("unobtained");
        else {
//            Date date = Date.from(Instant.ofEpochMilli(stamp.getDateObtained()));
//            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, requireContext().getResources().getConfiguration().locale);
            staticDate.setText(getDateStringForLocale(stamp.getDateObtained()));
        }
        calendar = view.findViewById(R.id.calendar_btn);
        calendar.setOnClickListener(view1 -> datePicker(stamp));

        // TODO i hate this textbox stuff will have to have another mess around with it
        // notes stuff
        notes = view.findViewById(R.id.extra_notes);
        notes.setText(stamp.getNotes());
        notes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                StampCollection.getInstance().setStampNotes(stamp, parentStampSet, s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        return view;
    }

    private void datePicker(Stamp stamp) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(
                requireContext(),  // Pass the context of the fragment
                android.R.style.Widget_Holo_CalendarView,
                (view, year1, month1, day1) -> {
                    // Handle the selected date here
//                    String selectedDate = day1 + "/" + (month1 + 1) + "/" + year1;
                    LocalDate selectedDate = LocalDate.of(year1, month1 + 1, day1);
                    StampCollection.getInstance().setStampDateObtained(stamp, selectedDate);
                    staticDate.setText(getDateStringForLocale(selectedDate));
                },
                year, month, day
        );
        datePicker.show();
        datePicker.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private String getDateStringForLocale(long epochDay) {
        try {
            return getDateStringForLocale(LocalDate.ofEpochDay(epochDay));
        }
        catch (DateTimeException e) { // this isn't really necessary but saves you having to force wipe all stamp data
            Date date = Date.from(Instant.ofEpochMilli(epochDay));
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, requireContext().getResources().getConfiguration().locale);
            return df.format(date);
        }
    }

    private String getDateStringForLocale(LocalDate date) {
        DateTimeFormatter format = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(requireContext().getResources().getConfiguration().locale);
        return date.format(format);
    }
}