package com.example.expense.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.expense.data.dao.BoardingDao
import com.example.expense.data.dao.CardDao
import com.example.expense.data.dao.TransactionDao
import com.example.expense.data.model.MonthCard
import com.example.expense.data.model.SingleUser
import com.example.expense.data.model.Transaction

@TypeConverters(DbTypeConverters::class)
@Database(entities = [SingleUser::class, Transaction::class,MonthCard::class], version = 1)
abstract class ExpenseDatabase : RoomDatabase() {
    abstract fun getBoardDao(): BoardingDao
    abstract fun getTransactionDao(): TransactionDao
    abstract fun getCardDao(): CardDao

    companion object {
        private var instance: ExpenseDatabase? = null

        fun getDatabase(context: Context) = instance
            ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseDatabase::class.java,
                    "expense_manager_db"
                ).build().also {
                    instance = it
                }
            }
    }
}