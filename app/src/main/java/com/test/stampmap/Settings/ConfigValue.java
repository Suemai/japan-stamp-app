package com.test.stampmap.Settings;

public enum ConfigValue {
    // declare config values here
    CLEAR_FILTERS(true);


    public final String name;
    public final Object defaultValue;

    ConfigValue(Object defaultValue) {
        this.name = this.name();
        this.defaultValue = defaultValue;
    }
}
