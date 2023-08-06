package com.test.stampmap.Dialogues;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.test.stampmap.Adapter.StampRecyclerAdapter;
import com.test.stampmap.R;
import com.test.stampmap.Stamp.Stamp;
import com.test.stampmap.Stamp.StampCollection;
import com.test.stampmap.Stamp.StampSet;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class AddStampSetDialogue extends BottomSheetDialogFragment {

    private static GeoPoint coordinates;
    private RecyclerView stampListView;
    protected static StampRecyclerAdapter adapter;
    private static final List<List<String>> data = new ArrayList<>();

    public AddStampSetDialogue(){}
    public AddStampSetDialogue(GeoPoint coordinates){
        AddStampSetDialogue.coordinates = coordinates;
        AddStampSetDialogue.data.clear();
    }


    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance){
        View v = layoutInflater.inflate(R.layout.add_stamp_drawer, container,false);

        EditText newStampName = v.findViewById(R.id.newStampName);
        EditText newStampLocation = v.findViewById(R.id.newStampLocation);
        EditText newStampHours = v.findViewById(R.id.newStampHours);
        EditText newStampHoliday = v.findViewById(R.id.newStampHoliday);
        EditText newStampFee = v.findViewById(R.id.newStampFees);
        RadioGroup obtainable = v.findViewById(R.id.newObtainable);
        Button addStampButton = v.findViewById(R.id.uploadedButton);
        Button createStampSet = v.findViewById(R.id.add_new_stamp);
        stampListView = v.findViewById(R.id.stampListRecView);

        addStampButton.setOnClickListener(v1 -> new AddStampDialogue(1).show(getChildFragmentManager(), "ModalSheet"));
        createStampSet.setOnClickListener(v12 -> {
            if (newStampName.getText().toString().equals("")) makeToast("no name");
            else if (newStampLocation.getText().toString().equals("")) makeToast("no location");
            else if (newStampHours.getText().toString().equals("")) makeToast("no hours");
            else if (newStampHoliday.getText().toString().equals("")) makeToast("no holiday");
            else if (newStampFee.getText().toString().equals("")) makeToast("no fee");
            else if (obtainable.getCheckedRadioButtonId() == -1) makeToast("no obtainability");
            else if (data.isEmpty()) makeToast("no stamps added");
            else {
                List <Stamp> newStampList = new ArrayList<>();
                RadioButton selectedButton = obtainable.findViewById(obtainable.getCheckedRadioButtonId());
                boolean isObtainable = selectedButton.getText().toString().equals("YES");
                for (List<String> info : data) {
                    newStampList.add(new Stamp(info.get(0), "不明", newStampLocation.getText().toString(), info.get(1), isObtainable, AddStampSetDialogue.coordinates, true, false, false, 0, ""));
                }
                StampSet newStampSet = new StampSet(newStampName.getText().toString(), "不明", "ベリベリレア", newStampHours.getText().toString(), newStampHoliday.getText().toString(), newStampFee.getText().toString(), newStampList);
                StampCollection.getInstance().addCustomStampSet(newStampSet);
                dismiss();
            }
        });
        addStampsRecyclerView();
        return v;
    }

    private void addStampsRecyclerView(){

        adapter = new StampRecyclerAdapter(data, R.layout.stamp_card, (holder, position) -> {

            // get ur views from the thingy innit
            TextView stampName = holder.itemView.findViewById(R.id.stamp_name);
            ImageView stampImage = holder.itemView.findViewById(R.id.stamp_image);
            CardView card = holder.itemView.findViewById(R.id.obtained_parent);

            // do the fancies to your stuff
            stampName.setText(data.get(position).get(0));
            StampCollection.loadImage(data.get(position).get(1), stampImage);
            card.setOnClickListener(view -> {
                final int position1 = holder.getAdapterPosition();
                Toast.makeText(requireContext(), data.get(position1).get(0) + " selected boi", Toast.LENGTH_SHORT).show();
            });
        });
        stampListView.setAdapter(adapter);
        stampListView.setLayoutManager(new GridLayoutManager(this.getContext(),3));
    }

    void makeToast(String text){
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume(){
        super.onResume();
        if (adapter != null) adapter.notifyDataSetChanged();
    }
}
