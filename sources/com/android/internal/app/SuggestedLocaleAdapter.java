package com.android.internal.app;

import android.content.Context;
import android.content.res.Configuration;
import android.net.wifi.WifiEnterpriseConfig;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import com.android.internal.R;
import com.android.internal.app.LocaleHelper;
import com.android.internal.app.LocaleStore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

public class SuggestedLocaleAdapter extends BaseAdapter implements Filterable {
    private static final int MIN_REGIONS_FOR_SUGGESTIONS = 6;
    private static final int TYPE_HEADER_ALL_OTHERS = 1;
    private static final int TYPE_HEADER_SUGGESTED = 0;
    private static final int TYPE_LOCALE = 2;
    private Context mContextOverride = null;
    private final boolean mCountryMode;
    private Locale mDisplayLocale = null;
    private LayoutInflater mInflater;
    /* access modifiers changed from: private */
    public ArrayList<LocaleStore.LocaleInfo> mLocaleOptions;
    /* access modifiers changed from: private */
    public ArrayList<LocaleStore.LocaleInfo> mOriginalLocaleOptions;
    /* access modifiers changed from: private */
    public int mSuggestionCount;

    static /* synthetic */ int access$208(SuggestedLocaleAdapter x0) {
        int i = x0.mSuggestionCount;
        x0.mSuggestionCount = i + 1;
        return i;
    }

    public SuggestedLocaleAdapter(Set<LocaleStore.LocaleInfo> localeOptions, boolean countryMode) {
        this.mCountryMode = countryMode;
        this.mLocaleOptions = new ArrayList<>(localeOptions.size());
        for (LocaleStore.LocaleInfo li : localeOptions) {
            if (li.isSuggested()) {
                this.mSuggestionCount++;
            }
            this.mLocaleOptions.add(li);
        }
    }

    public boolean areAllItemsEnabled() {
        return false;
    }

    public boolean isEnabled(int position) {
        return getItemViewType(position) == 2;
    }

    public int getItemViewType(int position) {
        if (!showHeaders()) {
            return 2;
        }
        if (position == 0) {
            return 0;
        }
        if (position == this.mSuggestionCount + 1) {
            return 1;
        }
        return 2;
    }

    public int getViewTypeCount() {
        if (showHeaders()) {
            return 3;
        }
        return 1;
    }

    public int getCount() {
        if (showHeaders()) {
            return this.mLocaleOptions.size() + 2;
        }
        return this.mLocaleOptions.size();
    }

    public Object getItem(int position) {
        int offset = 0;
        if (showHeaders()) {
            offset = position > this.mSuggestionCount ? -2 : -1;
        }
        return this.mLocaleOptions.get(position + offset);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public void setDisplayLocale(Context context, Locale locale) {
        if (locale == null) {
            this.mDisplayLocale = null;
            this.mContextOverride = null;
        } else if (!locale.equals(this.mDisplayLocale)) {
            this.mDisplayLocale = locale;
            Configuration configOverride = new Configuration();
            configOverride.setLocale(locale);
            this.mContextOverride = context.createConfigurationContext(configOverride);
        }
    }

    private void setTextTo(TextView textView, int resId) {
        if (this.mContextOverride == null) {
            textView.setText(resId);
        } else {
            textView.setText(this.mContextOverride.getText(resId));
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        int i;
        if (convertView == null && this.mInflater == null) {
            this.mInflater = LayoutInflater.from(parent.getContext());
        }
        int itemType = getItemViewType(position);
        switch (itemType) {
            case 0:
            case 1:
                if (!(convertView instanceof TextView)) {
                    convertView = this.mInflater.inflate((int) R.layout.language_picker_section_header, parent, false);
                }
                TextView textView = (TextView) convertView;
                if (itemType == 0) {
                    setTextTo(textView, R.string.language_picker_section_suggested);
                } else if (this.mCountryMode) {
                    setTextTo(textView, R.string.region_picker_section_all);
                } else {
                    setTextTo(textView, R.string.language_picker_section_all);
                }
                textView.setTextLocale(this.mDisplayLocale != null ? this.mDisplayLocale : Locale.getDefault());
                break;
            default:
                if (!(convertView instanceof ViewGroup)) {
                    convertView = this.mInflater.inflate((int) R.layout.language_picker_item, parent, false);
                }
                TextView text = (TextView) convertView.findViewById(R.id.locale);
                LocaleStore.LocaleInfo item = (LocaleStore.LocaleInfo) getItem(position);
                text.setText((CharSequence) item.getLabel(this.mCountryMode));
                text.setTextLocale(item.getLocale());
                text.setContentDescription(item.getContentDescription(this.mCountryMode));
                if (this.mCountryMode) {
                    int layoutDir = TextUtils.getLayoutDirectionFromLocale(item.getParent());
                    convertView.setLayoutDirection(layoutDir);
                    if (layoutDir == 1) {
                        i = 4;
                    } else {
                        i = 3;
                    }
                    text.setTextDirection(i);
                    break;
                }
                break;
        }
        return convertView;
    }

    private boolean showHeaders() {
        if ((this.mCountryMode && this.mLocaleOptions.size() < 6) || this.mSuggestionCount == 0 || this.mSuggestionCount == this.mLocaleOptions.size()) {
            return false;
        }
        return true;
    }

    public void sort(LocaleHelper.LocaleInfoComparator comp) {
        Collections.sort(this.mLocaleOptions, comp);
    }

    class FilterByNativeAndUiNames extends Filter {
        FilterByNativeAndUiNames() {
        }

        /* access modifiers changed from: protected */
        public Filter.FilterResults performFiltering(CharSequence prefix) {
            Filter.FilterResults results = new Filter.FilterResults();
            if (SuggestedLocaleAdapter.this.mOriginalLocaleOptions == null) {
                ArrayList unused = SuggestedLocaleAdapter.this.mOriginalLocaleOptions = new ArrayList(SuggestedLocaleAdapter.this.mLocaleOptions);
            }
            ArrayList<LocaleStore.LocaleInfo> values = new ArrayList<>(SuggestedLocaleAdapter.this.mOriginalLocaleOptions);
            if (prefix == null || prefix.length() == 0) {
                results.values = values;
                results.count = values.size();
            } else {
                Locale locale = Locale.getDefault();
                String prefixString = LocaleHelper.normalizeForSearch(prefix.toString(), locale);
                int count = values.size();
                ArrayList<LocaleStore.LocaleInfo> newValues = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    LocaleStore.LocaleInfo value = values.get(i);
                    String nameToCheck = LocaleHelper.normalizeForSearch(value.getFullNameInUiLanguage(), locale);
                    if (wordMatches(LocaleHelper.normalizeForSearch(value.getFullNameNative(), locale), prefixString) || wordMatches(nameToCheck, prefixString)) {
                        newValues.add(value);
                    }
                }
                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        /* access modifiers changed from: package-private */
        public boolean wordMatches(String valueText, String prefixString) {
            if (valueText.startsWith(prefixString)) {
                return true;
            }
            for (String word : valueText.split(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER)) {
                if (word.startsWith(prefixString)) {
                    return true;
                }
            }
            return false;
        }

        /* access modifiers changed from: protected */
        public void publishResults(CharSequence constraint, Filter.FilterResults results) {
            ArrayList unused = SuggestedLocaleAdapter.this.mLocaleOptions = (ArrayList) results.values;
            int unused2 = SuggestedLocaleAdapter.this.mSuggestionCount = 0;
            Iterator it = SuggestedLocaleAdapter.this.mLocaleOptions.iterator();
            while (it.hasNext()) {
                if (((LocaleStore.LocaleInfo) it.next()).isSuggested()) {
                    SuggestedLocaleAdapter.access$208(SuggestedLocaleAdapter.this);
                }
            }
            if (results.count > 0) {
                SuggestedLocaleAdapter.this.notifyDataSetChanged();
            } else {
                SuggestedLocaleAdapter.this.notifyDataSetInvalidated();
            }
        }
    }

    public Filter getFilter() {
        return new FilterByNativeAndUiNames();
    }
}
