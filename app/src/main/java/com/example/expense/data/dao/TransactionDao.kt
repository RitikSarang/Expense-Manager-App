package com.example.expense.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expense.data.model.Transaction

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTransaction(transaction: Transaction)

    @Query("SELECT * FROM `transaction`")
    fun getAllTransactions():LiveData<List<Transaction>>

    @Query("SELECT SUM(amount) FROM `transaction` WHERE expense=1")
    fun getAmountSpend():LiveData<Int>

    @Query("SELECT SUM(amount) FROM `transaction` WHERE income=1")
    fun getAmountSaved():LiveData<Int>

    @Query("SELECT SUM(amount) FROM `transaction` WHERE type = 0 AND expense = 1")
    fun getCash():LiveData<Int>


    @Query("SELECT * FROM `transaction`  WHERE Date(\"now\") >= date ORDER BY date ASC")
    fun getUpcomingTransactions():LiveData<List<Transaction>>

    @Query("SELECT * FROM `transaction`  ORDER BY date ASC")
    fun getDateByAsc():LiveData<List<Transaction>>

    @Query("select * from `transaction`where date = :date")
    fun getTuplesByTransactionDate(date:Long):LiveData<List<Transaction>>
}