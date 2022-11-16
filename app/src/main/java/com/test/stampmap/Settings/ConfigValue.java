package com.test.stampmap.Settings;

public enum ConfigValue {
    // declare config values here
    CLEAR_FILTERS("clearFilters", true);


    public final String value;
    public final Class<?> clazz;
    public final Object defaultValue;

    ConfigValue(String name, Object defaultValue) {
        this.value = name;
        this.defaultValue = defaultValue;

        if (defaultValue instanceof Boolean) this.clazz = Boolean.class;
        else if (defaultValue instanceof Integer) this.clazz = Integer.class;
        else if (defaultValue instanceof Float) this.clazz = Float.class;
        else if (defaultValue instanceof Long) this.clazz = Long.class;
        else this.clazz = defaultValue.getClass().getDeclaringClass();
    }
}
