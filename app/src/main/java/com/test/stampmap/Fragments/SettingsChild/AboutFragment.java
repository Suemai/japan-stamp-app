package com.test.stampmap.Fragments.SettingsChild;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import com.test.stampmap.R;


public class AboutFragment extends Fragment {

    ImageButton email_btn, github_btn, discord_btn, back_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);


        email_btn = view.findViewById(R.id.gmail_btn);
        github_btn = view.findViewById(R.id.github_btn);
        discord_btn = view.findViewById(R.id.discord_btn);


        //not necessarily needed since u can swipe back
        //back_btn.setOnClickListener(view1 -> Navigation.findNavController(view).navigate(R.id.nav_to_settings));

//      I'll link them to stuff later
//        email_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        return view;
    }
}
