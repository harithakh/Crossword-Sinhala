package com.haritha_kh.crosswordsinhala.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "puzzles")
public class PuzzleEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "puzzle_id") //@ColumnInfo annotation in Room is optional
    public int puzzleId;

    @ColumnInfo(name = "box_number")
    public int boxNumber;

    @ColumnInfo(name = "letter")
    private final String letter;

    @ColumnInfo(name = "index_number")
    private final int indexNumber;

    @ColumnInfo(name = "hint_letter")
    private final int hintLetter;

    // Constructor
    public PuzzleEntity(int puzzleId, int boxNumber, String letter, int indexNumber, int hintLetter) {
        this.puzzleId = puzzleId;
        this.boxNumber = boxNumber;
        this.letter = letter;
        this.indexNumber = indexNumber;
        this.hintLetter = hintLetter;
    }

    // getters, setters can be added as needed later


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getLetter() {
        return letter;
    }

    public int getHintLetter() {
        return hintLetter;
    }


    public int getIndexNumber() {
        return indexNumber;
    }

}
