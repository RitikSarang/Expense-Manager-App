package com.example.expense.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "Expense")
data class SingleUser(
    @PrimaryKey(autoGenerate = true)
    val id : Long,
    val name:String,
    val budget:Int,
    val income:Int = 0
)