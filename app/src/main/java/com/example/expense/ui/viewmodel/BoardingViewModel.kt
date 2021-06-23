package com.example.expense.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.expense.data.repo.BoardingRepo
import com.example.expense.data.model.SingleUser
import kotlinx.coroutines.launch

class BoardingViewModel(application: Application):AndroidViewModel(application){
    private val repo = BoardingRepo(application)


    private val id = MutableLiveData(0L)

    val getid : LiveData<Long>
    get() = id

    fun insertUser(singleUser: SingleUser){
        viewModelScope.launch {
            repo.insertUser(singleUser)
        }
    }

    fun getUser(id:Long):LiveData<SingleUser>{
            return repo.getUser(id)
    }

    fun getLiveBudget():LiveData<Int>{
        return repo.getLiveBudget()
    }

}