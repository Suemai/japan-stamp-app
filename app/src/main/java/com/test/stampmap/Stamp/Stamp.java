package com.test.stampmap.Stamp;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class Stamp {
    private final String name;
    private final String address;
    private final String location;
    private final String imageLink;
    private final boolean isObtainable;
    private final GeoPoint coordinates;
    private final boolean isCustom;
    private boolean isObtained;
    private boolean isOnWishlist;
    private long dateObtained;
    private String notes;

    public Stamp() {
        this("", "", "", "", false, new GeoPoint(0f, 0f), true, false, false, 0, "");
    }

    public Stamp(String name, String address, String location, String imageLink, boolean isObtainable, GeoPoint coordinates, boolean isCustom, boolean isObtained, boolean isOnWishlist, long dateObtained, String notes) {
        this.name = name;
        this.address = address;
        this.location = location;
        this.imageLink = imageLink;
        this.isObtainable = isObtainable;
        this.coordinates = coordinates;
        this.isCustom = isCustom;
        this.isObtained = isObtained;
        this.isOnWishlist = isOnWishlist;
        this.dateObtained = dateObtained;
        this.notes = notes;
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

    public String getNotes() { return this.notes != null ? this.notes : ""; }

    protected void setObtained(boolean value) {
        this.isObtained = value;
        this.dateObtained = value ? LocalDate.now().toEpochDay() : 0;
    }

    protected void setDateObtained(LocalDate date) {
        if (!getIsObtained()) dateObtained = 0;
        else dateObtained = date.toEpochDay();
    }

    protected void setOnWishlist(boolean value) {
        this.isOnWishlist = value;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public StampSet getParentStampSet(){
        return StampCollection.getInstance().getAllStamps().stream().filter(stampSet -> {
            for (Stamp stamp2 : stampSet) if (stamp2.equals(this)) return true;
            return false;
        }).findFirst().orElse(null);
    }

    public static Stamp StampFromJSON(JSONObject JSONStamp){
        String coords = "0.0,0.0";
        try{
            coords = JSONStamp.getJSONArray("経緯度").join(",");
        } catch (JSONException ignored) {}
        GeoPoint position = GeoPoint.fromDoubleString(coords, ',');
        return new Stamp(JSONStamp.optString("名前"), JSONStamp.optString("所在地"), JSONStamp.optString("設置場所"), JSONStamp.optString("リンク"), JSONStamp.optBoolean("存在"), position, false, false, false, 0, "");
    }

    @NotNull
    @Override
    public String toString(){
        String name = "Name: " + this.name + "\n";
        String location = "Location: " + this.location + "\n";
        String obtainable = "Obtainable: " + (this.isObtainable ? "Yes" : "No");
        return name + location + obtainable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stamp that = (Stamp) o;
        return this.name.equals(that.name) && this.address.equals(that.address) && this.location.equals(that.location) && this.imageLink.equals(that.imageLink);
    }

    @Override
    public int hashCode(){
        return name.hashCode() + address.hashCode() + location.hashCode() + imageLink.hashCode();
    }
}
