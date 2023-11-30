package com.example.expensesappbybard

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Expenses(
    @PrimaryKey val id: Int,
    val amount: Double,
    val category: String
)
