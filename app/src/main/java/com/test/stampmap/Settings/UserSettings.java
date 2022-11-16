package com.test.stampmap.Settings;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Map;

public class UserSettings extends Application {
    public static final String PREFERENCES = "preferences";

    public <T> void setConfigValue(ConfigValue configValue, T value){
        SharedPreferences sharedPrefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        Map<String, ?> prefs = sharedPrefs.getAll();
        if (prefs.get(configValue.value) != null && prefs.get(configValue.value).getClass() != value.getClass()) return;
        if (value instanceof Boolean) sharedPrefs.edit().putBoolean(configValue.value, (boolean)value).apply();
        else if (value instanceof String) sharedPrefs.edit().putString(configValue.value, (String)value).apply();
        else if (value instanceof Integer) sharedPrefs.edit().putInt(configValue.value, (int)value).apply();
    }

    public <T> T getConfigValue(ConfigValue configValue){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        Map<String, ?> prefs = sharedPreferences.getAll();
        T value = (T) prefs.get(configValue.value);
        if (value == null) {
            Log.i("StampApp", "VALUES DOES NOT EXIST... CREATING DEFAULT...");
            if (configValue.clazz.equals(Boolean.class)) {
                sharedPreferences.edit().putBoolean(configValue.value, true).apply();
            }
        }
        return (T) sharedPreferences.getAll().get(configValue.value);
    }
}
