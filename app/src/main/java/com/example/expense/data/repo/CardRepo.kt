package com.example.expense.data.repo

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.expense.data.database.ExpenseDatabase
import com.example.expense.data.model.MonthCard
import com.example.expense.data.model.SingleUser

class CardRepo(application: Application){

     private val cardDao = ExpenseDatabase.getDatabase(application).getCardDao()

     suspend fun insertCard(monthCard: MonthCard){
          return cardDao.insert(monthCard)
     }

     fun getCard(id:Long):LiveData<MonthCard>{
          return cardDao.getCard(id)
     }

     fun getAllCards():LiveData<List<MonthCard>>{
          return cardDao.getAllCards()
     }
}
