package com.test.stampmap;

import android.util.Log;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class StampSet implements Iterable<Stamp>{
    private final String name, address, difficulty, openHours, holiday;
    private final List<Stamp> stamps;

    public StampSet(String name, String address, String difficulty, String openHours, String holiday, List<Stamp> stamps) {
        this.name = name;
        this.address = address;
        this.difficulty = difficulty;
        this.openHours = !Objects.equals(openHours, "") ? openHours : "未確認";
        this.holiday = !Objects.equals(holiday, "") ? holiday : "未確認";
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

    public List<Stamp> getStamps(){
        return this.stamps;
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
        return new StampSet(JSONStampSet.optString("名前"), JSONStampSet.optString("所在地"), JSONStampSet.optString("難易度"), JSONStampSet.optString(openHoursKey), JSONStampSet.optString(holidayKey), stamps);
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
        String holiday = "Holiday: " + this.holiday;
        StringBuilder stamps = new StringBuilder();
        int i = 1;
        for (Stamp stamp : this){
            stamps.append("\n");
            String title = "Stamp " + i++ + "\n";
            String stampInfo = stamp.toString();
            stamps.append(title).append(stampInfo);
        }
        return name + address + difficulty + openHours + holiday + stamps;
    }
}
