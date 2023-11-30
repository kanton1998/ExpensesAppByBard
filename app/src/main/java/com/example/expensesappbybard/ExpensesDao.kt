package com.example.expensesappbybard

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExpensesDao {

    @Insert
    suspend fun insert(expense: Expenses)

    @Query("SELECT * FROM expenses")
    suspend fun getAll(): List<Expenses>
}
