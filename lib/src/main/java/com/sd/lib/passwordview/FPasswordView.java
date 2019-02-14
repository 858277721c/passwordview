package com.sd.lib.passwordview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FPasswordView extends FrameLayout
{
    private final EditText mEditText;
    private final LinearLayout mLinearLayout;

    private int mItemCount;

    private int mItemTextColor;
    private int mItemTextSize;
    private int mItemMargin;
    private Drawable mItemDivider;
    private Drawable mItemBackground;

    private String mPasswordPlaceholder;

    private Callback mCallback;

    public FPasswordView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        mLinearLayout = new LinearLayout(context);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        addView(mLinearLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mEditText = new InternalEditText(context);
        addView(mEditText, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        int itemCount = getResources().getInteger(R.integer.lib_passwordview_item_count);
        int itemTextColor = getResources().getColor(R.color.lib_passwordview_text_item);
        int itemTextSize = getResources().getDimensionPixelSize(R.dimen.lib_passwordview_text_item);
        int itemMargin = 0;
        Drawable itemDivider = null;
        Drawable itemBackground = null;
        String passwordPlaceholder = getResources().getString(R.string.lib_passwordview_password_placeholder);

        if (attrs != null)
        {
            final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LibPasswordView);

            itemCount = a.getInteger(R.styleable.LibPasswordView_pvItemCount, itemCount);
            itemTextColor = a.getColor(R.styleable.LibPasswordView_pvItemTextColor, itemTextColor);
            itemTextSize = a.getDimensionPixelSize(R.styleable.LibPasswordView_pvItemTextSize, itemTextSize);
            itemMargin = a.getDimensionPixelSize(R.styleable.LibPasswordView_pvItemMargin, itemMargin);

            if (a.hasValue(R.styleable.LibPasswordView_pvItemBackground))
                itemBackground = a.getDrawable(R.styleable.LibPasswordView_pvItemBackground);

            if (a.hasValue(R.styleable.LibPasswordView_pvItemDivider))
                itemDivider = a.getDrawable(R.styleable.LibPasswordView_pvItemDivider);

            if (a.hasValue(R.styleable.LibPasswordView_pvPasswordPlaceholder))
                passwordPlaceholder = a.getString(R.styleable.LibPasswordView_pvPasswordPlaceholder);

            a.recycle();
        }

        mItemTextColor = itemTextColor;
        mItemTextSize = itemTextSize;
        mItemMargin = itemMargin;
        mItemDivider = itemDivider;
        mItemBackground = itemBackground;
        mPasswordPlaceholder = passwordPlaceholder;

        setItemCount(itemCount);
    }

    /**
     * 设置回调
     *
     * @param callback
     */
    public void setCallback(Callback callback)
    {
        mCallback = callback;
    }

    /**
     * 设置密码长度
     *
     * @param count
     */
    public void setItemCount(int count)
    {
        if (count <= 0)
            throw new IllegalArgumentException();

        if (mItemCount != count)
        {
            if (!mEditText.getText().toString().isEmpty())
                throw new RuntimeException("Count can not be change when text is not empty");

            mItemCount = count;
            mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(count)});

            mLinearLayout.removeAllViews();
            mLinearLayout.setDividerDrawable(mItemDivider);

            for (int i = 0; i < count; i++)
            {
                final TextView textView = new InternalTextView(getContext());
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(mItemTextColor);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mItemTextSize);

                if (Build.VERSION.SDK_INT >= 16)
                    textView.setBackground(mItemBackground);
                else
                    textView.setBackgroundDrawable(mItemBackground);

                final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
                if (i > 0)
                    params.leftMargin = mItemMargin;

                mLinearLayout.addView(textView, params);
            }

            bindText(mEditText.getText().toString());
        }
    }

    private void bindText(String content)
    {
        final int count = mLinearLayout.getChildCount();
        for (int i = 0; i < count; i++)
        {
            String itemText = "";
            if (i < content.length())
                itemText = TextUtils.isEmpty(mPasswordPlaceholder) ? String.valueOf(content.charAt(i)) : mPasswordPlaceholder;

            final TextView child = (TextView) mLinearLayout.getChildAt(i);
            child.setText(itemText);
        }
    }

    private final class InternalTextView extends TextView
    {
        public InternalTextView(Context context)
        {
            super(context);
        }
    }

    private final class InternalEditText extends EditText
    {
        public InternalEditText(Context context)
        {
            super(context);
            setBackgroundColor(0);
            setPadding(0, 0, 0, 0);
            setGravity(Gravity.CENTER);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event)
        {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {
                final InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (!manager.isActive(this))
                {
                    setFocusable(true);
                    requestFocus();
                    manager.showSoftInput(this, InputMethodManager.SHOW_FORCED);
                }
            }
            return false;
        }

        @Override
        protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter)
        {
            super.onTextChanged(text, start, lengthBefore, lengthAfter);
            bindText(text.toString());
            if (mCallback != null)
                mCallback.onTextChanged(text.toString());
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
        }
    }

    public interface Callback
    {
        void onTextChanged(String text);
    }
}
