package com.haritha_kh.crosswordsinhala.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ClueDao {

    @Query("SELECT * FROM clues WHERE puzzle_id = :id")
    LiveData<List<ClueEntity>> getClueById(int id);
}