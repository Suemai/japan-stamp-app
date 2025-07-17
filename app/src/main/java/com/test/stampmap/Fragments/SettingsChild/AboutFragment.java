package com.test.stampmap.Fragments.SettingsChild;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

      //I'll link them to stuff later
        email_btn.setOnClickListener(view1 -> {
            String recipient = "suemaei@outlook.com";
            composeEmail(recipient);
        });

        github_btn.setOnClickListener(view12 -> {
            String url = "https://github.com/Suemai/japan-stamp-app";
            openUrl(url);
        });

        discord_btn.setOnClickListener(view13 -> discordDialog());
        return view;
    }

    private void composeEmail(String recipient) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + recipient));
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void openUrl(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);

        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void discordDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        builder.setTitle("Discord")
                .setPositiveButton("Open Discord", (dialog, which) -> openDiscordApp())
                .setNegativeButton("Ignore", (dialog, which) -> dialog.dismiss())
                .setCancelable(true);

        // Inflate a custom layout for the dialog with a selectable TextView
        View dialogView = View.inflate(requireContext(), R.layout.discord_dialog, null);
        TextView message = dialogView.findViewById(R.id.discord_message);
        message.setTextIsSelectable(true);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openDiscordApp() {
        String discordPackageName = "com.discord";

        // Check if the Discord app is installed
        boolean isDiscordInstalled = isAppInstalled(discordPackageName);

        if (isDiscordInstalled) {
            // Open the Discord app
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://discord.com/channels/@me"));
            startActivity(intent);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Discord")
                    .setMessage("Discord not installed!");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    private boolean isAppInstalled(String packageName) {
        PackageManager pm = requireContext().getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(packageName);
        return intent != null;
    }
}
