package com.lyapunov.tetris.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScoreDao {
    @Query("SELECT * FROM score ORDER BY score_detail DESC LIMIT 10")
    suspend fun getTopTen(): List<Score>

    @Insert
    suspend fun insert(vararg score: Score)

    @Delete
    suspend fun delete(score: Score)
}