package com.test.stampmap.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import com.test.stampmap.Activity.MainActivity;
import com.test.stampmap.R;
import com.test.stampmap.Settings.SupportedLocale;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class LanguageSelectAdapter extends ArrayAdapter<CharSequence> {

    public final List<SupportedLocale> locales;
    public final OnSelectedListener onSelectedListener;
    public boolean firstActivation = true;

    private static @NonNull Resources getLocalizedResources(Context context, Locale desiredLocale) {
        Configuration conf = context.getResources().getConfiguration();
        conf = new Configuration(conf);
        conf.setLocale(desiredLocale);
        Context localizedContext = context.createConfigurationContext(conf);
        return localizedContext.getResources();
    }

    private static List<CharSequence> createLocaleStringList(@NonNull Context context, List<SupportedLocale> locales) {
        List<CharSequence> langs = locales.stream().map(SupportedLocale::getName).collect(Collectors.toList());
        langs.set(0, String.format("%s (%s)", Locale.getDefault().getDisplayLanguage(), getLocalizedResources(context, Locale.getDefault()).getString(R.string.sys_lang)));
        return langs;
    }

    private LanguageSelectAdapter(@NonNull Context context, int resource, @NonNull List<SupportedLocale> locales) {
        super(context, resource, createLocaleStringList(context, locales));
        this.locales = locales;
        onSelectedListener = new OnSelectedListener(this);
    }

    public LanguageSelectAdapter(@NonNull Context context, int resource) {
        this(context, resource, Arrays.stream(SupportedLocale.values().clone()).collect(Collectors.toList()));
    }

    public static class OnSelectedListener implements AdapterView.OnItemSelectedListener {
        private final LanguageSelectAdapter adapter;

        OnSelectedListener(LanguageSelectAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (adapter.firstActivation) adapter.firstActivation = false;
            else MainActivity.setLocale((Activity)view.getContext(), adapter.locales.get(position));
        }
        public void onNothingSelected(AdapterView<?> parent) {}
    }
}
