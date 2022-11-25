package com.test.stampmap.Stamp;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.*;

public class StampSet implements Iterable<Stamp>, Serializable {
    private final String name, address, difficulty, openHours, holiday, entryFee;
    private final List<Stamp> stamps;

    public StampSet(String name, String address, String difficulty, String openHours, String holiday, String entryFee, List<Stamp> stamps) {
        this.name = name;
        this.address = address;
        this.difficulty = difficulty;
        this.openHours = !openHours.equals("") ? openHours : "年中無休（仮）";
        this.holiday = !holiday.equals("") ? holiday : "未確認";
        this.entryFee = !entryFee.equals("") ? entryFee : "無料（仮）";
        this.stamps = stamps;
    }

    @NotNull
    @Override
    public Iterator<Stamp> iterator() {
        return stamps.iterator();
    }

    public String getName(){
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    public String getDifficulty() {
        return this.difficulty;
    }

    public String getOpenHours() {
        return this.openHours;
    }

    public String getHoliday() {
        return this.holiday;
    }

    public String getEntryFee() {
        return this.entryFee;
    }

    public List<Stamp> getStamps(){
        return this.stamps;
    }

    public static boolean getIsObtained(Stamp stamp) {
        return stamp.getIsObtained();
    }

    public static boolean getIsOnWishlist(Stamp stamp) {
        return stamp.getIsOnWishlist();
    }

    public static StampSet StampSetFromJSON(JSONObject JSONStampSet){
        List<Stamp> stamps = new ArrayList<>();
        String[] keyNames = new String[JSONStampSet.length()];
        try {
            keyNames = JSONStampSet.names().join(",").replace("\"", "").split(",");
            JSONArray JSONStamps = JSONStampSet.getJSONArray("スタンプ");
            for (int i=0; i<JSONStamps.length(); i++) {
                JSONObject JSONStamp = JSONStamps.getJSONObject(i);
                stamps.add(Stamp.StampFromJSON(JSONStamp));
            }
        } catch (JSONException ignored) {}
        String openHoursKey = Arrays.stream(keyNames).filter(key -> key.contains("営業時間")).findFirst().orElse("営業時間");
        String holidayKey = Arrays.stream(keyNames).filter(key -> key.contains("定休日")).findFirst().orElse("定休日");
        return new StampSet(JSONStampSet.optString("名前"), JSONStampSet.optString("所在地"), JSONStampSet.optString("難易度"), JSONStampSet.optString(openHoursKey), JSONStampSet.optString(holidayKey), JSONStampSet.optString("入場料（大人一般）"), stamps);
    }

    /**
     *
     * @return {@link String} containing all the details of the {@link StampSet}
     */
    @NotNull
    @Override
    public String toString(){
        String name = "Name: " + this.name + "\n";
        String address = "Address: " + this.address + "\n";
        String difficulty = "Difficulty: " + this.difficulty + "\n";
        String openHours = "Open Hours: " + this.openHours + "\n";
        String holiday = "Holiday: " + this.holiday + "\n";
        String entreeFee = "Entry Fee: " + this.entryFee;
        StringBuilder stamps = new StringBuilder();
        int i = 1;
        for (Stamp stamp : this){
            stamps.append("\n");
            String title = "Stamp " + i++ + "\n";
            String stampInfo = stamp.toString();
            stamps.append(title).append(stampInfo);
        }
        return name + address + difficulty + openHours + holiday + entreeFee + stamps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StampSet that = (StampSet) o;
        return this.name.equals(that.name) && this.address.equals(that.address);
    }

    @Override
    public int hashCode() {
        int value;
        value = name.hashCode() + address.hashCode();
        return value;
    }
}
