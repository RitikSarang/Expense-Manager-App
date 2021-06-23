package com.example.expense.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.expense.data.model.Transaction
import com.example.expense.data.repo.TransactionRepo
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application):AndroidViewModel(application) {
    private val repo : TransactionRepo = TransactionRepo(application)
    private val _incomeTotal = MutableLiveData<Int>(0)


    val incomeTotal:LiveData<Int>
    get() = _incomeTotal



    fun setIncomeTotal(amount : Int){
        _incomeTotal.value = amount
    }


    fun insertTransaction(transaction: Transaction){
        viewModelScope.launch {
            repo.insertTransaction(transaction)
        }
    }

    fun getAllTransactions(): LiveData<List<Transaction>> {
        return repo.getAllTransactions()
    }


    fun getAmountSpend():LiveData<Int>{
        return repo.getAmountSpend()
    }
    fun getAmountSaved():LiveData<Int>{
        return repo.getAmountSaved()
    }

    fun getCash():LiveData<Int>{
        return repo.getCash()
    }

    fun getUpcomingTransactions():LiveData<List<Transaction>>{
        return repo.getUpcomingTransactions()
    }

    fun getDateByAsc():LiveData<List<Transaction>>{
        return repo.getDateByAsc()
    }
}