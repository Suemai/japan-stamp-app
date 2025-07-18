package com.test.stampmap.Adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import com.test.stampmap.Settings.SupportedLocale;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class LanguageSelectAdapter extends ArrayAdapter<CharSequence> {

    public List<SupportedLocale> locales;

    private static List<CharSequence> createLocaleStringList(List<SupportedLocale> locales) {
        List<CharSequence> langs = locales.stream().map(SupportedLocale::getName).collect(Collectors.toList());
        langs.set(0, "Default (" + Locale.getDefault().getDisplayLanguage() + ")");
        return langs;
    }

    public LanguageSelectAdapter(@NonNull Context context, int resource, @NonNull List<SupportedLocale> locales) {
        super(context, resource, createLocaleStringList(locales));
        this.locales = locales;
    }
}
