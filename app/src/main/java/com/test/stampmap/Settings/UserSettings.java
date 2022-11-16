package com.test.stampmap.Settings;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import com.test.stampmap.Activity.MainActivity;

import java.util.Map;

public class UserSettings extends Application {
    private static final String PREFERENCES = "preferences";

    private static UserSettings instance;

    public static void setUserSettings(UserSettings app){
        instance = app;
    }

    public static <T> void setConfigValue(ConfigValue configValue, T value){
        SharedPreferences sharedPrefs = instance.getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        Map<String, ?> prefs = sharedPrefs.getAll();
        if (prefs.get(configValue.value) != null && prefs.get(configValue.value).getClass() != value.getClass()) return;
        if (value instanceof Boolean) sharedPrefs.edit().putBoolean(configValue.value, (boolean)value).apply();
        else if (value instanceof String) sharedPrefs.edit().putString(configValue.value, (String)value).apply();
        else if (value instanceof Integer) sharedPrefs.edit().putInt(configValue.value, (int)value).apply();
        else if (value instanceof Float) sharedPrefs.edit().putFloat(configValue.value, (float)value).apply();
        else if (value instanceof Long) sharedPrefs.edit().putLong(configValue.value, (long)value).apply();
    }

    public static <T> T getConfigValue(ConfigValue configValue){
        SharedPreferences sharedPrefs = instance.getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        if (sharedPrefs.getAll().get(configValue.value) == null) setConfigValue(configValue, configValue.defaultValue);
        return (T) sharedPrefs.getAll().get(configValue.value);
    }
}
