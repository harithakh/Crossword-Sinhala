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

    @ColumnInfo(name = "row")
    public int row;

    @ColumnInfo(name = "col")
    public int col;

    @ColumnInfo(name = "letter")
    public String letter;

    @ColumnInfo(name = "index_number")
    public int indexNumber;

    @ColumnInfo(name = "hint_letter", defaultValue = "0")
    public int hintLetter;

    // Constructor
    public PuzzleEntity(int puzzleId, int row, int col, String letter, int indexNumber, int hintLetter) {
        this.puzzleId = puzzleId;
        this.row = row;
        this.col = col;
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
}
