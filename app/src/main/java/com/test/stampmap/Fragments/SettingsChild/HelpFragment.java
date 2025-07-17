package com.test.stampmap.Fragments.SettingsChild;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.test.stampmap.Adapter.HelpRecyclerAdapter;
import com.test.stampmap.Class.help;
import com.test.stampmap.R;

import java.util.ArrayList;
import java.util.List;


public class HelpFragment extends Fragment {

    RecyclerView recyclerView;
    List<help> helplist;

    HelpRecyclerAdapter adapter;
    private View v;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_help, container, false);
        recyclerView = v.findViewById(R.id.help_rec);

        helplist = new ArrayList<>();
        innitData();
        recyclerView();

        return v;
    }

    public void recyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HelpRecyclerAdapter(helplist);
        recyclerView.setAdapter(adapter);
    }

    public void innitData(){
        helplist.add(new help(R.string.help_things_to_note_title, R.string.help_things_to_note_content));
        helplist.add(new help(R.string.help_how_to_search_title, R.string.help_how_to_search_content));
        helplist.add(new help(R.string.help_difficulty_system_title, R.string.help_difficulty_system_content));
        helplist.add(new help(R.string.help_custom_stamp_title, R.string.help_custom_stamp_content));
        helplist.add(new help(R.string.help_tips_title, R.string.help_tips_content));
    }
}