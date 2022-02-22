package com.example.todo.di

import android.content.Context
import com.example.todo.data.source.local.DataBase
import com.example.todo.data.source.local.dao.To_DoDao
import com.example.todo.data.source.repository.ToDoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ToDoDi{

    @Singleton
    @Provides
    fun providesDataBase(@ApplicationContext appContext: Context) =
        DataBase.getDatabase(appContext)


    @Provides
    @Singleton
    fun prividesRepository(toDodao: To_DoDao): ToDoRepository {
        return ToDoRepository(toDodao = toDodao)
    }

    @Provides
    fun providesToDoDao(dataBase: DataBase): To_DoDao {
        return dataBase.To_DoDao()
    }
}