package com.example.expensesappbybard

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Expenses::class], version = 1)
abstract class ExpensesDatabase : RoomDatabase() {

    abstract fun expensesDao(): ExpensesDao
}
