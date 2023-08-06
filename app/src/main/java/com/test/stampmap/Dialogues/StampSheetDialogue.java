package com.test.stampmap.Dialogues;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.imageview.ShapeableImageView;
import com.test.stampmap.Adapter.StampRecyclerAdapter;
import com.test.stampmap.Fragments.ExploreFragment;
import com.test.stampmap.R;
import com.test.stampmap.Stamp.SerialisableStamp;
import com.test.stampmap.Stamp.Stamp;
import com.test.stampmap.Stamp.StampCollection;
import com.test.stampmap.Stamp.StampSet;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.function.Consumer;

import static com.test.stampmap.Stamp.SerialisableStamp.deserialise;

public class StampSheetDialogue extends BottomSheetDialogFragment {

    private static StampSet stampSet;
    StampRecyclerAdapter adapter;

    public StampSheetDialogue(){}

    public StampSheetDialogue(final StampSet stampSet){
        StampSheetDialogue.stampSet = stampSet;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance) {
        View v = layoutInflater.inflate(R.layout.stamp_info_drawer, container, false);
        TextView name = v.findViewById(R.id.stampName_drawer);
        TextView diff = v.findViewById(R.id.difficulty_drawer);
        TextView open = v.findViewById(R.id.hours_drawer);
        TextView holiday = v.findViewById(R.id.holiday_drawer);
        TextView fee = v.findViewById(R.id.fee_drawer);
        ImageButton addStamp = v.findViewById(R.id.addStamp_btn);
        ImageButton deleteStampSet = v.findViewById(R.id.deleteStamp_btn);
        name.setText(stampSet.getName());
        diff.setText("Difficulty: " + stampSet.getDifficulty());
        open.setText("Open Hours: " + stampSet.getOpenHours());
        holiday.setText("Holiday: " + stampSet.getHoliday());
        fee.setText("Entry Fee: " + stampSet.getEntryFee());

        addStamp.setOnClickListener(v1 -> new AddStampDialogue(this::addStampResult).show(getChildFragmentManager(), "ModalSheet"));
        deleteStampSet.setOnClickListener(v1 -> new StampDeleteAlertDialogue(requireContext(), stampSet, this::onStampDeleted));

        RecyclerView stampList = v.findViewById(R.id.stamp_list);
        adapter = new StampRecyclerAdapter(stampSet.getStamps(), R.layout.stamp_element, (holder, position) -> {

            CardView card = holder.itemView.findViewById(R.id.stamp_sheet_element);
            ShapeableImageView stampImage = holder.itemView.findViewById(R.id.stamp_image);
            TextView stampName = holder.itemView.findViewById(R.id.stampNo_drawer);
            TextView obtainable = holder.itemView.findViewById(R.id.obtainable_drawer);
            TextView location = holder.itemView.findViewById(R.id.location_drawer);
            TextView owned = holder.itemView.findViewById(R.id.owned_drawer);

            Stamp stamp = stampSet.getStamps().get(position);
            SpannableString content = new SpannableString(stamp.getName()) ;
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0) ;
            stampName.setText(content);
            location.setText("設置場所: " + stamp.getLocation());
            obtainable.setText("存在: " + (stamp.getIsObtainable() ? "Yes" : "No"));
            owned.setText("所持: " + (stamp.getIsObtained() ? "Yes" : "No"));
            StampCollection.loadImage(holder.itemView, stamp, stampImage);

            card.setOnClickListener(v1 -> {
                View popupView = LayoutInflater.from(requireContext()).inflate(R.layout.stamp_sheet_popup, null, false);
                ((TextView)popupView.findViewById(R.id.stamp_name)).setText(stamp.getName());
                SwitchCompat obtained = popupView.findViewById(R.id.obtained_switch);
                obtained.setChecked(stamp.getIsObtained());
                obtained.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    StampCollection.getInstance().setStampObtained(stamp, stampSet, isChecked);
                    owned.setText("所持: " + (stamp.getIsObtained() ? "Yes" : "No"));
                });
                SwitchCompat onWishlist = popupView.findViewById(R.id.onWish_switch);
                onWishlist.setChecked(stamp.getIsOnWishlist());
                onWishlist.setOnCheckedChangeListener((buttonView, isChecked) -> StampCollection.getInstance().setStampOnWishlist(stamp, stampSet, isChecked));

                new AlertDialog.Builder(requireContext()).setView(popupView).show();
            });
            card.setOnLongClickListener(v1 -> {
                new StampDeleteAlertDialogue(requireContext(), stamp, this::onStampDeleted);
                return false;
            });
        });

        stampList.setAdapter(adapter);
        stampList.setLayoutManager(new LinearLayoutManager(requireContext()));

        return v;
    }

    public void addStampResult(String name, String image){
        Stamp stamp = new Stamp(name, stampSet.getAddress(), stampSet.getStamps().get(0).getLocation(), image, true, stampSet.getStamps().get(0).getCoordinates(), true, false, false, 0, "");
        StampCollection.getInstance().addCustomStamp(stamp, stampSet);
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    void onStampDeleted(){
        if (stampSet.getStamps().isEmpty()){
            requireActivity().getSupportFragmentManager().getFragments().stream().filter(ExploreFragment.class::isInstance).findFirst().ifPresent(fragment -> {
                ((ExploreFragment)fragment).loadMarkers();
                dismiss();
            });
        }
        adapter.notifyDataSetChanged();
    }
}
