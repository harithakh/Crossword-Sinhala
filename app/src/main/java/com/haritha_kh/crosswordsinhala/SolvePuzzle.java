package com.haritha_kh.crosswordsinhala;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.haritha_kh.crosswordsinhala.database.AppDatabase;
import com.haritha_kh.crosswordsinhala.database.PuzzleEntity;

import java.util.List;

public class SolvePuzzle extends AppCompatActivity{

    private int puzzleNumber;
    private LiveData<List<PuzzleEntity>> allData;
    EditText[][] letters = new EditText[7][7];
    TextView[][] indexNumbers = new TextView[7][7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solve_puzzle);

        EditText tvr1c1 = findViewById(R.id.r1c1);
        // Hiding action bar
        if(getSupportActionBar()!=null){
            this.getSupportActionBar().hide();
        }

        puzzleNumber = getIntent().getIntExtra("puzzle_number",0);
        TextView puzzleNumberTextview = findViewById(R.id.puzzle_number_textview);
        puzzleNumberTextview.setText(String.valueOf(puzzleNumber));

        AppDatabase db = AppDatabase.getInstance(this);
        allData = db.puzzleDao().getPuzzleById(1); // the puzzle id that passes from the menu must come here. puzzleNumber

        observeOnce(allData, this, newValue -> {
            tvr1c1.setText(newValue.get(0).getLetter());
            setLetters(newValue);
            Log.d("Data from database", "check it working " + newValue.get(0).getLetter());
        });

    }

    // This method is for observing data once and then automatically removing the observer.
    // related to LiveData
    public static <T> void observeOnce(LiveData<T> liveData, LifecycleOwner owner, Observer<T> observer){
        liveData.observe(owner, new Observer<T>() {
            @Override
            public void onChanged(T t) {
                observer.onChanged(t);
                liveData.removeObserver(this);
            }
        });
    }

    private void setLetters(List<PuzzleEntity> box){

        // making editText references
        for(int i=0; i<7; i++){
            for (int j = 0; j < 7; j++){
                String editTextId = "r" + (i + 1) + "c" + (j + 1);
                int resID = getResources().getIdentifier(editTextId, "id", getPackageName());
                letters[i][j] = findViewById(resID);
            }
        }

        //making textView references
        for(int i=0; i<7; i++){
            for(int j=0; j<7; j++){
                String textViewId = "tiny_text_" + (i+1) + "_" + (j+1);
                int resID = getResources().getIdentifier(textViewId, "id", getPackageName());
                indexNumbers[i][j] = findViewById(resID);
            }
        }

        MakePuzzle makePuzzle = new MakePuzzle();

        makePuzzle.puzzleMaker(letters, indexNumbers, box);
    }

}