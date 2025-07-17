package com.test.stampmap.Settings;

public enum SupportedLocale {
    DEFAULT("Default", ""),
    ENGLISH("English", "en"),
    JAPANESE("日本語", "ja"),
    CHINESE_SIMPLIFIED("中文（简体）", "zh_CN");

    final String code;
    final String name;

    SupportedLocale(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public static SupportedLocale getCurrent() {
        return SupportedLocale.valueOf(ConfigValue.APP_LOCALE.getValue());
    }
}
