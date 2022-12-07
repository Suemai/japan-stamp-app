package com.test.stampmap.Fragments.Child;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.test.stampmap.Adapter.obtainedRecViewAdapter;
import com.test.stampmap.Adapter.wishRecViewAdapter;
import com.test.stampmap.R;
import com.test.stampmap.Stamp.Stamp;
import com.test.stampmap.Stamp.StampCollection;
import com.test.stampmap.Stamp.StampSet;

import java.util.ArrayList;
import java.util.HashMap;


public class WishlistFragment extends Fragment {

    private RecyclerView wishView;
    private final ArrayList<Stamp> stamps = new ArrayList<>();

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
//        adapter.notifyDataSetChanged();
    }

    private void wishRecyclerView(){

        wishRecViewAdapter adapter = new wishRecViewAdapter(this.getContext(), stamps);
        wishView.setAdapter(adapter);

        wishView.setLayoutManager(new GridLayoutManager(this.getContext(), 3));
    }
}