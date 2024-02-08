package com.haritha_kh.crosswordsinhala;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haritha_kh.crosswordsinhala.database.AppDatabase;
import com.haritha_kh.crosswordsinhala.database.ClueEntity;
import com.haritha_kh.crosswordsinhala.database.PuzzleEntity;

import java.util.ArrayList;
import java.util.List;

public class SolvePuzzle extends AppCompatActivity{

    private int puzzleNumber;
    private int levelNumber;
    private LiveData<List<PuzzleEntity>> lettersData;
    private LiveData<List<ClueEntity>> cluesData;
    TextView[][] indexNumbers = new TextView[7][7];
    EditText[][] letterEditTexts = new EditText[7][7];
    String[][] userInput = new String[7][7];

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
        levelNumber = puzzleNumber;

        TextView puzzleNumberTextview = findViewById(R.id.puzzle_number_textview);
        puzzleNumberTextview.setText(String.valueOf(puzzleNumber));

        AppDatabase db = AppDatabase.getInstance(this);
        lettersData = db.puzzleDao().getPuzzleById(1); // the puzzle id that passes from the menu must come here. puzzleNumber
        cluesData = db.clueDao().getClueById(1);


        observeOnce(lettersData, this, newValue -> {
//            tvr1c1.setText(newValue.get(0).getLetter());
            setLetters(newValue);
            Log.d("Data from database", "check it working " + newValue.get(0).getLetter());
        });

        observeOnce(cluesData, this, newValue -> {
            setClues(newValue);
            Log.d("Data from database", "check it working " + newValue.get(0).getClue());
        });

    }

    @Override
    protected void onPause(){
        super.onPause();

        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("completedLevels", levelNumber);
        editor.apply();
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

    //set the initial letters of the puzzle and checking answers
    private void setLetters(List<PuzzleEntity> box){

        Button checkAnswers = findViewById(R.id.check_answers_button);

        // making editText references
        for(int i=0; i<7; i++){
            for (int j = 0; j < 7; j++){
                String editTextId = "r" + (i + 1) + "c" + (j + 1);
                int resID = getResources().getIdentifier(editTextId, "id", getPackageName());
                letterEditTexts[i][j] = findViewById(resID);
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
        makePuzzle.puzzleMaker(letterEditTexts, indexNumbers, box);

        //if all the answers are correct, should be able to go the next puzzle
        checkAnswers.setOnClickListener(new View.OnClickListener() {
            private boolean isLevelCompleted;
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 7; i++) {
                    for (int j = 0; j < 7; j++) {
                        String letterDatabase = box.get((i*7)+j).getLetter(); //letter from db
                        if(letterDatabase!=null){
                            userInput[i][j] = letterEditTexts[i][j].getText().toString();
                            letterEditTexts[i][j].setBackgroundColor(Color.WHITE);
                            if(!(letterEditTexts[i][j].getText().toString().equals(letterDatabase))){
                                //set editText red if the user answer is wrong
                                letterEditTexts[i][j].setBackgroundColor(Color.RED);
                            }else{
                                isLevelCompleted = true;
                            }
                        }
                    }
                }
                //if all the answers are correct,level number increases by 1
                if (isLevelCompleted) {
                    levelNumber += 1;
                    letterEditTexts[0][0].setText(String.valueOf(levelNumber));
                }
            }
        });

    }

    //set the clues to the textViews.
    private void setClues(List<ClueEntity> clues){

        List<TextView> cluesAcross = new ArrayList<>();
        List<TextView> cluesDown = new ArrayList<>();

        Button buttonAcross = findViewById(R.id.across_button);
        Button buttonDown = findViewById(R.id.down_button);

        ImageView directionIcon = findViewById(R.id.direction_image_view);

        LinearLayout cluesContainer = findViewById(R.id.clues_container);

        // put data that comes from the database to cluesAcross and cluesDown Lists.
        for(int i=0; i<clues.size(); i++){
            TextView textView = new TextView(this);
            String displayText = clues.get(i).getIndexNumber()+ ") " + clues.get(i).getClue();
            textView.setText(displayText);
            textView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            textView.setTextColor(Color.BLACK);
            cluesContainer.addView(textView);

            if(clues.get(i).getOrientation()==0){
                textView.setVisibility(View.VISIBLE);
                cluesAcross.add(textView);
            } else {
                textView.setVisibility(View.GONE);
                cluesDown.add(textView);
            }
        }

        buttonAcross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directionIcon.setImageResource(R.drawable.icon_stick_right);
//                buttonDown.setBackgroundColor(Color.GRAY);
//                buttonAcross.setBackgroundColor(Color.MAGENTA);
                for(TextView clue: cluesDown){
                    clue.setVisibility(View.GONE);
                }
                for(TextView clue: cluesAcross){
                    clue.setVisibility(View.VISIBLE);
                }
            }
        });

        buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directionIcon.setImageResource(R.drawable.icon_stick_down);
                for(TextView clue: cluesAcross){
                    clue.setVisibility(View.GONE);
                }
                for(TextView clue: cluesDown){
                    clue.setVisibility(View.VISIBLE);
                }
            }
        });


    }

}