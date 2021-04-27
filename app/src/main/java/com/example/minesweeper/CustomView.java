package com.example.minesweeper;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomView extends View {

    private TextPaint mTextPaint;
    private Paint rectPaint;
    ArrayList<Cell> cells = new ArrayList<>(100);

    Rect square;

    public CustomView(Context context) {
        super(context);
        init(null, 0);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        setBackgroundColor(Color.WHITE);
        for (int i = 0; i < 100; i++)
            cells.add(new Cell());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

        int contentWidth = getWidth() - paddingLeft - paddingRight;

        int rectBounds = contentWidth / 10;

        int sideLength = rectBounds - 10;

        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectPaint.setColor(Color.BLACK);

        square = new Rect(10, 10, sideLength, sideLength);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                canvas.save();
                canvas.translate(j * rectBounds, i * rectBounds);
                if (!cells.get(i * 10 + j).isCovered())
                    rectPaint.setColor(Color.GRAY);
                else {
                    if (cells.get(i * 10 + j).isMined())
                        rectPaint.setColor(Color.RED);
                    else
                        rectPaint.setColor(Color.BLACK);
                }
                canvas.drawRect(square, rectPaint);
                canvas.restore();
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {

        int eventAction = event.getAction();

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int caseSize = contentWidth / 10;

        int x = (int)event.getX();
        int y = (int)event.getY();
        int row = y / caseSize;
        int col = x / caseSize;

        if (eventAction == MotionEvent.ACTION_DOWN)
            cells.get(10 * row + col).setCovered(false);

        // tell the View to redraw the Canvas
        invalidate();

        // tell the View that we handled the event
        return true;

    }
}
