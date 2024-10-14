package com.example.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.models.TodoDBO
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("SELECT * FROM TodoDBO")
    fun observeAll():Flow<List<TodoDBO>>

    @Query("SELECT * FROM TodoDBO WHERE id = :id")
    suspend fun getById(id: String): TodoDBO?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todoDBO: TodoDBO)

    @Delete
    suspend fun delete(todoDBO: TodoDBO)

}