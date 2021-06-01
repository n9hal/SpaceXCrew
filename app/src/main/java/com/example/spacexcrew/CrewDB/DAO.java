package com.example.spacexcrew.CrewDB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DAO {
    @Insert
    public void crewInsertion(Crew crew);

    @Query("SELECT * FROM crew")
    List<Crew> getAllCrew();

    @Query("DELETE FROM crew")
    public void deleteAll();

    @Query("SELECT COUNT(*) FROM crew")
    public int checkCrew();
}
