package com.test.stampmap.Settings;

public enum ConfigValue {
    /**
     * a config value for whether the filters should be automatically cleared after a search
     */
    CLEAR_FILTERS("clearFilters", Boolean.class);

    public final String value;
    public final Class<?> clazz;

    ConfigValue(String value, Class<?> clazz) {
        this.value = value;
        this.clazz = clazz;
    }
}
