package com.example.minesweeper;

import android.content.Context;
import android.util.AttributeSet;
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

public class CustomView extends View {

    private TextPaint mTextPaint;
    private Paint rectPaint;

    Rect square;
    int sideLength;

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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        //Bounds of squares to be drawn.
        int rectBounds = contentWidth/10;

        //Side length of the square.
        int sideLength = rectBounds - 10 ;


        //Paint instance for drawing the squares.
        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectPaint.setColor(Color.BLACK);

        //Create a rect which is actually a square.
        square = new Rect(10,10, sideLength, sideLength);

        //Draw a row of squares.
        for(int i = 0; i< 10; i++) {
            for(int j = 0; j < 10; j++) {
                canvas.save();
                canvas.translate(j * rectBounds, i * rectBounds);
                canvas.drawRect(square, rectPaint);
                canvas.restore();
            }

        }
    }
}
