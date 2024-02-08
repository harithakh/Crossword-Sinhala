package com.haritha_kh.crosswordsinhala.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "clues")
public class ClueEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private final int id;

    @ColumnInfo(name = "puzzle_id")
    public int puzzleId;

    @ColumnInfo(name = "orientation")
    private final int orientation;

    @ColumnInfo(name = "index_number")
    private final int indexNumber;

    @ColumnInfo(name = "clue")
    private final String clue;

    public ClueEntity(int id, int puzzleId, int orientation, int indexNumber, String clue) {
        this.id = id;
        this.puzzleId = puzzleId;
        this.orientation = orientation;
        this.indexNumber = indexNumber;
        this.clue = clue;
    }

    public int getId() {
        return id;
    }

    public int getOrientation() {
        return orientation;
    }

    public String getClue() {
        return clue;
    }

    public int getIndexNumber() {
        return indexNumber;
    }


}
