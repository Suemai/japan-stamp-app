package com.test.stampmap.Stamp;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.test.stampmap.Interface.BoolMethod;
import com.test.stampmap.Stamp.Receivers.Receiver;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class StampCollection {

    private static final String SAVE_FILE_NAME = "MyStamps";
    private static final String STAMP_FILE = "all-stamps-coords.json";

    private static final StampCollection instance = new StampCollection();

    private final HashMap<Integer, StampSet> allStamps = new HashMap<>();
    private final HashMap<Integer, StampSet> myStamps = new HashMap<>();
    private final HashMap<Integer, StampSet> wishlist = new HashMap<>();
    private final HashMap<Integer, StampSet> customStamps = new HashMap<>();
    private final ArrayList<StampSet> currentFilteredStamps = new ArrayList<>();

    private final ArrayList<Receiver.MyStampsUpdateReceiver> myStampsUpdateCallback = new ArrayList<>();
    private final ArrayList<Receiver.WishlistUpdateReceiver> wishlistUpdateCallback = new ArrayList<>();

    public static StampCollection getInstance(){
        return instance;
    }

    public ArrayList<StampSet> getAllStamps(){
        return new ArrayList<>(allStamps.values());
    }

    public ArrayList<StampSet> getMyStamps(){
        return new ArrayList<>(myStamps.values());
    }

    public ArrayList<StampSet> getWishlist(){
        return new ArrayList<>(wishlist.values());
    }

    public ArrayList<StampSet> getCustomStamps(){
        return new ArrayList<>(customStamps.values());
    }

    public ArrayList<StampSet> getCurrentFilteredStamps(){
        return currentFilteredStamps;
    }

    public void load(Context context){
        JSONArray stampList = loadJSONArrayFromAsset(context);
        allStamps.clear();
        for (int i=0; i<stampList.length(); i++) {
            StampSet stampSet = StampSet.StampSetFromJSON(stampList.optJSONObject(i));
            allStamps.put(stampSet.hashCode(), stampSet);
        }
        loadCustomStampData(context);
    }

    private void loadCustomStampData(Context context){
        ObjectInputStream objIn;
        try (FileInputStream in = context.openFileInput(SAVE_FILE_NAME)){
            objIn = new ObjectInputStream(in);
            this.myStamps.putAll((HashMap<Integer, StampSet>) objIn.readObject());
            this.wishlist.putAll((HashMap<Integer, StampSet>) objIn.readObject());
            this.wishlist.putAll((HashMap<Integer, StampSet>) objIn.readObject());
            allStamps.putAll(myStamps);
            allStamps.putAll(wishlist);
            allStamps.putAll(customStamps);
        } catch (IOException | ClassNotFoundException ignored) {}
    }

    public void saveMyStamps(Context context){
        ObjectOutputStream objOut;
        try (FileOutputStream out = context.openFileOutput(SAVE_FILE_NAME, Activity.MODE_PRIVATE)){
            objOut = new ObjectOutputStream(out);
            objOut.writeObject(this.myStamps);
            objOut.writeObject(this.wishlist);
            objOut.writeObject(this.customStamps);
            out.getFD().sync();
        } catch (IOException ignored) {}
    }

    public void setStampObtained(Stamp stamp, StampSet stampSet, boolean value){
        stamp.setObtained(value);
        if (value) myStamps.put(stampSet.hashCode(), stampSet);
        else removeIfNecessary(stampSet, StampSet::getIsObtained, myStamps);
        for (Receiver.MyStampsUpdateReceiver callback : myStampsUpdateCallback) callback.onMyStampsUpdate();
    }

    public void setStampOnWishlist(Stamp stamp, StampSet stampSet, boolean value){
        stamp.setOnWishlist(value);
        if (value) wishlist.put(stampSet.hashCode(), stampSet);
        else removeIfNecessary(stampSet, StampSet::getIsOnWishlist, wishlist);
        for (Receiver.WishlistUpdateReceiver callback : wishlistUpdateCallback) callback.onWishlistUpdate();
    }

    public void addMyStampsUpdateEvent(Receiver.MyStampsUpdateReceiver receiver){
        myStampsUpdateCallback.add(receiver);
    }

    public void addWishlistUpdateEvent(Receiver.WishlistUpdateReceiver receiver){
        wishlistUpdateCallback.add(receiver);
    }

    public static void loadImage(View view, Stamp stamp, ImageView imageView){
        Glide.with(view).load(new GlideUrl(stamp.getImageLink(), new LazyHeaders.Builder()
                .addHeader("referer", "https://stamp.funakiya.com/").build())).into(imageView);
    }

    private void removeIfNecessary(StampSet stampSet, BoolMethod method, HashMap<Integer, StampSet> listToRemoveFrom){
        for (Stamp stamp : stampSet) if (method.hasMatch(stamp)) return;
        listToRemoveFrom.remove(stampSet.hashCode());
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
