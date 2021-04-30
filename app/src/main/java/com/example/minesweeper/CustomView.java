package com.example.minesweeper;

import android.content.Context;
import android.os.Debug;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class CustomView<Mode> extends View {

    // colors
    private Paint black;
    private Paint gray;
    private Paint red;
    private Paint yellow;

    TextView marked;
    int nbMarked;

    boolean lost;

    // array of cells
    ArrayList<Cell> cells;

    // mode of the game
    boolean uncoveredMode;

    Rect square;

    public CustomView(Context context) {
        super(context);
        InitGame();
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        InitGame();
    }

    public CustomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        InitGame();
    }

    // init the game
    public void InitGame() {

        lost = false;
        uncoveredMode = true;
        nbMarked = 0;

        // setting background
        setBackgroundColor(Color.WHITE);

        // filling the cell array
        cells = new ArrayList<>(100);
        for (int i = 0; i < 100; i++)
            cells.add(new Cell());

        // placing the 20 mines
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

    // returns the number of adjacent mines of the cell at i, j
    public int getAdjacentMines(int i, int j) {

        int nb = 0;
        if (i > 0 && j > 0 && cells.get((i - 1) * 10 + j - 1).isMined())
            nb++;
        if (i > 0 && cells.get((i - 1) * 10 + j).isMined())
            nb++;
        if (i > 0 && j < 9 && cells.get((i - 1) * 10 + j + 1).isMined())
            nb++;
        if (j > 0 && cells.get(i * 10 + j - 1).isMined())
            nb++;
        if (j < 9 && cells.get(i * 10 + j + 1).isMined())
            nb++;
        if (i < 9 && j > 0 && cells.get((i + 1) * 10 + j - 1).isMined())
            nb++;
        if (i < 9 && cells.get((i + 1) * 10 + j).isMined())
            nb++;
        if (j < 9 && i < 9 && cells.get((i + 1) * 10 + j + 1).isMined())
            nb++;
        return nb;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // getting the dimensions
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
        yellow = new Paint();
        yellow.setColor(Color.YELLOW);

        square = new Rect(10, 10, sideLength, sideLength);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                canvas.save();
                canvas.translate(j * squareSize, i * squareSize);

                // case of a marked cell
                if (cells.get(i * 10 + j).isMarked())
                    canvas.drawRect(square, yellow);
                else {
                    // case of a covered cell
                    if (cells.get(i * 10 + j).isCovered())
                        canvas.drawRect(square, black);
                    else {
                        // case of a mined cell
                        if (cells.get(i * 10 + j).isMined()) {
                            canvas.drawRect(square, red);
                            black.setTextSize(squareSize * 0.8f);
                            canvas.drawText("M", squareSize * 0.2f, squareSize * 0.8f, black);
                        } else
                            // case of an empty cell
                            {
                                canvas.drawRect(square, gray);
                                String s = String.valueOf(getAdjacentMines(i, j));
                                black.setTextSize(100);
                                canvas.drawText(s, squareSize * 0.2f, squareSize * 0.8f, black);
                            }
                    }
                }
                canvas.restore();
            }
        }

        // if the game is lost
        if (lost)
        {
            for (int y = 0; y < 10; y++)
            {
                for (int x = 0; x < 10; x++) {
                    canvas.save();
                    canvas.translate(x * squareSize, y * squareSize);
                    // marking each mined cell in red
                    if (cells.get(y * 10 + x).isMined())
                    {
                        canvas.drawRect(square, red);
                        black.setTextSize(squareSize * 0.8f);
                        canvas.drawText("M", squareSize * 0.2f, squareSize * 0.8f, black);
                    }
                    canvas.restore();
                }
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {

        int eventAction = event.getAction();

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int caseSize = contentWidth / 10;

        // getting the right index in the matrix of cells
        int x = (int)event.getX();
        int y = (int)event.getY();
        int row = y / caseSize;
        int col = x / caseSize;

        // checking if user clicks
        if (10 * row + col < 100 && !lost && eventAction == MotionEvent.ACTION_DOWN)
        {
            if (uncoveredMode)
            {
                if (!cells.get(10 * row + col).isMarked())
                {
                    cells.get(10 * row + col).setCovered(false);
                    if (cells.get(10 * row + col).isMined())
                        lost = true;
                    if (!cells.get(10 * row + col).isMined() && !cells.get(10 * row + col).isCovered() && getAdjacentMines(row, col) == 0)
                        spread(row, col);
                }
            }
            else
            {
                if (cells.get(10 * row + col).isCovered()) {
                    if (cells.get(10 * row + col).isMarked())
                        nbMarked--;
                    else
                        nbMarked++;
                    cells.get(10 * row + col).setMarked();
                    marked.setText(String.valueOf(nbMarked));
                }
            }

        }
        invalidate();
        return true;
    }

    // reset the game
    public void resetGame() {
        InitGame();
        invalidate();
    }

    // switch the mode of the game
    public void switchMode() {
        uncoveredMode = !uncoveredMode;
    }

    public boolean isUncoveredMode() {return uncoveredMode;}

    // set uncovered the right cells around the cell at i, j
    public void spread(int i, int j) {
        cells.get(i * 10 + j).setCovered(false);
        if (i > 0 && j > 0 && cells.get((i - 1) * 10 + j - 1).isCovered())
        {
            cells.get((i - 1) * 10 + j - 1).setCovered(false);
            if (getAdjacentMines(i - 1, j - 1) == 0)
                spread(i - 1, j - 1);
        }
        if (i > 0 && cells.get((i - 1) * 10 + j).isCovered())
        {
            cells.get((i - 1) * 10 + j).setCovered(false);
            if (getAdjacentMines(i - 1, j) == 0)
                spread(i - 1, j);
        }
        if (i > 0 && j < 9 && cells.get((i - 1) * 10 + j + 1).isCovered())
        {
            cells.get((i - 1) * 10 + j + 1).setCovered(false);
            if (getAdjacentMines(i - 1, j + 1) == 0)
                spread(i - 1, j + 1);
        }
        if (j > 0 && cells.get(i * 10 + j - 1).isCovered())
        {
            cells.get(i * 10 + j - 1).setCovered(false);
            if (getAdjacentMines(i, j - 1) == 0)
                spread(i, j - 1);
        }
        if (j < 9 && cells.get(i * 10 + j + 1).isCovered())
        {
            cells.get(i * 10 + j + 1).setCovered(false);
            if (getAdjacentMines(i, j + 1) == 0)
                spread(i, j + 1);
        }
        if (i < 9 && j > 0 && cells.get((i + 1) * 10 + j - 1).isCovered())
        {
            cells.get((i + 1) * 10 + j - 1).setCovered(false);
            if (getAdjacentMines(i + 1, j - 1) == 0)
                spread(i + 1, j - 1);
        }
        if (i < 9 && cells.get((i + 1) * 10 + j).isCovered())
        {
            cells.get((i + 1) * 10 + j).setCovered(false);
            if (getAdjacentMines(i + 1, j) == 0)
                spread(i + 1, j);
        }
        if (j < 9 && i < 9 && cells.get((i + 1) * 10 + j + 1).isCovered())
        {
            cells.get((i + 1) * 10 + j + 1).setCovered(false);
            if (getAdjacentMines(i + 1, j + 1) == 0)
                spread(i + 1, j + 1);
        }
    }
}
