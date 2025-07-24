package com.test.stampmap.Stamp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.test.stampmap.Interface.BoolMethod;
import com.test.stampmap.Stamp.Receivers.Receiver;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class StampCollection {

    private static final String SAVE_FILE_NAME = "MyStamps";
    private static final String STAMP_FILE = "all-stamps-coords.json";

    private static final StampCollection instance = new StampCollection();
    private boolean initialised = false;

    private final HashMap<Integer, StampSet> allStamps = new HashMap<>();
    private final HashMap<Integer, StampSet> userStampData = new HashMap<>();
    private final ArrayList<StampSet> currentFilteredStamps = new ArrayList<>();

    private final ArrayList<Receiver.MyStampsUpdateReceiver> myStampsUpdateCallback = new ArrayList<>();
    private final ArrayList<Receiver.WishlistUpdateReceiver> wishlistUpdateCallback = new ArrayList<>();
    private final ArrayList<Receiver.CustomStampsUpdateReceiver> customStampsUpdateCallback = new ArrayList<>();

    private Application app;

    public static StampCollection getInstance(){
        return instance;
    }

    public List<StampSet> getAllStamps(){
        return new ArrayList<>(allStamps.values());
    }

    public List<StampSet> getMyStamps(){
        return userStampData.values().stream().filter(StampSet::getContainsObtained).collect(Collectors.toList());
    }

    public List<StampSet> getWishlist(){
        return userStampData.values().stream().filter(StampSet::getContainsWishlist).collect(Collectors.toList());
    }

    public List<StampSet> getCustomStamps(){
        return userStampData.values().stream().filter(StampSet::getContainsCustom).collect(Collectors.toList());
    }

    public List<StampSet> getCurrentFilteredStamps(){
        return currentFilteredStamps;
    }

    public StampSet getStampSetByHash(int hash){
        return allStamps.get(hash);
    }

    public void load(Application application){
        if (initialised) return;
        initialised = true;
        this.app = application;
        JSONArray stampList = loadJSONArrayFromAsset(application.getApplicationContext());
        allStamps.clear();
        for (int i=0; i<stampList.length(); i++) {
            StampSet stampSet = StampSet.StampSetFromJSON(stampList.optJSONObject(i));
            allStamps.put(stampSet.hashCode(), stampSet);
        }
        loadCustomStampData();
    }

    private void loadCustomStampData(){
        ObjectInputStream objIn;
        try (FileInputStream in = app.getApplicationContext().openFileInput(SAVE_FILE_NAME)){
            objIn = new ObjectInputStream(in);
            this.userStampData.putAll(StampSerialiser.deserialiseStamps((String)objIn.readObject()));
            allStamps.putAll(userStampData);
        } catch (IOException | ClassNotFoundException ignored) {}
    }

    public void saveMyStamps(){
        Executors.newSingleThreadExecutor().execute(() -> {
            ObjectOutputStream objOut;
            try (FileOutputStream out = app.getApplicationContext().openFileOutput(SAVE_FILE_NAME, Activity.MODE_PRIVATE)){
                objOut = new ObjectOutputStream(out);
                objOut.writeObject(StampSerialiser.serialiseStamps(this.userStampData));
                out.getFD().sync();
            } catch (IOException ignored) {}
        });
    }

    public void setStampObtained(Stamp stamp, StampSet stampSet, boolean value){
        stamp.setObtained(value);
        if (value) userStampData.put(stampSet.hashCode(), stampSet);
        else removeIfNecessary(stampSet, StampSet::getContainsCustomData);
        saveMyStamps();
        for (Receiver.MyStampsUpdateReceiver callback : myStampsUpdateCallback) callback.onMyStampsUpdate();
    }

    public void setStampDateObtained(Stamp stamp, LocalDate date) {
        if (!stamp.getIsObtained()) return;
        stamp.setDateObtained(date);
        saveMyStamps();
    }

    public void setStampOnWishlist(Stamp stamp, StampSet stampSet, boolean value){
        stamp.setOnWishlist(value);
        if (value) userStampData.put(stampSet.hashCode(), stampSet);
        else removeIfNecessary(stampSet, StampSet::getContainsCustomData);
        saveMyStamps();
        for (Receiver.WishlistUpdateReceiver callback : wishlistUpdateCallback) callback.onWishlistUpdate();
    }

    public void setStampNotes(Stamp stamp, StampSet stampSet, String notes){
        stamp.setNotes(notes);
        if (!notes.isEmpty()) userStampData.put(stampSet.hashCode(), stampSet);
        else removeIfNecessary(stampSet, StampSet::getContainsCustomData);
        saveMyStamps();
    }

    public void addCustomStampSet(StampSet stampSet){
        allStamps.put(stampSet.hashCode(), stampSet);
        userStampData.put(stampSet.hashCode(), stampSet);
        saveMyStamps();
        for (Receiver.CustomStampsUpdateReceiver callback : customStampsUpdateCallback) callback.onCustomStampsUpdate();
    }

    public void addCustomStamp(Stamp stamp, StampSet stampSet){
        stampSet.getStamps().add(stamp);
        userStampData.put(stampSet.hashCode(), stampSet);
        saveMyStamps();
        for (Receiver.CustomStampsUpdateReceiver callback : customStampsUpdateCallback) callback.onCustomStampsUpdate();
    }

    public void deleteCustomStamp(Stamp stamp){
        StampSet parentSet = userStampData.values().stream().filter(stampSet -> {
            for (Stamp stamp2 : stampSet) if (stamp2.equals(stamp)) return true;
            return false;
        }).findFirst().orElse(null);
        if (parentSet != null) {
            parentSet.getStamps().remove(stamp);
            if (parentSet.getStamps().isEmpty()) {
                deleteStampSet(parentSet);
                return;
            }
            saveMyStamps();
            announceAllUpdateReceivers();
        }
    }

    public void deleteStampSet(StampSet stampSet){
        for (Stamp stamp : stampSet) if (!stamp.getIsCustom()) return;
        userStampData.remove(stampSet.hashCode(), stampSet);
        allStamps.remove(stampSet.hashCode(), stampSet);
        currentFilteredStamps.remove(stampSet);
        stampSet.getStamps().clear();
        saveMyStamps();
        announceAllUpdateReceivers();
    }

    public void addMyStampsUpdateEvent(Receiver.MyStampsUpdateReceiver receiver){
        myStampsUpdateCallback.add(receiver);
    }

    public void addWishlistUpdateEvent(Receiver.WishlistUpdateReceiver receiver){
        wishlistUpdateCallback.add(receiver);
    }

    public void addCustomStampsUpdateEvent(Receiver.CustomStampsUpdateReceiver receiver){
        customStampsUpdateCallback.add(receiver);
    }

    public void announceAllUpdateReceivers() {
        for (Receiver.CustomStampsUpdateReceiver callback : customStampsUpdateCallback) callback.onCustomStampsUpdate();
        for (Receiver.WishlistUpdateReceiver callback : wishlistUpdateCallback) callback.onWishlistUpdate();
        for (Receiver.MyStampsUpdateReceiver callback : myStampsUpdateCallback) callback.onMyStampsUpdate();
    }

    public static void loadImage(View view, Stamp stamp, ImageView imageView){
        if (stamp.getIsCustom()) loadImage(stamp.getImageLink(), imageView);
        else {
            Glide.with(view).load(new GlideUrl(stamp.getImageLink(), new LazyHeaders.Builder()
                    .addHeader("referer", "https://stamp.funakiya.com/").build())).into(imageView);
        }
    }

    public static void loadImage(String base64, ImageView imageView){
        byte[] decodedString = Base64.getDecoder().decode(base64);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageView.setImageBitmap(decodedByte);
    }

    private void removeIfNecessary(StampSet stampSet, BoolMethod method){
        if (method.hasMatch(stampSet)) return;
        userStampData.remove(stampSet.hashCode());
    }

    /**
     * Thank you StackOverflow
     *
     * @return {@link String} of JSON data from file.
     */
    private JSONArray loadJSONArrayFromAsset(Context context) {
        String json;
        try {
            InputStream is = context.getAssets().open(STAMP_FILE);
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
