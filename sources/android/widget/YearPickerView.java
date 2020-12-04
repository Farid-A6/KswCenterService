package android.widget;

import android.content.Context;
import android.content.res.Resources;
import android.icu.util.Calendar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.AbsListView;
import android.widget.AdapterView;
import com.android.internal.R;

class YearPickerView extends ListView {
    /* access modifiers changed from: private */
    public final YearAdapter mAdapter;
    private final int mChildSize;
    /* access modifiers changed from: private */
    public OnYearSelectedListener mOnYearSelectedListener;
    private final int mViewSize;

    public interface OnYearSelectedListener {
        void onYearChanged(YearPickerView yearPickerView, int i);
    }

    public YearPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 16842868);
    }

    public YearPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public YearPickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setLayoutParams(new AbsListView.LayoutParams(-1, -2));
        Resources res = context.getResources();
        this.mViewSize = res.getDimensionPixelOffset(R.dimen.datepicker_view_animator_height);
        this.mChildSize = res.getDimensionPixelOffset(R.dimen.datepicker_year_label_height);
        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                int year = YearPickerView.this.mAdapter.getYearForPosition(position);
                YearPickerView.this.mAdapter.setSelection(year);
                if (YearPickerView.this.mOnYearSelectedListener != null) {
                    YearPickerView.this.mOnYearSelectedListener.onYearChanged(YearPickerView.this, year);
                }
            }
        });
        this.mAdapter = new YearAdapter(getContext());
        setAdapter((ListAdapter) this.mAdapter);
    }

    public void setOnYearSelectedListener(OnYearSelectedListener listener) {
        this.mOnYearSelectedListener = listener;
    }

    public void setYear(final int year) {
        this.mAdapter.setSelection(year);
        post(new Runnable() {
            public void run() {
                int position = YearPickerView.this.mAdapter.getPositionForYear(year);
                if (position >= 0 && position < YearPickerView.this.getCount()) {
                    YearPickerView.this.setSelectionCentered(position);
                }
            }
        });
    }

    public void setSelectionCentered(int position) {
        setSelectionFromTop(position, (this.mViewSize / 2) - (this.mChildSize / 2));
    }

    public void setRange(Calendar min, Calendar max) {
        this.mAdapter.setRange(min, max);
    }

    private static class YearAdapter extends BaseAdapter {
        private static final int ITEM_LAYOUT = 17367342;
        private static final int ITEM_TEXT_ACTIVATED_APPEARANCE = 16974768;
        private static final int ITEM_TEXT_APPEARANCE = 16974767;
        private int mActivatedYear;
        private int mCount;
        private final LayoutInflater mInflater;
        private int mMinYear;

        public YearAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        public void setRange(Calendar minDate, Calendar maxDate) {
            int minYear = minDate.get(1);
            int count = (maxDate.get(1) - minYear) + 1;
            if (this.mMinYear != minYear || this.mCount != count) {
                this.mMinYear = minYear;
                this.mCount = count;
                notifyDataSetInvalidated();
            }
        }

        public boolean setSelection(int year) {
            if (this.mActivatedYear == year) {
                return false;
            }
            this.mActivatedYear = year;
            notifyDataSetChanged();
            return true;
        }

        public int getCount() {
            return this.mCount;
        }

        public Integer getItem(int position) {
            return Integer.valueOf(getYearForPosition(position));
        }

        public long getItemId(int position) {
            return (long) getYearForPosition(position);
        }

        public int getPositionForYear(int year) {
            return year - this.mMinYear;
        }

        public int getYearForPosition(int position) {
            return this.mMinYear + position;
        }

        public boolean hasStableIds() {
            return true;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            TextView v;
            int textAppearanceResId;
            boolean activated = true;
            boolean hasNewView = convertView == null;
            if (hasNewView) {
                v = (TextView) this.mInflater.inflate(17367342, parent, false);
            } else {
                v = (TextView) convertView;
            }
            int year = getYearForPosition(position);
            if (this.mActivatedYear != year) {
                activated = false;
            }
            if (hasNewView || v.isActivated() != activated) {
                if (activated) {
                    textAppearanceResId = 16974768;
                } else {
                    textAppearanceResId = 16974767;
                }
                v.setTextAppearance(textAppearanceResId);
                v.setActivated(activated);
            }
            v.setText((CharSequence) Integer.toString(year));
            return v;
        }

        public int getItemViewType(int position) {
            return 0;
        }

        public int getViewTypeCount() {
            return 1;
        }

        public boolean isEmpty() {
            return false;
        }

        public boolean areAllItemsEnabled() {
            return true;
        }

        public boolean isEnabled(int position) {
            return true;
        }
    }

    public int getFirstPositionOffset() {
        View firstChild = getChildAt(0);
        if (firstChild == null) {
            return 0;
        }
        return firstChild.getTop();
    }

    public void onInitializeAccessibilityEventInternal(AccessibilityEvent event) {
        super.onInitializeAccessibilityEventInternal(event);
        if (event.getEventType() == 4096) {
            event.setFromIndex(0);
            event.setToIndex(0);
        }
    }
}
