package com.test.stampmap.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import com.test.stampmap.R;

public class MyStampsViewHolder extends StampViewHolder {

    public final TextView stampName;
    public final ImageView stampImage;
    public final CardView card;

    public MyStampsViewHolder(View itemView) {
        super(itemView);
        stampName = itemView.findViewById(R.id.stamp_name);
        stampImage = itemView.findViewById(R.id.stamp_image);
        card = itemView.findViewById(R.id.obtained_parent);
    }

    @Override
    public int getLayout() {
        return R.layout.stamp_card;
    }
}