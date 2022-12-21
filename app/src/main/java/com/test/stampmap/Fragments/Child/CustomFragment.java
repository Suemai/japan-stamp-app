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
import com.test.stampmap.Stamp.StampCollection;

public class CustomFragment extends Fragment {

    private RecyclerView customView;
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

        customView = v.findViewById(R.id.customRecView);


        customRecyclerView();

        return v;
    }

    public void customRecyclerView(){

        //not done, need to do stuff first before anything else
        //customView.setAdapter(adapter);
        //customView.setLayoutManager(new GridLayoutManager(this.getContext(), 3));
    }
}