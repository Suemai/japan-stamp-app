package com.test.stampmap.Settings;

import android.app.Application;
import android.content.SharedPreferences;

import java.util.Map;

public class UserSettings extends Application {
    private static final String PREFERENCES = "preferences";

    private static UserSettings instance;

    public static void setUserSettings(UserSettings app){
        instance = app;
    }

    protected static <T> void setConfigValue(ConfigValue configValue, T value){
        SharedPreferences sharedPrefs = instance.getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        Map<String, ?> prefs = sharedPrefs.getAll();
        if (prefs.get(configValue.getName()) != null && prefs.get(configValue.getName()).getClass() != value.getClass()) return;
        if (value instanceof Boolean) sharedPrefs.edit().putBoolean(configValue.getName(), (boolean)value).apply();
        else if (value instanceof String) sharedPrefs.edit().putString(configValue.getName(), (String)value).apply();
        else if (value instanceof Integer) sharedPrefs.edit().putInt(configValue.getName(), (int)value).apply();
        else if (value instanceof Float) sharedPrefs.edit().putFloat(configValue.getName(), (float)value).apply();
        else if (value instanceof Long) sharedPrefs.edit().putLong(configValue.getName(), (long)value).apply();
    }

    protected static <T> T getConfigValue(ConfigValue configValue){
        SharedPreferences sharedPrefs = instance.getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        if (sharedPrefs.getAll().get(configValue.getName()) == null) setConfigValue(configValue, configValue.getDefaultValue());
        return (T) sharedPrefs.getAll().get(configValue.getName());
    }
}
