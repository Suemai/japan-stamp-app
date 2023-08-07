package com.test.stampmap.Settings;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.*;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import com.test.stampmap.BuildConfig;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import android.os.Handler;

public class UpdateManager {

    private Context context;
    private String currentVersion;
    private JSONObject latest;
    private JSONArray assets;
    private Handler handler = new Handler(Looper.getMainLooper());


    public UpdateManager(Context context){
        this.context = context;
    }

    public void checkForUpdates() {
        if(!connectivity()){
            Toast.makeText(context, "Unable to connect to the internet.", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Searching for updates", Toast.LENGTH_SHORT).show();

            //Checking on GitHub
            String gitHubUrl = "https://api.github.com/repos/Suemai/japan-stamp-app/releases/latest";

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(gitHubUrl)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Toast.makeText(context, "Failed to search, please try again", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String jsonResponse = response.body().string();

                    //Just to test and make sure I'm getting the right response
                    Log.d("JSON response: ", jsonResponse);

                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                latest = new JSONObject(jsonResponse);
                                String latestVersion = latest.getString("tag_name");
                                //Log.d("Latest Version: ", latestVersion);

                                int compareResult = compareVersions(latestVersion);

                                Log.d("Compare result: ", String.valueOf(compareResult));
                                if (compareResult>0){
                                    //An update is available
                                    showUpdateDialog();
                                }else{
                                    Toast.makeText(context,"No updates available", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    });

                }
            });
        }

    }

    private void showUpdateDialog() {
        // Create and show the update dialog here
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("New Update Available");
        builder.setMessage("A new update is available. Do you want to install it now?");
        builder.setPositiveButton("Install Now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Trigger the APK download and installation here
                // Implement the download and install APK logic
                try {
                    downloadAndInstall();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        builder.setNegativeButton("Install Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private boolean connectivity(){
        //check if there is network available first
        ConnectivityManager connection = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connection.getActiveNetwork();
        return network != null;
    }

    private int compareVersions(String latestVersion){
        currentVersion = BuildConfig.VERSION_NAME;
        Log.d("Current Version: ", currentVersion);
        Log.d("Latest Version: ", latestVersion);

        // Ensure latestVersion is not null or empty
        if (latestVersion == null || latestVersion.isEmpty()) {
            Log.d("Latest Version: ", "latestVersion is null or empty");
            return -1; // Assume the latest version is smaller or unknown
        }

        //for the "v" in front of the version name on GitHub
        if (latestVersion.startsWith("v")){
            latestVersion = latestVersion.substring(1);
            Log.d("Latest Version: ", latestVersion);
        }

        // Split version strings and compare each part
        String[] latestParts = latestVersion.split("\\.");
        String[] currentParts = currentVersion.split("\\.");
        Log.d("Latest Parts: ", Arrays.toString(latestParts));
        Log.d("Current Parts: ", Arrays.toString(currentParts));

        int minLength = Math.min(latestParts.length, currentParts.length);

        for (int i = 0; i < minLength; i++) {
            int latestPart = Integer.parseInt(latestParts[i]);
            int currentPart = Integer.parseInt(currentParts[i]);

            Log.d("Latest Parts "+latestParts[i], Arrays.toString(latestParts));
            Log.d("Current Parts "+currentParts[i], Arrays.toString(currentParts));

            if (latestPart > currentPart) {
                return 1; // latestVersion is greater
            } else if (latestPart < currentPart) {
                return -1; // currentVersion is greater
            }
        }
        // The version with more parts is considered greater
        return Integer.compare(latestParts.length, currentParts.length);
    }

    private void downloadAndInstall() throws JSONException {

        assets = latest.getJSONArray("assets");
        // First asset contains the APK file
        JSONObject asset = assets.getJSONObject(0);
        String latestApk = asset.getString("browser_download_url");
        String apkTitle = asset.getString("name");
        Log.d("apk url: ", latestApk);
        Log.d("apk name: ", apkTitle);

        // Create a DownloadManager.Request with the APK download URL
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(latestApk));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,apkTitle);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        // Get the DownloadManager service and enqueue the download request
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);

        Toast.makeText(context,"Downloading...",Toast.LENGTH_SHORT).show();

        //installing

    }
}
