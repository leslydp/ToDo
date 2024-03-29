package com.example.todo.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.chrono.ChronoLocalDate
import java.time.chrono.ChronoZonedDateTime

@Entity(tableName = "to_do_table")
data class ToDoEntity(
    val actividad: String,
    val todoDescription: String,
    @ColumnInfo(name = "date") val date: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
