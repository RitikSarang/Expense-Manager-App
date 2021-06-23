package com.example.expense.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


enum class Category {
    MILK,
    NEWSPAPER,
    GROCERY,
    BILL
}

enum class Type {
    CASH,
    CREDIT,
    DEBIT
}


@Entity(tableName = "transaction")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val name: String,
    val amount: Int,
    val date: Date,

    val category: Int,
    val type: Int,
    val comments: String,
    val income:Boolean,
    val expense:Boolean
)