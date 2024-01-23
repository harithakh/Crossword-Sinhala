package com.haritha_kh.crosswordsinhala;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonClicked(View view) {
        //This method sends an intent to ThePuzzle class with tha puzzle number.
        //Puzzle number is taken from the puzzle number button id on the xml layout.

        //Taking the button id name as a string
        String buttonText = view.getResources().getResourceEntryName(view.getId());

        //last 3 letters are taken from the id
        String button_no_string = Character.toString(buttonText.charAt(2))
                + Character.toString(buttonText.charAt(3))
                + Character.toString(buttonText.charAt(4))
                + Character.toString(buttonText.charAt(5));

        //then that button_no_string converted into an int
        int buttonNumber = Integer.parseInt(button_no_string);

        startActivity(new Intent(MainActivity.this, SolvePuzzle.class)
                .putExtra("puzzle_number",buttonNumber));
    }
}