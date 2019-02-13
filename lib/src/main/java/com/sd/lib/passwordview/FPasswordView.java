package com.sd.lib.passwordview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
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
        int itemMargin = (int) (getResources().getDisplayMetrics().density * 10);
        int itemBackgroundResource = R.drawable.lib_passwordview_bg_item;
        String passwordPlaceholder = "●";

        if (attrs != null)
        {
            final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LibPasswordView);

            itemCount = a.getInteger(R.styleable.LibPasswordView_pvItemCount, itemCount);
            itemMargin = a.getDimensionPixelSize(R.styleable.LibPasswordView_pvItemMargin, itemMargin);
            itemBackgroundResource = a.getResourceId(R.styleable.LibPasswordView_pvItemBackground, itemBackgroundResource);

            if (a.hasValue(R.styleable.LibPasswordView_pvPasswordPlaceholder))
                passwordPlaceholder = a.getString(R.styleable.LibPasswordView_pvPasswordPlaceholder);

            a.recycle();
        }

        setItemCount(itemCount);
        setItemMargin(itemMargin);
        setItemBackgroundResource(itemBackgroundResource);
        setPasswordPlaceholder(passwordPlaceholder);
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
                final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
                mLinearLayout.addView(textView, params);
            }
        }
    }

    /**
     * 设置密码item之间的间距
     *
     * @param margin
     */
    public void setItemMargin(int margin)
    {
        if (margin <= 0)
            throw new IllegalArgumentException();

        if (mItemMargin != margin)
        {
            mItemMargin = margin;

            final int count = mLinearLayout.getChildCount();
            for (int i = 1; i < count; i++)
            {
                final View child = mLinearLayout.getChildAt(i);
                final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) child.getLayoutParams();
                params.leftMargin = margin;
                child.setLayoutParams(params);
            }
        }
    }

    /**
     * 设置item背景
     *
     * @param resId
     */
    public void setItemBackgroundResource(int resId)
    {
        if (mItemBackgroundResource != resId)
        {
            mItemBackgroundResource = resId;

            final int count = mLinearLayout.getChildCount();
            for (int i = 0; i < count; i++)
            {
                final View child = mLinearLayout.getChildAt(i);
                child.setBackgroundResource(resId);
            }
        }
    }

    /**
     * 设置密码占位符，如果为null或者空字符串，则显示明文
     *
     * @param placeholder
     */
    public void setPasswordPlaceholder(String placeholder)
    {
        if (placeholder == null)
            placeholder = "";

        if (!placeholder.equals(mPasswordPlaceholder))
        {
            mPasswordPlaceholder = placeholder;
            invalidate();
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

            final String content = text.toString();

            final int count = mLinearLayout.getChildCount();
            for (int i = 0; i < count; i++)
            {
                String itemText = "";
                if (i < content.length())
                    itemText = TextUtils.isEmpty(mPasswordPlaceholder) ? String.valueOf(content.charAt(i)) : mPasswordPlaceholder;

                final TextView child = (TextView) mLinearLayout.getChildAt(i);
                child.setText(itemText);
            }

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
