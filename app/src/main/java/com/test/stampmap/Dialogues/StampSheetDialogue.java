package com.test.stampmap.Dialogues;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.imageview.ShapeableImageView;
import com.test.stampmap.Adapter.StampRecViewAdapter;
import com.test.stampmap.R;
import com.test.stampmap.Stamp.Stamp;
import com.test.stampmap.Stamp.StampCollection;
import com.test.stampmap.Stamp.StampSet;
import com.test.stampmap.ViewHolders.StampViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class StampSheetDialogue extends BottomSheetDialogFragment {

    private static StampSet stampSet;

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
        name.setText(stampSet.getName());
        diff.setText("Difficulty: " + stampSet.getDifficulty());
        open.setText("Open Hours: " + stampSet.getOpenHours());
        holiday.setText("Holiday: " + stampSet.getHoliday());
        fee.setText("Entry Fee: " + stampSet.getEntryFee());

        RecyclerView stampList = v.findViewById(R.id.stamp_list);
        StampRecViewAdapter<StampSheetViewHolder> adapter = new StampRecViewAdapter<>(stampSet.getStamps(), StampSheetViewHolder.class, (holder, position) -> {
            Stamp stamp = stampSet.getStamps().get(position);
            SpannableString content = new SpannableString(stamp.getName()) ;
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0) ;
            holder.stampName.setText(content);
            holder.location.setText("設置場所: " + stamp.getLocation());
            holder.obtainable.setText("存在: " + (stamp.getIsObtainable() ? "Yes" : "No"));
            holder.owned.setText("所持: " + (stamp.getIsObtained() ? "Yes" : "No"));
            StampCollection.loadImage(holder.itemView, stamp, holder.stampImage);

            holder.card.setOnClickListener(v1 -> {
                AlertDialog.Builder popup = new AlertDialog.Builder(requireContext());
                View popupView = LayoutInflater.from(requireContext()).inflate(R.layout.stamp_sheet_popup, null, false);
                ((TextView)popupView.findViewById(R.id.stamp_name)).setText(stamp.getName());
                SwitchCompat obtained = popupView.findViewById(R.id.obtained_switch);
                obtained.setChecked(stamp.getIsObtained());
                obtained.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    StampCollection.getInstance().setStampObtained(stamp, stampSet, isChecked);
                    StampCollection.getInstance().saveMyStamps(requireContext());
                    holder.owned.setText("所持: " + (stamp.getIsObtained() ? "Yes" : "No"));
                });
                SwitchCompat onWishlist = popupView.findViewById(R.id.onWish_switch);
                onWishlist.setChecked(stamp.getIsOnWishlist());
                onWishlist.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    StampCollection.getInstance().setStampOnWishlist(stamp, stampSet, isChecked);
                    StampCollection.getInstance().saveMyStamps(requireContext());
                });
                popup.setView(popupView); popup.show();
            });
        });

        stampList.setAdapter(adapter);
        stampList.setLayoutManager(new LinearLayoutManager(requireContext()));

        return v;
    }

    public static class StampSheetViewHolder extends StampViewHolder {

        public final CardView card;
        public final ShapeableImageView stampImage;
        public final TextView stampName, obtainable, location, owned;

        public StampSheetViewHolder(View itemView) {
            super(itemView);
            this.card = itemView.findViewById(R.id.stamp_sheet_element);
            this.stampImage = itemView.findViewById(R.id.stamp_image);
            this.stampName = itemView.findViewById(R.id.stampNo_drawer);
            this.obtainable = itemView.findViewById(R.id.obtainable_drawer);
            this.location = itemView.findViewById(R.id.location_drawer);
            this.owned = itemView.findViewById(R.id.owned_drawer);
        }

        @Override
        public int getLayout() {
            return R.layout.stamp_element;
        }
    }
}
