package com.sd.lib.passwordview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class FPasswordView extends EditText
{
    private int mItemCount;
    private int mItemMargin;
    private int mItemBackgroundResource;
    private Drawable mItemBackgroundDrawable;

    private String mPasswordPlaceholder;

    public FPasswordView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs)
    {
        setBackgroundColor(0);
        super.setPadding(0, 0, 0, 0);
        super.setGravity(Gravity.CENTER);

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

    @Override
    public void setPadding(int left, int top, int right, int bottom)
    {
    }

    @Override
    public void setGravity(int gravity)
    {
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
            if (!getText().toString().isEmpty())
                throw new RuntimeException("Count can not be change when text is not empty");

            mItemCount = count;
            setFilters(new InputFilter[]{new InputFilter.LengthFilter(count)});
            invalidate();
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
            invalidate();
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

            try
            {
                mItemBackgroundDrawable = getResources().getDrawable(resId);
            } catch (Exception e)
            {
                mItemBackgroundDrawable = null;
            }

            invalidate();
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

    @Override
    protected void onDraw(Canvas canvas)
    {
        if (getWidth() <= 0 || getHeight() <= 0)
            return;

        final int itemWidth = (getWidth() - (mItemCount - 1) * mItemMargin) / mItemCount;
        if (itemWidth <= 0)
            return;

        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = getHeight();

        final String text = getText().toString();
        for (int i = 0; i < mItemCount; i++)
        {
            if (i > 0)
                left += mItemMargin;

            right = left + itemWidth;

            onDrawItemBackground(canvas, left, top, right, bottom);

            if (i < text.length())
            {
                final String textItem = TextUtils.isEmpty(mPasswordPlaceholder) ? String.valueOf(text.charAt(i)) : mPasswordPlaceholder;
                final float textItemWidth = getPaint().measureText(textItem);

                final float textX = left + ((itemWidth - textItemWidth) / 2);
                canvas.drawText(String.valueOf(textItem), textX, getBaseline(), getPaint());
            }

            left = right;
        }
    }

    protected void onDrawItemBackground(Canvas canvas, int left, int top, int right, int bottom)
    {
        if (mItemBackgroundDrawable != null)
        {
            mItemBackgroundDrawable.setBounds(left, top, right, bottom);
            mItemBackgroundDrawable.draw(canvas);
        }
    }
}
