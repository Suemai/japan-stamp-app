package com.test.stampmap.Fragments.Child;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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

import java.util.ArrayList;


public class WishlistFragment extends Fragment {

    private RecyclerView wishView;
    private final ArrayList<Stamp> stamps = new ArrayList<>();
    StampRecyclerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_wishlist, container, false);

        wishView = v.findViewById(R.id.wishRecView);

        setData();

        wishRecyclerView();

        StampCollection.getInstance().addWishlistUpdateEvent(this::setData);

        return v;
    }

    void setData(){
        for (StampSet stampSet : StampCollection.getInstance().getWishlist()){
            for (Stamp stamp: stampSet){
                if (stamp.getIsOnWishlist()) stamps.add(stamp);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private void wishRecyclerView(){
        adapter = new StampRecyclerAdapter(stamps, R.layout.stamp_card, (holder, position) -> {

            // get ur views from the thingy innit
            TextView stampName = holder.itemView.findViewById(R.id.stamp_name);
            ImageView stampImage = holder.itemView.findViewById(R.id.stamp_image);
            CardView card = holder.itemView.findViewById(R.id.obtained_parent);

            // do the fancies to your stuff
            stampName.setText(stamps.get(position).getName());
            StampCollection.loadImage(holder.itemView, stamps.get(position), stampImage);
            card.setOnClickListener(view -> {
                final int position1 = holder.getAdapterPosition();
                Toast.makeText(requireContext(), stamps.get(position1).getName() + " selected boi", Toast.LENGTH_SHORT).show();
            });
        });
        wishView.setAdapter(adapter);
        wishView.setLayoutManager(new GridLayoutManager(this.getContext(), 3));
    }
}