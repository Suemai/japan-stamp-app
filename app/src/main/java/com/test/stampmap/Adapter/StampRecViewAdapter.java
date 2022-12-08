package com.test.stampmap.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.test.stampmap.ViewHolders.StampViewHolder;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class StampRecViewAdapter<T extends StampViewHolder> extends RecyclerView.Adapter<T>{

    private final List<?> dataSet;
    private final StampRecViewAdapter.OnBindViewHolder<T> onHolderBind;
    private final Class<T> viewHolderType;

    public StampRecViewAdapter(List<?> dataSet, Class<T> holderType, StampRecViewAdapter.OnBindViewHolder<T> onHolderBind){
        this.dataSet = dataSet;
        this.onHolderBind = onHolderBind;
        this.viewHolderType = holderType;
    }

    @NotNull
    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) throws NullPointerException {
        try {
            StampViewHolder thing = viewHolderType.getDeclaredConstructor(View.class).newInstance(new View(parent.getContext()));
            View v = LayoutInflater.from(parent.getContext()).inflate(thing.getLayout(), parent, false);
            return (viewHolderType.getDeclaredConstructor(View.class).newInstance(v));
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ignored) {}
        throw new NullPointerException("git gud");
    }

    @Override
    public void onBindViewHolder(@NotNull T holder, int position) {
        onHolderBind.onBind(holder, position);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public interface OnBindViewHolder<T extends StampViewHolder> {
        void onBind(T holder, int position);
    }
}
