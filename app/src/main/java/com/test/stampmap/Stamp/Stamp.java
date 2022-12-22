package com.test.stampmap.Stamp;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.io.Serializable;
import java.util.Date;

public class Stamp implements Serializable {
    private final String name, address, location, imageLink;
    private final boolean isObtainable;
    private final GeoPoint coordinates;
    private final boolean isCustom;
    private boolean isObtained;
    private boolean isOnWishlist;
    private long dateObtained;

    public Stamp(String name, String address, String location, String imageLink, boolean isObtainable, GeoPoint coordinates, boolean isCustom) {
        this.name = name;
        this.address = address;
        this.location = location;
        this.imageLink = imageLink;
        this.isObtainable = isObtainable;
        this.coordinates = coordinates;
        this.isCustom = isCustom;
        this.isObtained = false;
        this.isOnWishlist = false;
        this.dateObtained = 0;
    }

    public String getName(){
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    public String getLocation() {
        return this.location;
    }

    public String getImageLink() {
        return this.imageLink;
    }

    public boolean getIsObtainable() {
        return this.isObtainable;
    }

    public GeoPoint getCoordinates() {
        return this.coordinates;
    }

    public boolean getIsCustom() {
        return isCustom;
    }

    public boolean getIsObtained(){
        return this.isObtained;
    }

    public boolean getIsOnWishlist(){
        return this.isOnWishlist;
    }

    public long getDateObtained() {
        return this.dateObtained;
    }

    protected void setObtained(boolean value) {
        this.isObtained = value;
        this.dateObtained = value ? new Date().toInstant().toEpochMilli() : 0;
    }

    protected void setOnWishlist(boolean value) {
        this.isOnWishlist = value;
    }

    public static Stamp StampFromJSON(JSONObject JSONStamp){
        String coords = "0.0,0.0";
        try{
            coords = JSONStamp.getJSONArray("経緯度").join(",");
        } catch (JSONException ignored) {}
        GeoPoint position = GeoPoint.fromDoubleString(coords, ',');
        return new Stamp(JSONStamp.optString("名前"), JSONStamp.optString("所在地"), JSONStamp.optString("設置場所"), JSONStamp.optString("リンク"), JSONStamp.optBoolean("存在"), position, false);
    }

    @NotNull
    @Override
    public String toString(){
        String name = "Name: " + this.name + "\n";
        String location = "Location: " + this.location + "\n";
        String obtainable = "Obtainable: " + (this.isObtainable ? "Yes" : "No");
        return name + location + obtainable;
    }
}
