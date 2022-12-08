package com.test.stampmap.ViewHolders;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.test.stampmap.Interface.ViewHolderLayout;
import org.jetbrains.annotations.NotNull;

public abstract class StampViewHolder extends RecyclerView.ViewHolder implements ViewHolderLayout {
    public StampViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
    }
}
