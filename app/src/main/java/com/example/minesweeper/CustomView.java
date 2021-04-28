package com.example.minesweeper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;

import java.util.ArrayList;
import java.util.Random;

public class CustomView extends View {

    private TextPaint mTextPaint;
    private Paint black;
    private Paint gray;
    private Paint red;

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
        int count = 0;
        while (count < 20)
        {
            Random rand = new Random();
            int x = rand.nextInt(10);
            int y = rand.nextInt(10);
            if (!cells.get(y * 10 + x).isMined())
            {
                cells.get(y * 10 + x).setMined(true);
                count++;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

        int contentWidth = getWidth() - paddingLeft - paddingRight;

        int squareSize = contentWidth / 10;

        int sideLength = squareSize - 10;

        // setting colors
        black = new Paint();
        black.setColor(Color.BLACK);
        gray = new Paint();
        gray.setColor(Color.GRAY);
        red = new Paint();
        red.setColor(Color.RED);

        square = new Rect(10, 10, sideLength, sideLength);


        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                canvas.save();
                canvas.translate(j * squareSize, i * squareSize);
                if (cells.get(i * 10 + j).isCovered())
                {
                    canvas.drawRect(square, black);
                }
                else {
                    if (cells.get(i * 10 + j).isMined())
                    {
                        canvas.drawRect(square, red);
                        black.setTextSize(80);
                        canvas.drawText("M", 20, 80, black);
                    }
                    else
                        canvas.drawRect(square, gray);
                }
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

        if (cells.get(10 * row + col).isCovered() && eventAction == MotionEvent.ACTION_DOWN)
            cells.get(10 * row + col).setCovered(false);

        invalidate();
        return true;
    }
}
