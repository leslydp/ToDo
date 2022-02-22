package com.example.todo.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.todo.data.source.local.entity.ToDoEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface To_DoDao {

    @Query("SELECT * FROM to_do_table ORDER BY date ASC" )
    fun getToDoListOrder(): Flow<List<ToDoEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(actividad: ToDoEntity, descriotion: ToDoEntity, date: ToDoEntity)

    @Query("DELETE FROM to_do_table")
    suspend fun deleteAll()

}