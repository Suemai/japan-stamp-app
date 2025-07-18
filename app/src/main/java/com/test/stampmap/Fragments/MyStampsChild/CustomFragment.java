package com.test.stampmap.Fragments.MyStampsChild;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.test.stampmap.Adapter.StampRecyclerAdapter;
import com.test.stampmap.Dialogues.StampDeleteAlertDialogue;
import com.test.stampmap.R;
import com.test.stampmap.Stamp.Stamp;
import com.test.stampmap.Stamp.StampCollection;
import com.test.stampmap.Stamp.StampSet;

import java.util.ArrayList;

public class CustomFragment extends Fragment {

    private RecyclerView customStampsView;
    private final ArrayList<Stamp> stamps = new ArrayList<>();
    StampRecyclerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_custom, container, false);

        customStampsView = v.findViewById(R.id.customRecView);

        setData();

        customStampsRecyclerView();

        // TODO: add an event for custom stamps probably
        StampCollection.getInstance().addCustomStampsUpdateEvent(this::setData);

        return v;
    }

    void setData(){
        stamps.clear();
        for (StampSet stampSet : StampCollection.getInstance().getCustomStamps()){
            for (Stamp stamp : stampSet) {
                if (stamp.getIsCustom()) {
                    stamps.add(stamp);
                }
            }
        }
        if (adapter != null && !this.isHidden()) adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    private void customStampsRecyclerView(){

        adapter = new StampRecyclerAdapter(stamps, R.layout.stamp_card, (holder, position) -> {

            // get ur views from the thingy innit
            TextView stampName = holder.itemView.findViewById(R.id.stamp_name);
            ImageView stampImage = holder.itemView.findViewById(R.id.stamp_image);
            CardView card = holder.itemView.findViewById(R.id.obtained_parent);

            // do the fancies to your stuff
            Stamp stampInfo = stamps.get(position);
            StampSet parent = stampInfo.getParentStampSet();
            Bundle args = new Bundle();
            args.putIntArray("stamp", new int[]{parent.hashCode(), stampInfo.hashCode()});

            stampName.setText(stamps.get(position).getName());
            StampCollection.loadImage(holder.itemView, stamps.get(position), stampImage);
            card.setOnClickListener(view -> {
                Navigation.findNavController(view).navigate(R.id.myStamp_to_stampInfo, args);
            });
            card.setOnLongClickListener(v -> {
                new StampDeleteAlertDialogue(requireContext(), stamps.get(position), null);
                return false;
            });
        });
        customStampsView.setAdapter(adapter);
        customStampsView.setLayoutManager(new GridLayoutManager(this.getContext(),3));
    }
}