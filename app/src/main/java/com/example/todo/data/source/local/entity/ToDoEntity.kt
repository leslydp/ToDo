package com.example.todo.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.chrono.ChronoLocalDate
import java.time.chrono.ChronoZonedDateTime

@Entity(tableName = "to_do_table")
class ToDoEntity(
    @PrimaryKey @ColumnInfo (name = "actividad") val actividad: String,
    @ColumnInfo (name = "description") val descriotion: String,
    @ColumnInfo (name = "date") val date: Long = System.currentTimeMillis()
)
