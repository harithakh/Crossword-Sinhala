package com.haritha_kh.crosswordsinhala;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class SolvePuzzle extends AppCompatActivity {

    private int puzzleNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solve_puzzle);

        // Hiding action bar
        if(getSupportActionBar()!=null){
            this.getSupportActionBar().hide();
        }

        puzzleNumber = getIntent().getIntExtra("puzzle_number",0);
        TextView puzzleNumberTextview = findViewById(R.id.puzzle_number_textview);
        puzzleNumberTextview.setText(String.valueOf(puzzleNumber));
    }
}