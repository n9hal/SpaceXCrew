package com.example.spacexcrew.CrewDB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Crew.class,version = 1)
public abstract class CrewDB extends RoomDatabase {
    private static CrewDB INSTANCE;
    public abstract DAO dao();

    static CrewDB getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (Crew.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CrewDB.class, "Crew")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
