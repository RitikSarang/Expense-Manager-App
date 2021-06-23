package com.example.expense.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expense.data.model.SingleUser

@Dao
interface BoardingDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(singleUser: SingleUser)

    @Query("SELECT * FROM expense WHERE `id`=:id")
    fun getUser(id:Long):LiveData<SingleUser>

    @Query("SELECT budget FROM expense")
    fun getLiveBudget():LiveData<Int>
}