package com.android.internal.widget;

import android.annotation.UnsupportedAppUsage;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.TextView;

public class TextViewInputDisabler {
    private InputFilter[] mDefaultFilters;
    private InputFilter[] mNoInputFilters = {new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            return "";
        }
    }};
    private TextView mTextView;

    @UnsupportedAppUsage
    public TextViewInputDisabler(TextView textView) {
        this.mTextView = textView;
        this.mDefaultFilters = this.mTextView.getFilters();
    }

    @UnsupportedAppUsage
    public void setInputEnabled(boolean enabled) {
        this.mTextView.setFilters(enabled ? this.mDefaultFilters : this.mNoInputFilters);
    }
}
