package com.sd.passwordview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.widget.EditText;

public class FPasswordView extends EditText
{
    private int mItemCount;
    private int mItemMargin;

    private int mItemStrokeColor;
    private int mItemStrokeWidth;

    private final Paint mPaintStroke = new Paint(Paint.ANTI_ALIAS_FLAG);

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

        mItemStrokeColor = Color.parseColor("#dddddd");
        mItemStrokeWidth = (int) (getResources().getDisplayMetrics().density * 2);

        setFilters(new InputFilter[]{new InputFilter.LengthFilter(mItemCount)});
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom)
    {
        super.setPadding(0, 0, 0, 0);
    }

    /**
     * 设置密码长度
     *
     * @param count
     */
    public final void setItemCount(int count)
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
    public final void setItemMargin(int margin)
    {
        if (margin <= 0)
            throw new IllegalArgumentException();

        mItemMargin = margin;
        if (mItemMargin != margin)
        {
            mItemMargin = margin;
            invalidate();
        }
    }

    /**
     * 设置item边框大小
     *
     * @param width
     */
    public final void setItemStrokeWidth(int width)
    {
        if (width < 0)
            width = 0;

        if (mItemStrokeWidth != width)
        {
            mItemStrokeWidth = width;
            invalidate();
        }
    }

    public final void setItemStrokeColor(int color)
    {
        if (mItemStrokeColor != color)
        {
            mItemStrokeColor = color;
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

        mPaintStroke.setColor(mItemStrokeColor);
        mPaintStroke.setStrokeWidth(mItemStrokeWidth);

        int startX = 0;
        final int bottomY = getHeight();

        final String text = getText().toString();
        for (int i = 0; i < mItemCount; i++)
        {
            if (i > 0)
                startX += mItemMargin;

            final int endX = startX + itemWidth;
            canvas.drawLine(startX, bottomY, endX, bottomY, mPaintStroke);

            if (i < text.length())
            {
                final String textItem = String.valueOf(text.charAt(i));
                final float textItemWidth = getPaint().measureText(textItem);

                final float textX = startX + ((itemWidth - textItemWidth) / 2);
                final float textY = getBaseline();
                canvas.drawText(String.valueOf(textItem), textX, textY, getPaint());
            }

            startX = endX;
        }
    }
}
