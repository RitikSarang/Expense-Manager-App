package com.example.expense.data.repo

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.expense.data.database.ExpenseDatabase
import com.example.expense.data.model.Transaction

class TransactionRepo(application: Application) {
    private val transactionRepo = ExpenseDatabase.getDatabase(application).getTransactionDao()

    suspend fun insertTransaction(transaction: Transaction){
        return transactionRepo.insertTransaction(transaction)
    }

    fun getAllTransactions():LiveData<List<Transaction>>{
        return transactionRepo.getAllTransactions()
    }

    fun getAmountSpend():LiveData<Int>{
        return transactionRepo.getAmountSpend()
    }

    fun getAmountSaved():LiveData<Int>{
        return transactionRepo.getAmountSaved()
    }

    fun getCash():LiveData<Int>{
        return transactionRepo.getCash()
    }

    fun getUpcomingTransactions():LiveData<List<Transaction>>{
        return transactionRepo.getUpcomingTransactions()
    }

    fun getDateByAsc():LiveData<List<Transaction>>{
        return transactionRepo.getDateByAsc()
    }
}