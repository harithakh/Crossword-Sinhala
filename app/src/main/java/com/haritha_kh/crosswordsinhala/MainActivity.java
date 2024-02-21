package com.haritha_kh.crosswordsinhala;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.gridlayout.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final List<Button> buttonList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        int nextLevel = sharedPreferences.getInt("next_level", 1);

        makeButtons(nextLevel,60); //total must update

    }


    @Override
    protected void onResume() {
        super.onResume();
        // when a level completed and user comes back to this page, the active buttons will be updated.
        int nextLevel = sharedPreferences.getInt("next_level", 1);

        TextView textView = findViewById(R.id.sharedFCheck); //test
        textView.setText(String.valueOf(nextLevel));

        for (Button button : buttonList){
            int buttonNumber = buttonList.indexOf(button) + 1;
            if (buttonNumber <= nextLevel){
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, SolvePuzzle.class)
                                .putExtra("puzzle_number",buttonNumber));
                    };
                });
                button.setEnabled(true);
            }
        }
    }

    private void makeButtons(int currentLevel, int totalPuzzles){
        int numCols = 3;
        GridLayout gridLayout = findViewById(R.id.menuButtonsGrid);

        gridLayout.setColumnCount(numCols);
        int numRows = (int) Math.ceil((double) totalPuzzles / numCols);
        gridLayout.setRowCount(numRows);

        int buttonStyle = R.style.menu_button_style;

        for (int i=1; i<totalPuzzles+1; i++){
            int puzzleNumber = i;
            Button button = new Button(this);
            button.setText(String.valueOf(i));
            button.setHeight(250);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
//            params.width = 0; // Set width to 0 to let GridLayout manage the width
//            params.height = 0;
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f); // Set row weight to 1
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);

            if (puzzleNumber <= currentLevel) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, SolvePuzzle.class)
                                .putExtra("puzzle_number",puzzleNumber));
                    };
                });
            } else {
                button.setEnabled(false);
            }
            gridLayout.addView(button, params);

            buttonList.add(button);

        }
    }
}