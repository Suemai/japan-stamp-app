package com.test.stampmap.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.test.stampmap.Class.help;
import com.test.stampmap.R;

import java.util.List;

public class HelpRecyclerAdapter extends RecyclerView.Adapter<HelpRecyclerAdapter.ViewHolder> {

    public final List<help> helpList;
    //private final int layout;

    public HelpRecyclerAdapter(List<help> helpList) {
        this.helpList = helpList;
        //this.layout = layout;
    }

    @Override
    public HelpRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.help_card, parent, false);
        return new HelpRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HelpRecyclerAdapter.ViewHolder holder, int position) {
        help helpItem = helpList.get(position);
        holder.helpTitle.setText(helpItem.getTitle());
        holder.helpText.setText(helpItem.getDescription());

        boolean isExpanded = helpList.get(position).isExpanded();
        //if layer is expanded make visible else it's not visible
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        //making the arrow image change
        if (isExpanded){
            holder.arrowImage.setImageResource(R.drawable.up_arrow);
        }else{
            holder.arrowImage.setImageResource(R.drawable.down_arrow);
        }
    }

    @Override
    public int getItemCount() {
        return helpList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView helpTitle, helpText;
        RelativeLayout expandableLayout;

        ImageView arrowImage;
        public ViewHolder(View itemView) {
            super(itemView);

            helpTitle = itemView.findViewById(R.id.help_title);
            helpText = itemView.findViewById(R.id.help_text);
            expandableLayout = itemView.findViewById(R.id.expandable);
            arrowImage = itemView.findViewById(R.id.expandable_arrows);

            helpTitle.setOnClickListener(view -> {
                help helpItem = helpList.get(getAdapterPosition());
                helpItem.setExpanded(!helpItem.isExpanded());
                notifyItemChanged(getAdapterPosition());
            });
        }
    }
}
