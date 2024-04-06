package com.haritha_kh.crosswordsinhala.utils;

import android.graphics.Color;
import android.widget.EditText;
import android.widget.TextView;

import com.haritha_kh.crosswordsinhala.database.PuzzleEntity;

import java.util.List;

public class MakePuzzle {

    public void puzzleMaker(EditText[][] editTexts, TextView[][] textViews, List<PuzzleEntity> box){

//      placing data taken from the database to the EditTexts and the TextViews.
        for(int i=0; i<7; i++){
            for(int j=0; j<7; j++){
                String letter = box.get((i*7)+j).getLetter();
                int index = box.get((i*7)+j).getIndexNumber();
                if(letter==null){
                    // set relevant black boxes
                    editTexts[i][j].setBackgroundColor(Color.BLACK);
                    editTexts[i][j].setEnabled(false);
                }
                if(box.get((i*7)+j).getHintLetter()==1){
                    editTexts[i][j].setText(letter);
                    editTexts[i][j].setEnabled(false);
                }
                if(index!=0){
                    textViews[i][j].setText(String.valueOf(index));
                }
            }
        }

    }
}
