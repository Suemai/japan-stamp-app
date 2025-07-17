package com.test.stampmap.Settings;

public enum ConfigValue {
    // declare config values here
    CLEAR_FILTERS(true),
    APP_LOCALE(SupportedLocale.DEFAULT.name());

    private final String name;
    private final Object defaultValue;
    private final Class<?> clazz;

    <T> ConfigValue(T defaultValue) {
        this.name = this.name();
        this.defaultValue = defaultValue;
        this.clazz = defaultValue.getClass();
    }

    public String getName() {
        return name;
    }
    public Class<?> getClazz() {
        return clazz;
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
