package com.haritha_kh.crosswordsinhala.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PuzzleDao {
    @Query("SELECT * FROM puzzles")
    LiveData<List<PuzzleEntity>> getAll();

}
