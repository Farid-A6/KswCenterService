package android.widget;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;

public class EditText extends TextView {
    public EditText(Context context) {
        this(context, (AttributeSet) null);
    }

    public EditText(Context context, AttributeSet attrs) {
        this(context, attrs, 16842862);
    }

    public EditText(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public EditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean getFreezesText() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean getDefaultEditable() {
        return true;
    }

    /* access modifiers changed from: protected */
    public MovementMethod getDefaultMovementMethod() {
        return ArrowKeyMovementMethod.getInstance();
    }

    public Editable getText() {
        CharSequence text = super.getText();
        if (text == null) {
            return null;
        }
        if (text instanceof Editable) {
            return (Editable) super.getText();
        }
        super.setText(text, TextView.BufferType.EDITABLE);
        return (Editable) super.getText();
    }

    public void setText(CharSequence text, TextView.BufferType type) {
        super.setText(text, TextView.BufferType.EDITABLE);
    }

    public void setSelection(int start, int stop) {
        Selection.setSelection(getText(), start, stop);
    }

    public void setSelection(int index) {
        Selection.setSelection(getText(), index);
    }

    public void selectAll() {
        Selection.selectAll(getText());
    }

    public void extendSelection(int index) {
        Selection.extendSelection(getText(), index);
    }

    public void setEllipsize(TextUtils.TruncateAt ellipsis) {
        if (ellipsis != TextUtils.TruncateAt.MARQUEE) {
            super.setEllipsize(ellipsis);
            return;
        }
        throw new IllegalArgumentException("EditText cannot use the ellipsize mode TextUtils.TruncateAt.MARQUEE");
    }

    public CharSequence getAccessibilityClassName() {
        return EditText.class.getName();
    }

    /* access modifiers changed from: protected */
    public boolean supportsAutoSizeText() {
        return false;
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        if (isEnabled()) {
            info.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_TEXT);
        }
    }
}
