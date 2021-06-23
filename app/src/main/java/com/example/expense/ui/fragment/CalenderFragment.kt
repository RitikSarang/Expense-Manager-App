package com.example.expense.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expense.R
import com.example.expense.data.model.Transaction
import com.example.expense.ui.adapter.CalendarAdapter
import com.example.expense.ui.viewmodel.TransactionViewModel
import kotlinx.android.synthetic.main.fragment_calender.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList


class CalenderFragment : Fragment(R.layout.fragment_calender), CalendarAdapter.OnItemListener {
    private var income: MutableList<String> = mutableListOf()
    private var expense: MutableList<String> = mutableListOf()
    private lateinit var selectedDate: LocalDate
    private val transactionViewModel: TransactionViewModel by activityViewModels()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedDate = LocalDate.now()

    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMonthView()
         previousMonthAction.setOnClickListener {
             previousMonthAction()
         }
         nextMonthAction.setOnClickListener {
             nextMonthAction()
         }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun incomeInMonth(selectedDate: LocalDate, it: List<Transaction>?): MutableList<String> {
        val tempIncome: MutableList<String> = mutableListOf()
        val tempIndex: MutableList<Int> = mutableListOf()
        val tempValue: MutableList<Int> = mutableListOf()
        if (income.isNotEmpty()) {
            income.clear()
        }
        if (tempIncome.isNotEmpty()) {
            tempIncome.clear()
        }
        if (tempIndex.isNotEmpty()) {
            tempIndex.clear()
        }
        if (tempValue.isNotEmpty()) {
            tempValue.clear()
        }
        it?.let {
            val firstOfMonth = selectedDate.withDayOfMonth(1)

            val blankSpaces = firstOfMonth.dayOfWeek.value
            //Formula bL + date -1 = result
            for (i in it.indices) {

                val lengthRoomDate = it[i].date.year.toString()
                val lengthRoomDateForLocaleDate = selectedDate.year.toString()
                var dateLastDigit = 0
                    for (i in lengthRoomDate.indices){
                    if(lengthRoomDate.length-1 == i){
                        dateLastDigit = lengthRoomDate[i] - '0'
                    }
                }
                var dateLocaleLastDigit = 0
                for (i in lengthRoomDateForLocaleDate.indices){
                    if(lengthRoomDateForLocaleDate.length-1 == i){
                        dateLocaleLastDigit = lengthRoomDateForLocaleDate[i] - '0'
                    }
                }

                if (selectedDate.monthValue == it[i].date.month + 1 && dateLastDigit == dateLocaleLastDigit) {

                    if (it[i].income) {
                        tempIndex.add(it[i].date.date)

                        tempValue.add(it[i].amount)

                    }
                }
            }
            if (tempIndex.isNotEmpty() && tempValue.isNotEmpty()) {
            val result = finalArr(tempIndex, tempValue)
            val resultIndex = finalArrIndex(tempIndex, tempValue)

            var count = 0
            for (i in 0..41) {
                if (count != resultIndex.size) {
                    if (i == blankSpaces + resultIndex[count] -1) {

                        tempIncome.add(result[count].toString())
                        count++
                    }else{
                        tempIncome.add("")
                    }
                } else {
                    tempIncome.add("")
                }
            }

        }}
        return tempIncome
    }
    private fun finalArr(index: MutableList<Int>, values: MutableList<Int>): IntArray {
        val temp = IntArray(index.size)
        var count = 0
        temp[0] = values[0]
        for (i in index.indices) {
            for (j in i + 1 until index.size) {
                if (index[i] == index[j]) {
                    temp[count] += values[j]
                    break
                } else {
                    count++
                    temp[count] = values[j]
                    break
                }
            }
        }
        return temp
    }
    private fun finalArrIndex(index: MutableList<Int>, values: MutableList<Int>): IntArray {
        val temp = IntArray(index.size)
        var count = 0
        temp[0] = index[0]
        for (i in index.indices) {
            for (j in i + 1 until index.size) {
                if (index[i] == index[j]) {
                    temp[count] = index[j]
                    break
                } else {
                    count++
                    temp[count] = index[j]
                    break
                }
            }
        }
        return temp
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun expenseInMonth(selectedDate: LocalDate, it: List<Transaction>?): MutableList<String> {
        val tempIncome: MutableList<String> = mutableListOf()
        val tempIndex: MutableList<Int> = mutableListOf()
        val tempValue: MutableList<Int> = mutableListOf()
        if (expense.isNotEmpty()) {
            expense.clear()
        }
        if (tempIncome.isNotEmpty()) {
            tempIncome.clear()
        }
        if (tempIndex.isNotEmpty()) {
            tempIndex.clear()
        }
        if (tempValue.isNotEmpty()) {
            tempValue.clear()
        }
        it?.let {
            val firstOfMonth = selectedDate.withDayOfMonth(1)
            val blankSpaces = firstOfMonth.dayOfWeek.value

            //Formula bL + date -1 = result
            for (i in it.indices) {
                val lengthRoomDate = it[i].date.year.toString()
                val lengthRoomDateForLocaleDate = selectedDate.year.toString()
                var dateLastDigit = 0
                for (i in lengthRoomDate.indices){
                    if(lengthRoomDate.length-1 == i){
                        dateLastDigit = lengthRoomDate[i] - '0'
                    }
                }
                var dateLocaleLastDigit = 0
                for (i in lengthRoomDateForLocaleDate.indices){
                    if(lengthRoomDateForLocaleDate.length-1 == i){
                        dateLocaleLastDigit = lengthRoomDateForLocaleDate[i] - '0'
                    }
                }

                if (selectedDate.monthValue == it[i].date.month + 1 && dateLastDigit==dateLocaleLastDigit) {
                    if (it[i].expense) {
                        tempIndex.add(it[i].date.date)

                        tempValue.add(it[i].amount)

                    }
                }
            }
            if (tempIndex.isNotEmpty() && tempValue.isNotEmpty()) {
                val result = finalArr(tempIndex, tempValue)
                val resultIndex = finalArrIndex(tempIndex, tempValue)

                var count = 0
                for (i in 0..41) {
                    if (count != resultIndex.size) {
                        if (i == blankSpaces + resultIndex[count] - 1) {

                            tempIncome.add(result[count].toString())
                            count++
                        } else {
                            tempIncome.add("")
                        }
                    } else {
                        tempIncome.add("")
                    }
                }

            }
        }
        return tempIncome
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setMonthView() {
        transactionViewModel.getDateByAsc().observe(viewLifecycleOwner, {
            monthYearTV?.text = monthYearFromDate(selectedDate)
            val daysInMonth: ArrayList<String> = daysInMonthArray(selectedDate)!!
            income = incomeInMonth(selectedDate, it)
            expense = expenseInMonth(selectedDate, it)

            val calendarAdapter = CalendarAdapter(daysInMonth, income, expense, this)
            val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 7)
            calendarRecyclerView.layoutManager = layoutManager
            calendarRecyclerView.adapter = calendarAdapter
        })
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun daysInMonthArray(date: LocalDate): ArrayList<String>? {
        val daysInMonthArray: ArrayList<String> = ArrayList()
        val yearMonth = YearMonth.from(date)
        val daysInMonth = yearMonth.lengthOfMonth()
        val firstOfMonth = selectedDate.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value
        for (i in 1..42) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("")
            } else {
                daysInMonthArray.add((i - dayOfWeek).toString())
            }
        }
        return daysInMonthArray
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun monthYearFromDate(date: LocalDate): String? {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun previousMonthAction() {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun nextMonthAction() {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemClick(position: Int, dayText: String?) {
        if (dayText != "") {
            val message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}