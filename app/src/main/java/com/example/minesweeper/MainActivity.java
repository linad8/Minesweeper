package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public Button resetButton;
    public Button modeButton;
    CustomView customView;

    TextView nbMines;
    TextView nbMarked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customView = findViewById(R.id.minesweeper);

        resetButton = findViewById(R.id.resetButton);
        modeButton = findViewById(R.id.modeButton);
        nbMarked = findViewById(R.id.nbMarked);
        nbMines = findViewById(R.id.nbMines);
    }

    // onClick method of resetButton
    public void resetGame(View view) {
        customView.resetGame();
    }

    // onClick method of modeButton
    public void switchMode(View view) {
        if (customView.isUncoveredMode())
            modeButton.setText("Uncovered Mode");
        else
            modeButton.setText("Marked Mode");
        customView.switchMode();
    }
}