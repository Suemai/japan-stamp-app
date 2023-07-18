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
import com.test.stampmap.R;
import com.test.stampmap.Stamp.Stamp;
import com.test.stampmap.Stamp.StampCollection;
import com.test.stampmap.Stamp.StampSet;

import java.util.ArrayList;


public class NotObtainedFragment extends Fragment {

    private RecyclerView noStampsView;
    private final ArrayList<Stamp> stamps = new ArrayList<>();

    private Stamp stampInfo;
    StampRecyclerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_not_obtained, container, false);

        noStampsView = v.findViewById(R.id.noStampRecView);

        setData();

        noStampsRecyclerView();

        StampCollection.getInstance().addMyStampsUpdateEvent(this::setData);

        return v;
    }

    void setData(){
        stamps.clear();
        for (StampSet stampSet : StampCollection.getInstance().getAllStamps()){
            for (Stamp stamp : stampSet) {
                if (!stamp.getIsObtained() && stamp.getIsObtainable()) {
                    stamps.add(stamp);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    private void noStampsRecyclerView(){
        // this is the lambda example of the constructor
        // works exactly the same except the code is now all in one block instead of split up
        adapter = new StampRecyclerAdapter(stamps, R.layout.stamp_card, (holder, position) -> {

            // get ur views from the thingy innit
            TextView stampName = holder.itemView.findViewById(R.id.stamp_name);
            ImageView stampImage = holder.itemView.findViewById(R.id.stamp_image);
            CardView card = holder.itemView.findViewById(R.id.obtained_parent);

            // do the fancies to your stuff
            stampInfo = stamps.get(position);
            Bundle args = new Bundle();
            args.putSerializable("stamp",stampInfo);

            stampName.setText(stamps.get(position).getName());
            StampCollection.loadImage(holder.itemView, stamps.get(position), stampImage);
            card.setOnClickListener(view -> {
                final int position1 = holder.getAdapterPosition();
                Toast.makeText(requireContext(), stamps.get(position1).getName() + " selected boi", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigate(R.id.myStamp_to_stampInfo, args);
            });
        });
        noStampsView.setAdapter(adapter);
        noStampsView.setLayoutManager(new GridLayoutManager(this.getContext(),3));
    }
}
