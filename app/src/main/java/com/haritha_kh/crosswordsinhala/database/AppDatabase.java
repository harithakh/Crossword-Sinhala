package com.haritha_kh.crosswordsinhala.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {PuzzleEntity.class}, version = 1)
//version number should be updated when the Room Database schema is changed, such as adding new tables.
public abstract class AppDatabase extends RoomDatabase{

    public abstract PuzzleDao puzzleDao();

    private static volatile AppDatabase instance;

    public static AppDatabase getInstance(Context context){
        if (instance == null){
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "crossword_app")
                            .createFromAsset("crossword_app.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }

        }
        return instance;
    }
}
