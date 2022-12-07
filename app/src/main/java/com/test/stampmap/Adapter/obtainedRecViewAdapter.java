package com.test.stampmap.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.test.stampmap.R;
import com.test.stampmap.Stamp.StampSet;

import java.util.ArrayList;

public class obtainedRecViewAdapter extends RecyclerView.Adapter<obtainedRecViewAdapter.ViewHolder> {

    // =================
    // constructor
    // =================
    private View v;
    private ArrayList<StampSet> stamps = new ArrayList<>();
    private Context context;

    public obtainedRecViewAdapter(Context context, ArrayList<StampSet>stamps){
        this.context = context;
        this.stamps = stamps;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stamp_card, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.stampName.setText(stamps.get(position).getName());

        //the line to get the image
        //holder.stampImage.setImageResource(stamps.get(position).getStamps());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int position = holder.getAdapterPosition();
                Toast.makeText(context, stamps.get(position).getName() + " selected", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return stamps.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView stampName;
        private ImageView stampImage;

        private CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            stampName = itemView.findViewById(R.id.stamp_name);
            stampImage = itemView.findViewById(R.id.stamp_image);
            card = itemView.findViewById(R.id.obtained_parent);
        }
}
}