package com.example.expense.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.expense.data.model.MonthCard
import com.example.expense.data.repo.BoardingRepo
import com.example.expense.data.model.SingleUser
import com.example.expense.data.repo.CardRepo
import kotlinx.coroutines.launch

class CardViewModel(application: Application):AndroidViewModel(application){
    private val repo = CardRepo(application)


    private val id = MutableLiveData(0L)

    val getid : LiveData<Long>
    get() = id


     fun insertUser(monthCard: MonthCard){
         viewModelScope.launch {
             repo.insertCard(monthCard)
         }
    }

    fun getCard(id:Long):LiveData<MonthCard>{
           return repo.getCard(id)
    }

    fun getAllCards():LiveData<List<MonthCard>>{
        return repo.getAllCards()
    }

}