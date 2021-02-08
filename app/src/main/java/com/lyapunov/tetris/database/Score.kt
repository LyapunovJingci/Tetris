package com.lyapunov.tetris.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Score(
        @ColumnInfo(name = "score_detail") val score: Int?
) {
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}
