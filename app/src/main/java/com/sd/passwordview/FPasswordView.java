package com.sd.passwordview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class FPasswordView extends EditText
{
    private int mItemCount;
    private int mItemMargin;
    private int mItemBackgroundResource;
    private Bitmap mItemBackground;

    public FPasswordView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs)
    {
        setBackgroundColor(0);
        setPadding(0, 0, 0, 0);

        mItemCount = 4;
        mItemMargin = (int) (getResources().getDisplayMetrics().density * 10);

        setFilters(new InputFilter[]{new InputFilter.LengthFilter(mItemCount)});
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom)
    {
        super.setPadding(0, 0, 0, 0);
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
                mItemBackground = BitmapFactory.decodeResource(getResources(), resId);
            } catch (Exception e)
            {
                mItemBackground = null;
            }

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

            if (mItemBackground != null)
            {
                canvas.drawBitmap(mItemBackground, null, new Rect(left, top, right, bottom), getPaint());
            }

            if (i < text.length())
            {
                final String textItem = String.valueOf(text.charAt(i));
                final float textItemWidth = getPaint().measureText(textItem);

                final float textX = left + ((itemWidth - textItemWidth) / 2);

                final float textY = getHeight() / 2 + (Math.abs(getPaint().getFontMetrics().ascent)) / 2;
                canvas.drawText(String.valueOf(textItem), textX, textY, getPaint());
            }

            left = right;
        }
    }
}
