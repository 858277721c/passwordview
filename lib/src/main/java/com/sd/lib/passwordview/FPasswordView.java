package com.sd.lib.passwordview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
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
    private int mItemWidth;
    private Drawable mItemDivider;
    private int mItemBackground;
    private boolean mItemSquare;

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
        int itemWidth = 0;
        Drawable itemDivider = null;
        int itemBackground = 0;
        boolean itemSquare = false;
        String passwordPlaceholder = getResources().getString(R.string.lib_passwordview_password_placeholder);

        if (attrs != null)
        {
            final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LibPasswordView);

            itemCount = a.getInteger(R.styleable.LibPasswordView_pvItemCount, itemCount);
            itemTextColor = a.getColor(R.styleable.LibPasswordView_pvItemTextColor, itemTextColor);
            itemTextSize = a.getDimensionPixelSize(R.styleable.LibPasswordView_pvItemTextSize, itemTextSize);
            itemMargin = a.getDimensionPixelSize(R.styleable.LibPasswordView_pvItemMargin, itemMargin);
            itemWidth = a.getDimensionPixelSize(R.styleable.LibPasswordView_pvItemWidth, itemWidth);
            itemSquare = a.getBoolean(R.styleable.LibPasswordView_pvItemSquare, itemSquare);

            if (a.hasValue(R.styleable.LibPasswordView_pvItemBackground))
                itemBackground = a.getResourceId(R.styleable.LibPasswordView_pvItemBackground, 0);

            if (a.hasValue(R.styleable.LibPasswordView_pvItemDivider))
                itemDivider = a.getDrawable(R.styleable.LibPasswordView_pvItemDivider);

            if (a.hasValue(R.styleable.LibPasswordView_pvPasswordPlaceholder))
                passwordPlaceholder = a.getString(R.styleable.LibPasswordView_pvPasswordPlaceholder);

            a.recycle();
        }

        mItemTextColor = itemTextColor;
        mItemTextSize = itemTextSize;
        mItemMargin = itemMargin;
        mItemWidth = itemWidth;
        mItemDivider = itemDivider;
        mItemBackground = itemBackground;
        mItemSquare = itemSquare;
        mPasswordPlaceholder = passwordPlaceholder;

        setItemCount(itemCount);
    }

    /**
     * 返回item的数量
     *
     * @return
     */
    public int getItemCount()
    {
        return mItemCount;
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
            mItemCount = count;

            mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(count)});
            mEditText.setText("");

            mLinearLayout.removeAllViews();
            mLinearLayout.setDividerDrawable(mItemDivider);

            for (int i = 0; i < count; i++)
            {
                final TextView textView = new InternalTextView(getContext());
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(mItemTextColor);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mItemTextSize);
                textView.setBackgroundResource(mItemBackground);

                final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth, ViewGroup.LayoutParams.MATCH_PARENT);
                params.weight = mItemWidth == 0 ? 1 : 0;

                if (i > 0)
                    params.leftMargin = mItemMargin;

                mLinearLayout.addView(textView, params);
            }

            bindText(mEditText.getText().toString());
        }
    }

    private void bindText(String content)
    {
        boolean selected = false;

        final int count = mLinearLayout.getChildCount();
        for (int i = 0; i < count; i++)
        {
            String itemText = "";
            if (i < content.length())
                itemText = TextUtils.isEmpty(mPasswordPlaceholder) ? String.valueOf(content.charAt(i)) : mPasswordPlaceholder;

            final TextView child = (TextView) mLinearLayout.getChildAt(i);
            child.setText(itemText);

            if (itemText.isEmpty() && !selected)
            {
                child.setSelected(true);
                selected = true;
            } else
            {
                child.setSelected(false);
            }
        }
    }

    private final class InternalTextView extends TextView
    {
        public InternalTextView(Context context)
        {
            super(context);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
        {
            if (mItemSquare)
                heightMeasureSpec = widthMeasureSpec;

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        @Override
        public void setSelected(boolean selected)
        {
            if (isSelected() != selected)
                super.setSelected(selected);
        }
    }

    private final class InternalEditText extends EditText
    {
        private String mLastText = "";

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

            final String newText = text.toString();
            if (!newText.equals(mLastText))
            {
                mLastText = newText;

                bindText(newText);
                if (mCallback != null)
                    mCallback.onTextChanged(newText);
            }
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
