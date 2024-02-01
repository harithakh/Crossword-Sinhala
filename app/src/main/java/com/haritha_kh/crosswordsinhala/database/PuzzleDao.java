package com.haritha_kh.crosswordsinhala.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PuzzleDao {
    @Query("SELECT * FROM puzzles WHERE puzzle_id = :id")
    LiveData<List<PuzzleEntity>> getPuzzleById(int id);

}

//@Dao
//public interface PuzzleDao {
//    @Query("SELECT * FROM puzzles WHERE puzzleId = :id")
//    LiveData<List<PuzzleEntity>> getPuzzleById(int id);
//}