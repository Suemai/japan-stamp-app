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
        helplist.add(new help("Things to note", "1. The stamps in question are different to the shrine/temple " +
                "seals that are available at temples and shrines. If you do plan to collect them, again you can use the " +
                "app to make a record of them, if you so wish. \n" +
                "However, there are no current or future plans to implement them into the app. \n" +
                "2. Due to the nature of the app and the source of the data, you must search in Japanese Kanji. \n" +
                "3. Due to the nature of the app and the source of the data, information will mostly if not be fully displayed in Japanese. Therefore it is recommended that if you do not know Japanese, have a translator handy. \n" +
                "4. Updates may come incredibly slow, so bare with us. \n" +
                "5. This app is by no means a full compulsory list of stamps, meaning that not all stamps available will be on the app."));
        helplist.add(new help("How to search on the app", "Due to the nature of the app, you must search in Kanji. \n" +
                "This can be done by pressing the search bar and typing the location, alternatively you can search via filtering. \n \n" +
                "To use filtering to search: \n" +
                "1. In the explore tab, press the 3 lines on the search bar. \n" +
                "2. Choose your filter or filters. You can have more than one filter. \n" +
                "3. Once filters are chosen, press Filter Search. \n" +
                "If you want to filter search again, make sure to press Clear Filters before filtering your search again."));
        helplist.add(new help("The difficulty star system",
                "You may have noticed the difficulty star system that is in the app. \n" +
                "Well, this is due to various factors, such as availability of the stamp, location of the stamp (e.g. on the way up to Mt Fuji), " +
                        "whether there are train station staff are available at specific hours, (I experienced this one the hard way) or other " +
                        "requirements. \n"+
                        "Honestly, not 100% sure here, since this was due to the difficulty scaling from our source website, but that's what makes " +
                        "finding them fun. o(≧▽≦)o"));
        helplist.add(new help("How to create a custom stamp or stamp set", "Let's first define a stamp and a stamp set. \n" +
                "A stamp set is the the group of stamps in a singular location. A stamp is a singular stamp. \n" +
                "You can have any number of stamps in a stamp set, but not vice versa. \n" +
                "1. Long hold the location where the stamp location is located. \n" +
                "2. Fill in the details of the stamp location. \n" +
                "3. Press Add Stamp to add an individual stamp, to finish press Add Stamp again. \n" +
                "4. To add more stamps in a stamp set, repeat step 3. \n" +
                "5. Once all information is completed \n" +
                "Key things to note with custom stamps: \n" +
                " - Currently there is no way to filter for custom stamps using prefecture or difficulty. \n" +
                " - The only filters that works with custom stamps is distance and entry fee. \n"));
        helplist.add(new help("Useful hints and tips", "To help you maximise our stamp gains, here are a few general locations where stamps are located: \n" +
                "1. Tourist information centres \n" +
                "2. Museums \n" +
                "3. Castles \n" +
                "4. Airports \n" +
                "5. Train stations \n" +
                "6. Roadside stations \n" +
                "7. Observation towers \n" +
                "8. Certain events, like Comiket 100 \n" +
                "9. Basically any tourist attraction. \n \n" +
                "Here are some useful websites that you can use as well: \n" +
                "They can be google translated for those not confident in their Japanese. \n" +
                "https://stamp.funakiya.com \n" +
                "https://100castlestamps.com \n \n" +
                "If you are still unable to find the stamp, a simple sumimasen eki (station) / kannkou (tourist) sutanpu wa doko desu ka? does the trick.\n" +
                "And with that, go and stamp 'em all! ＼(≧▽≦)／ \n " +
                "[insert red pointing at you to go catch 'em all]"));
    }
}