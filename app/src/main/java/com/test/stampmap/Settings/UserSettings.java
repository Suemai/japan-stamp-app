package com.test.stampmap.Settings;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import com.test.stampmap.R;

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
        Map<String, ?> prefs = sharedPrefs.getAll();
        Object val = prefs.get(configValue.getName());
        if (val == null || !prefs.get(configValue.getName()).getClass().equals(configValue.getClazz())) {
            sharedPrefs.edit().remove(configValue.getName()).apply();
            setConfigValue(configValue, configValue.getDefaultValue());
            return (T)configValue.getDefaultValue();
        }
        return (T)val;

    }

    public static int getStatusBarHeight() {
        int resourceId = instance.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return instance.getResources().getDimensionPixelSize(resourceId);
    }

    public static void setStatusBarUI(Activity activity, boolean forceBlack){
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        if (activity.getWindow().getStatusBarColor() == instance.getColor(R.color.transparent_white) || forceBlack)
            uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        activity.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    }
}
