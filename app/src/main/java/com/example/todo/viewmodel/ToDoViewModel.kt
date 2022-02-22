package com.example.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.source.local.entity.ToDoEntity
import com.example.todo.data.source.repository.ToDoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ToDoViewModel @Inject constructor(private val repository: ToDoRepository) : ViewModel() {
    val toDoList: Flow<List<ToDoEntity>> = repository.toDoList

    fun insert(toDoEntity: ToDoEntity) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.insert(toDoEntity = toDoEntity)
        }
    }
}