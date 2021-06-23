package com.example.expense.data.repo

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.expense.data.database.ExpenseDatabase
import com.example.expense.data.model.SingleUser

class BoardingRepo(application: Application){

     private val boardDao = ExpenseDatabase.getDatabase(application).getBoardDao()

     suspend fun insertUser(singleUser: SingleUser){
          return boardDao.insert(singleUser)
     }

     fun getUser(id:Long):LiveData<SingleUser>{
          return boardDao.getUser(id)
     }

     fun getLiveBudget():LiveData<Int>{
          return boardDao.getLiveBudget()
     }
}
