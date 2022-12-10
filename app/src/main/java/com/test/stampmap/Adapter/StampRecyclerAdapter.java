package com.test.stampmap.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.test.stampmap.Stamp.Stamp;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class StampRecyclerAdapter extends RecyclerView.Adapter<StampRecyclerAdapter.ViewHolder>{

    private final List<Stamp> dataSet;
    private final ViewHolderBinder viewBinder;
    private final int layout;

    public StampRecyclerAdapter(List<Stamp> dataSet, int layout, ViewHolderBinder onHolderBind){
        this.dataSet = dataSet;
        this.viewBinder = onHolderBind;
        this.layout = layout;
    }

    @NotNull
    @Override
    public StampRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) throws NullPointerException {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new StampRecyclerAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NotNull StampRecyclerAdapter.ViewHolder holder, int position) {
        // this is where the method that was passed in through the constructor gets called. pretty nifty right!
        viewBinder.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    // this is the cool magic thingy that allows us to pass in the methods through the constructor
    public interface ViewHolderBinder {
        void onBindViewHolder(StampRecyclerAdapter.ViewHolder holder, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
