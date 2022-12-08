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
import com.test.stampmap.Adapter.StampRecViewAdapter;
import com.test.stampmap.R;
import com.test.stampmap.Stamp.Stamp;
import com.test.stampmap.Stamp.StampCollection;
import com.test.stampmap.Stamp.StampSet;
import com.test.stampmap.ViewHolders.MyStampsViewHolder;
import com.test.stampmap.ViewHolders.StampViewHolder;

import java.util.*;


public class ObtainedFragment extends Fragment {
    private RecyclerView obtainedView;
    private final ArrayList<Stamp> stamps = new ArrayList<>();
    StampRecViewAdapter<MyStampsViewHolder> adapter;

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
                if (!stamp.getIsObtained()) continue;
                stamps.add(stamp);
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
        adapter = new StampRecViewAdapter<>(stamps, MyStampsViewHolder.class, this::onBindViewHolder);
        obtainedView.setAdapter(adapter);
        obtainedView.setLayoutManager(new GridLayoutManager(this.getContext(), 3));
    }

    public void onBindViewHolder(MyStampsViewHolder holder, int position){
        holder.stampName.setText(stamps.get(position).getName());
        StampCollection.loadImage(holder.itemView, stamps.get(position), holder.stampImage);
        holder.card.setOnClickListener(view -> {
            final int position1 = holder.getAdapterPosition();
            Toast.makeText(requireContext(), stamps.get(position1).getName() + " selected boi", Toast.LENGTH_SHORT).show();
        });
    }
}