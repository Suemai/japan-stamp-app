package com.test.stampmap.Settings;

public enum ConfigValue {
    // declare config values here
    CLEAR_FILTERS(true);


    private final String name;
    private final Object defaultValue;

    ConfigValue(Object defaultValue) {
        this.name = this.name();
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public <T> T getValue(){
        return UserSettings.getConfigValue(this);
    }

    public <T> void setValue(T value){
        UserSettings.setConfigValue(this, value);
    }
}
