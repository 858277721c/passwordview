package com.sd.lib.passwordview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
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
    private int mItemBackgroundResource;

    private String mPasswordPlaceholder;

    private Callback mCallback;

    public FPasswordView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        mLinearLayout = new LinearLayout(context);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.setGravity(Gravity.CENTER);
        addView(mLinearLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mEditText = new InternalEditText(context);
        addView(mEditText, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        int itemCount = 4;
        int itemTextColor = Color.BLACK;
        int itemTextSize = (int) (getResources().getDisplayMetrics().scaledDensity * 13);
        int itemMargin = (int) (getResources().getDisplayMetrics().density * 10);
        int itemBackgroundResource = R.drawable.lib_passwordview_bg_item;
        String passwordPlaceholder = "●";

        if (attrs != null)
        {
            final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LibPasswordView);

            itemCount = a.getInteger(R.styleable.LibPasswordView_pvItemCount, itemCount);
            itemTextColor = a.getColor(R.styleable.LibPasswordView_pvItemTextColor, itemTextColor);
            itemTextSize = a.getDimensionPixelSize(R.styleable.LibPasswordView_pvItemTextSize, itemTextSize);
            itemMargin = a.getDimensionPixelSize(R.styleable.LibPasswordView_pvItemMargin, itemMargin);
            itemBackgroundResource = a.getResourceId(R.styleable.LibPasswordView_pvItemBackground, itemBackgroundResource);

            if (a.hasValue(R.styleable.LibPasswordView_pvPasswordPlaceholder))
                passwordPlaceholder = a.getString(R.styleable.LibPasswordView_pvPasswordPlaceholder);

            a.recycle();
        }

        mItemTextColor = itemTextColor;
        mItemTextSize = itemTextSize;
        mItemMargin = itemMargin;
        mItemBackgroundResource = itemBackgroundResource;
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
            for (int i = 0; i < count; i++)
            {
                final TextView textView = new TextView(getContext());
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(mItemTextColor);
                textView.setTextSize(mItemTextSize);
                textView.setBackgroundResource(mItemBackgroundResource);

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
                setFocusable(true);
                requestFocus();
                manager.showSoftInput(this, InputMethodManager.SHOW_FORCED);
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
