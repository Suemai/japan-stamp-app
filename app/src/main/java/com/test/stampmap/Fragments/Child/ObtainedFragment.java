package com.test.stampmap.Fragments.Child;

import android.os.Bundle;
import android.widget.*;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.test.stampmap.Adapter.StampRecyclerAdapter;
import com.test.stampmap.R;
import com.test.stampmap.Stamp.Stamp;
import com.test.stampmap.Stamp.StampCollection;
import com.test.stampmap.Stamp.StampSet;

import java.util.*;


public class ObtainedFragment extends Fragment {
    private RecyclerView obtainedView;
    private final ArrayList<Stamp> stamps = new ArrayList<>();
    StampRecyclerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_obtained, container, false);

        obtainedView = v.findViewById(R.id.obtainedStampRecView);

        setData();

        obtainedRecyclerView();

        StampCollection.getInstance().addMyStampsUpdateEvent(this::setData);

        return v;
    }

    void setData(){
        stamps.clear();
        for (StampSet stampSet : StampCollection.getInstance().getMyStamps()) {
            for (Stamp stamp : stampSet) {
                if (stamp.getIsObtained()) stamps.add(stamp);
            }
        }
        stamps.sort(Comparator.comparingLong(Stamp::getDateObtained));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    private void obtainedRecyclerView(){
        // adapter constructor just takes the stamps list, the layout the ViewHolder will use, and the onBindViewHolder method
        adapter = new StampRecyclerAdapter(stamps, R.layout.stamp_card, this::onBindViewHolder);
        obtainedView.setAdapter(adapter);
        obtainedView.setLayoutManager(new GridLayoutManager(this.getContext(), 3));
    }

    // this is quite cool since you can define your own onBindViewHolder function and then pass it into the constructor of the adapter
    // it can also just be a lambda which I've done for the other stamp fragments
    public void onBindViewHolder(StampRecyclerAdapter.ViewHolder holder, int position){

        // get ur views from the thingy innit
        TextView stampName = holder.itemView.findViewById(R.id.stamp_name);
        ImageView stampImage = holder.itemView.findViewById(R.id.stamp_image);
        CardView card = holder.itemView.findViewById(R.id.obtained_parent);

        // do the fancies to your stuff
        stampName.setText(stamps.get(position).getName());
        StampCollection.loadImage(holder.itemView, stamps.get(position), stampImage);
        card.setOnClickListener(view -> {
            final int position1 = holder.getAdapterPosition();
            Toast.makeText(holder.itemView.getContext(), stamps.get(position1).getName() + " selected boi", Toast.LENGTH_SHORT).show();
        });
    }
}