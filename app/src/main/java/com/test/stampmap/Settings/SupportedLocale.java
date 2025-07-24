package com.test.stampmap.Settings;

import java.util.Arrays;
import java.util.Locale;

public enum SupportedLocale {
    DEFAULT("Default", Locale.ROOT),
    ENGLISH("English", Locale.UK),
    JAPANESE("日本語", Locale.JAPANESE),
    CHINESE_SIMPLIFIED("中文（简体）", Locale.SIMPLIFIED_CHINESE);

    private final Locale locale;
    private final String name;

    SupportedLocale(String name, Locale locale) {
        this.name = name;
        this.locale = locale;
    }

    public String getName() {
        return name;
    }

    public Locale getLocale() {
        return locale;
    }

    public static SupportedLocale getCurrent() {
        return SupportedLocale.valueOf(ConfigValue.APP_LOCALE.getValue());
    }

    public static SupportedLocale getSupportedLocaleFor(Locale locale) {
        return Arrays.stream(SupportedLocale.values()).filter(loc -> {
            if (loc.getLocale().equals(locale)) return true;
            if (!loc.getLocale().getLanguage().equals(locale.getLanguage())) return false;
            if (locale.getLanguage().equals("zh")) {
                boolean isSimplified1 = locale.getCountry().isEmpty() || locale.getCountry().equals("CN");
                boolean isSimplified2 = loc.getLocale().getCountry().isEmpty() || loc.getLocale().getCountry().equals("CN");
                return isSimplified1 == isSimplified2;
            }
            return true;
        }).findFirst().orElse(SupportedLocale.ENGLISH);
    }

    public static boolean supportsLocate(Locale locale) {
        return Arrays.stream(SupportedLocale.values()).anyMatch(loc -> loc.getLocale().getLanguage().equals(locale.getLanguage()));
    }
}
