package com.example.expense.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "month_card")
data class MonthCard(
    @PrimaryKey val id: Int,
    val month: String,
    val error: Boolean,
    val item1: Int,
    val item2: Int,
    val item3: Int,

    val item1Price: Int,
    val item2Price: Int,
    val item3Price: Int,

    val item1Type: Int,
    val item2Type: Int,
    val item3Type: Int,

    val date:Date
)