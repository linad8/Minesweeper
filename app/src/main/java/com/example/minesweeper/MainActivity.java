package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public Button resetButton;
    public Button modeButton;

    CustomView customView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customView = findViewById(R.id.minesweeper);

        resetButton = findViewById(R.id.resetButton);
        modeButton = findViewById(R.id.modeButton);
        customView.marked = findViewById(R.id.nbMarked);
    }

    // onClick method of resetButton
    public void resetGame(View view) {
        customView.resetGame();
        customView.marked.setText("0");
    }

    // onClick method of modeButton
    public void switchMode(View view) {
        if (customView.isUncoveredMode()) {
            modeButton.setText("Uncovered Mode");
            modeButton.setTextColor(Color.WHITE);
            modeButton.setBackgroundColor(getResources().getColor(R.color.green));
        }
        else {
            modeButton.setText("Marked Mode");
            modeButton.setBackgroundColor(Color.YELLOW);
            modeButton.setTextColor(Color.BLACK);
        }
        customView.switchMode();
    }
}