package com.haritha_kh.crosswordsinhala;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.haritha_kh.crosswordsinhala.database.AppDatabase;
import com.haritha_kh.crosswordsinhala.database.PuzzleDao;
import com.haritha_kh.crosswordsinhala.database.PuzzleEntity;

import java.util.List;

public class SolvePuzzle extends AppCompatActivity{

    private int puzzleNumber;
    private LiveData<List<PuzzleEntity>> allData;

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

        AppDatabase db = AppDatabase.getInstance(this);
        allData = db.puzzleDao().getAll();

        // Observe allData LiveData for UI updates
        allData.observe(this, new Observer<List<PuzzleEntity>>() {
            @Override
            public void onChanged(List<PuzzleEntity> appEntityList) {
                // Update UI with the new data
                Log.d("Data from database", "check it working " + allData.getValue().get(0).letter);
            }
        });


    }

}