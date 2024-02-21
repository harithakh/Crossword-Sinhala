package com.haritha_kh.crosswordsinhala;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.annotation.SuppressLint;
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
import com.haritha_kh.crosswordsinhala.utils.FormatString;
import com.haritha_kh.crosswordsinhala.utils.MakePuzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SolvePuzzle extends AppCompatActivity{

    private int puzzleNumber;
    private int nextLevel = 0;
    TextView[][] indexNumberTextViews = new TextView[7][7];
    EditText[][] letterEditTexts = new EditText[7][7];
    String[][] userInput = new String[7][7];
    FormatString formatString = new FormatString();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solve_puzzle);

        // Hiding action bar
        if(getSupportActionBar()!=null){
            this.getSupportActionBar().hide();
        }

        puzzleNumber = getIntent().getIntExtra("puzzle_number",0);

        // Initializing SharedPreferences
        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);

        TextView puzzleNumberTextview = findViewById(R.id.puzzle_number_textview);
        puzzleNumberTextview.setText(String.valueOf(puzzleNumber));

        // creating editText references
        for (int i=0; i<7; i++){
            for(int j=0; j<7; j++){
                String editTextId = "r" + (i + 1) + "c" + (j + 1);
                @SuppressLint("DiscouragedApi") int resID = getResources().getIdentifier(editTextId, "id", getPackageName());
                letterEditTexts[i][j] = findViewById(resID);
            }
        }

        AppDatabase db = AppDatabase.getInstance(this);
        LiveData<List<PuzzleEntity>> lettersData = db.puzzleDao().getPuzzleById(puzzleNumber);
        LiveData<List<ClueEntity>> cluesData = db.clueDao().getClueById(puzzleNumber);

        observeOnce(lettersData, this, newValue -> {
            setLetters(newValue);
        });

        observeOnce(cluesData, this, newValue -> {
            setClues(newValue);
//            Log.d("Data from database", "check it working " + newValue.get(0).getClue());
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        String userAnswersTemp = sharedPreferences.getString("user_puzzle_answers", "");
        int nextLevel = sharedPreferences.getInt("next_level", 1);
//        Log.d("Data from spfs", "check it working " +  userAnswersTemp);

//        //if the user opens another unlocked puzzle other than the last one,
        if(puzzleNumber == nextLevel && !userAnswersTemp.isEmpty()){
            String[] userAnswersTempArray = formatString.stringArrayMaker(userAnswersTemp);
//            Log.d("Data from spfs", "check it working " + Arrays.toString(userAnswersTempArray));

            for (int i=0; i<7; i++){
                for(int j=0; j<7; j++){
                    letterEditTexts[i][j].setText(userAnswersTempArray[(i * 7) + j]);
                }
            }

        }


    }

    @Override
    protected void onPause(){
        super.onPause();

        // getting the user inputs from the editTexts and putting them to userInput array
        for (int i=0; i<7; i++){
            for(int j=0; j<7; j++){
                userInput[i][j] = letterEditTexts[i][j].getText().toString().trim();
            }
        }

        //saving user answers to sharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("user_puzzle_answers", formatString.LinearStringMaker(userInput));
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

        //making textView references
        for(int i=0; i<7; i++){
            for(int j=0; j<7; j++){
                String textViewId = "tiny_text_" + (i+1) + "_" + (j+1);
                @SuppressLint("DiscouragedApi") int resID = getResources().getIdentifier(textViewId, "id", getPackageName());
                indexNumberTextViews[i][j] = findViewById(resID);
            }
        }

        MakePuzzle makePuzzle = new MakePuzzle();
        makePuzzle.puzzleMaker(letterEditTexts, indexNumberTextViews, box);

        //checking the answers. if all the answers are correct, can to go the next puzzle
        checkAnswers.setOnClickListener(new View.OnClickListener() {
            private boolean isLevelCompleted;
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 7; i++) {
                    for (int j = 0; j < 7; j++) {
                        String letterDatabase = box.get((i*7)+j).getLetter(); //letter from db

                        if(letterDatabase!=null){

                            String editTextInput = letterEditTexts[i][j].getText().toString().trim();
                            // userInput string must be used later to save data on SharedPreferences.
                            userInput[i][j] = editTextInput;

                            letterEditTexts[i][j].setBackgroundColor(Color.WHITE);

                            // check other_letters from the database
                            String[] otherLetters;
                            boolean isOtherLettersMatches = false;
                            if (box.get((i*7)+j).getOtherLetters() != null) {
                                otherLetters = box.get((i*7)+j).getOtherLetters().split(",");

                                //check if the answers matches with other letters
                                for(String str : otherLetters){
                                    if (editTextInput.equals(str)) {
                                        isOtherLettersMatches = true;
                                        break;
                                    }
                                }
                            }

                            if (editTextInput.equals(letterDatabase)){
                                isLevelCompleted = true;
                            } else if (isOtherLettersMatches){
                                letterEditTexts[i][j].setText(letterDatabase);
                                isLevelCompleted = true;
                            } else {
                                isLevelCompleted = false;
                                //set editText red if the user answer is wrong
                                letterEditTexts[i][j].setBackgroundColor(Color.RED);
                            }
                        }
                    }
                }
                //if all the answers are correct,level number increases by 1
                if (isLevelCompleted) {
                    nextLevel = puzzleNumber + 1;

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    // if all answers are correct, save an empty string to user answers on shared preferences
                    editor.putString("user_puzzle_answers","");

                    if (puzzleNumber == sharedPreferences.getInt("next_level", 1)){
                        editor.putInt("next_level", nextLevel);
                    }
                    editor.apply();
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

        buttonAcross.setOnClickListener(v -> {
            directionIcon.setImageResource(R.drawable.icon_stick_right);
//                buttonDown.setBackgroundColor(Color.GRAY);
//                buttonAcross.setBackgroundColor(Color.MAGENTA);
            for(TextView clue: cluesDown){
                clue.setVisibility(View.GONE);
            }
            for(TextView clue: cluesAcross){
                clue.setVisibility(View.VISIBLE);
            }
        });

        buttonDown.setOnClickListener(v -> {
            directionIcon.setImageResource(R.drawable.icon_stick_down);
            for(TextView clue: cluesAcross){
                clue.setVisibility(View.GONE);
            }
            for(TextView clue: cluesDown){
                clue.setVisibility(View.VISIBLE);
            }
        });


    }

}