package com.example.todo.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todo.data.source.local.dao.To_DoDao
import com.example.todo.data.source.local.entity.ToDoEntity

@Database(entities = [ToDoEntity::class], version = 1, exportSchema = false)
abstract class DataBase : RoomDatabase() {
    abstract fun To_DoDao(): To_DoDao

    companion object {

        @Volatile
        private var INSTANCE: DataBase? = null

        fun getDatabase(context: Context): DataBase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DataBase::class.java,
                    "toDoDatabase"
                ).build()
                INSTANCE = instance

                instance
            }
        }
    }
}
