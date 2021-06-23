package com.example.expense.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expense.data.model.MonthCard

@Dao
interface CardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(monthCard: MonthCard)

    @Query("SELECT * FROM month_card WHERE `id`=:id")
    fun getCard(id:Long):LiveData<MonthCard>

    @Query("SELECT * FROM month_card")
    fun getAllCards():LiveData<List<MonthCard>>
}