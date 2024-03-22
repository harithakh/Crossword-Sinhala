package com.haritha_kh.crosswordsinhala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.gridlayout.widget.GridLayout;

import android.widget.ProgressBar;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private final List<Button> buttonList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        int nextLevel = sharedPreferences.getInt("next_level", 1);

        // progress circle
        progressBar = findViewById(R.id.progress_circular);

        Window window = getWindow(); 
        // Set the status bar color
        window.setStatusBarColor(getColor(R.color.dark_blue));

        // tool bar
        Toolbar toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
        // Hide the title
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // don't call this method on onResume.
        makeButtons(nextLevel,25); //total must update

        ScrollView scrollView = findViewById(R.id.activity_main_scroll_view);

        // button corresponding to the completed level is displayed at the top.
        scrollView.post(() -> scrollView.scrollTo(0,buttonList.get(nextLevel-1).getTop()));
    }


    @Override
    protected void onResume() {
        super.onResume();
        // when a level completed and user comes back to this page, the active buttons will be updated.
        int nextLevel = sharedPreferences.getInt("next_level", 1);

//        TextView textView = findViewById(R.id.sharedFCheck); //test
//        textView.setText(String.valueOf(nextLevel));

        for (Button button : buttonList){
            int buttonNumber = buttonList.indexOf(button) + 1;
            if (buttonNumber <= nextLevel){
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        progressBar.setVisibility(View.VISIBLE); // hide progressBar circle

                        startActivity(new Intent(MainActivity.this, SolvePuzzle.class)
                                .putExtra("puzzle_number",buttonNumber));
                    };
                });
                button.setText(String.valueOf(buttonNumber));
                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 48);
                button.setTextColor(ContextCompat.getColor(this, R.color.gold));
                button.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.light_blue));
                button.setEnabled(true);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // hide the progress circle.
        progressBar.setVisibility(View.GONE);
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

            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f); // Set row weight to 1
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);

            if (puzzleNumber <= currentLevel) {
                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 48);
                button.setTextColor(ContextCompat.getColor(this, R.color.gold));
                button.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.light_blue));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, SolvePuzzle.class)
                                .putExtra("puzzle_number",puzzleNumber));
                    };
                });
            } else {
                //locked buttons
                button.setTextColor(ContextCompat.getColor(this, R.color.cream));
                button.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.lighter_blue));

                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
                String textTemp = i + "\uD83D\uDD12";
                button.setText(textTemp);
//                button.setForeground(ContextCompat.getDrawable(this, R.drawable.lock_icon));
                button.setEnabled(false);
            }
            gridLayout.addView(button, params);

            buttonList.add(button);

        }
    }

    // inflate menu resource file
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.three_dot_menu, menu);
        return true;
    }

    // to handle clicks on three dots menu items.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_help){
            Intent intent = new Intent(this, InfoActivity.class);
            intent.putExtra("intKey", 1);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_about) {
            Intent intent = new Intent(this, InfoActivity.class);
            intent.putExtra("intKey", 2);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}