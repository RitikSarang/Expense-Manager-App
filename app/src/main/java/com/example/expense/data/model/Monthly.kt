package com.example.expense.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

//@Entity(tableName = "monthly")
data class Monthly(
    //@PrimaryKey(autoGenerate = true)
    val id: Long,

    val name: String,
    val date: Date,
    val type: String
)