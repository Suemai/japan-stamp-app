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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.test.stampmap.R;
import com.test.stampmap.Stamp.Stamp;
import com.test.stampmap.Stamp.StampSet;

import java.util.ArrayList;

public class obtainedRecViewAdapter extends RecyclerView.Adapter<obtainedRecViewAdapter.ViewHolder> {

    // =================
    // constructor
    // =================
    private View v;
    private final ArrayList<Stamp> stamps;
    private final Context context;

    public obtainedRecViewAdapter(Context context, ArrayList<Stamp>stamps){
        this.context = context;
        this.stamps = stamps;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stamp_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.stampName.setText(stamps.get(position).getName());

        //the line to get the image
        //holder.stampImage.setImageResource(stamps.get(position).getStamps());
        Glide.with(v).load(new GlideUrl(stamps.get(position).getImageLink(), new LazyHeaders.Builder()
                .addHeader("referer", "https://stamp.funakiya.com/").build())).into(holder.stampImage);

        holder.card.setOnClickListener(view -> {
            final int position1 = holder.getAdapterPosition();
            Toast.makeText(context, stamps.get(position1).getName() + " selected", Toast.LENGTH_SHORT).show();
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