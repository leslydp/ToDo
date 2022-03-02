package com.example.todo.data.source.local.dao

import androidx.room.*
import com.example.todo.data.source.local.entity.ToDoEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface To_DoDao {

    @Query("SELECT * FROM to_do_table ORDER BY date ASC" )
    fun getToDoListOrder(): Flow<List<ToDoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(toDoEntity: ToDoEntity)

    @Query("DELETE FROM to_do_table")
    suspend fun deleteAll()

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update( toDoEntity: ToDoEntity)

    @Delete
    suspend fun delete(toDoEntity: ToDoEntity)

}