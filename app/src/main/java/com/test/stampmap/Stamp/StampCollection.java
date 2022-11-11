package com.test.stampmap.Stamp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class StampCollection {

    private static final String SAVE_FILE_NAME = "MyStamps";
    private static final String STAMP_FILE = "all-stamps-coords.json";

    private static StampCollection instance = new StampCollection();

    private final ArrayList<StampSet> allStamps = new ArrayList<>();
    private ArrayList<StampSet> myStamps = new ArrayList<>();
    private ArrayList<StampSet> currentFilteredStamps = new ArrayList<>();

    private boolean refreshRequired;

    public static StampCollection getInstance(){
        return instance;
    }

    public ArrayList<StampSet> getAllStamps(){
        return allStamps;
    }

    public ArrayList<StampSet> getMyStamps(){
        if (!refreshRequired) return myStamps;
        myStamps.clear();
        for (StampSet stampSet : allStamps){
            for (Stamp stamp : stampSet){
                if (stamp.getIsObtained()){
                    myStamps.add(stampSet);
                    break;
                }
            }
        }
        return myStamps;
    }

    public ArrayList<StampSet> getCurrentFilteredStamps(){
        return currentFilteredStamps;
    }

    public void load(Context context){
        JSONArray stampList = loadJSONArrayFromAsset(STAMP_FILE, context);
        allStamps.clear();
        for (int i=0; i<stampList.length(); i++)
            allStamps.add(StampSet.StampSetFromJSON(stampList.optJSONObject(i)));
        if (getMyStamps().isEmpty()) loadMyStamps(context);
    }

    private void loadMyStamps(Context context){
        ObjectInputStream objIn;
        try (FileInputStream in = context.openFileInput(SAVE_FILE_NAME)){
            objIn = new ObjectInputStream(in);
            this.myStamps = new ArrayList<>((ArrayList<StampSet>) objIn.readObject());
            for (StampSet myStamp : this.myStamps){
                for (StampSet allStamp : this.allStamps){
                    if (myStamp.equals(allStamp)){
                        allStamps.set(allStamps.indexOf(allStamp), myStamp);
                        break;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException ignored) {}
    }

    public void saveMyStamps(Context context){
        ObjectOutputStream objOut;
        try (FileOutputStream out = context.openFileOutput(SAVE_FILE_NAME, Activity.MODE_PRIVATE)){
            objOut = new ObjectOutputStream(out);
            objOut.writeObject(getMyStamps());
            out.getFD().sync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setObtainedStamp(StampSet stampSet, int index, boolean value){
        stampSet.getStamps().get(index).setObtained(value);
        refreshRequired = true;
    }

    /**
     * Thank you StackOverflow
     *
     * @param fileName file to be loaded from assets
     * @return {@link String} of JSON data from file.
     */
    private JSONArray loadJSONArrayFromAsset(String fileName, Context context) {
        String json;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        JSONArray parsed = null;
        try {
            parsed = new JSONArray(json);
        } catch (JSONException ignored) {}
        return parsed;
    }
}
