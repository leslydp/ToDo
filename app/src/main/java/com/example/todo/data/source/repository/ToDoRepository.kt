package com.example.todo.data.source.repository

import com.example.todo.data.source.local.dao.To_DoDao
import com.example.todo.data.source.local.entity.ToDoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ToDoRepository(private val toDodao: To_DoDao){

    val toDoList: Flow<List<ToDoEntity>> =toDodao.getToDoListOrder()

    suspend fun insert(actividad: ToDoEntity, descriotion: ToDoEntity, date: ToDoEntity){
        withContext(Dispatchers.IO){
            toDodao.insert(actividad, descriotion, date)
        }
    }

}