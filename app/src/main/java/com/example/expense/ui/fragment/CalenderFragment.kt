package com.example.expense.ui.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expense.R
import com.example.expense.data.model.Transaction
import com.example.expense.readableFormat
import com.example.expense.ui.adapter.CalendarAdapter
import com.example.expense.ui.viewmodel.TransactionViewModel
import kotlinx.android.synthetic.main.fragment_calender.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.time.days


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
                Log.i("year", "incomeInMonth: ${selectedDate.year} , ${it[i].date.year} , ${((it[i].date.year.times(((selectedDate.year).toFloat()/it[i].date.year))).toInt()+1).toInt()}")
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
                Log.i("dateCount", "incomeInMonth: $dateLastDigit , $dateLocaleLastDigit")
                if (selectedDate.monthValue == it[i].date.month + 1 && dateLastDigit == dateLocaleLastDigit) {
                    Log.i(
                        "expenses", "onCreate: ${it[i].date.year} : ${it[i].amount} "
                    )
                    if (it[i].income) {
                        tempIndex.add(it[i].date.date)
                        //correct same date value here only
                        tempValue.add(it[i].amount)
                        //proper hai bas same dates ke value add karke dalna hai phir scene nhi hoga
                    }
                }
            }
            if (tempIndex.isNotEmpty() && tempValue.isNotEmpty()) {
            val result = finalArr(tempIndex, tempValue)
            val resultIndex = finalArrIndex(tempIndex, tempValue)
           /* result.forEachIndexed { index, i ->
                Log.i(
                    "jhal", "onCreate: ${result[index]}"
                )
            }
            resultIndex.forEachIndexed { index, i ->
                Log.i(
                    "jhal", "onCreate: ${resultIndex[index]}"
                )
            }
            tempIndex.forEachIndexed { index, i ->
                Log.i(
                    "expensesssss", "onCreate: ${tempIndex[index]}"
                )
            }
            tempValue.forEachIndexed { index, i ->
                Log.i(
                    "expensesssss", "onCreate: ${tempValue[index]}"
                )
            }*/
            var count = 0
            for (i in 0..41) {//proper hai
                if (count != resultIndex.size) {
                    if (i == blankSpaces + resultIndex[count] -1) {
                        //final result crafting
                        tempIncome.add(result[count].toString())
                        count++
                    }else{
                        tempIncome.add("")
                    }
                } else {
                    tempIncome.add("")
                }
            }
           /* tempIncome.forEachIndexed { index, s ->
                Log.i(
                    "expensesssssss", "onCreate: $index : ${tempIncome[index]}"
                )
            }*/
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
                        //correct same date value here only
                        tempValue.add(it[i].amount)
                        //proper hai bas same dates ke value add karke dalna hai phir scene nhi hoga
                    }
                }
            }
            if (tempIndex.isNotEmpty() && tempValue.isNotEmpty()) {
                val result = finalArr(tempIndex, tempValue)
                val resultIndex = finalArrIndex(tempIndex, tempValue)
                /*tempIndex.forEachIndexed { index, i ->
                    Log.i(
                        "jhal", "onCreate: ${tempIndex[index]}"
                    )
                }
                tempValue.forEachIndexed { index, i ->
                    Log.i(
                        "jhal", "onCreate: ${tempValue[index]}"
                    )
                }*/
                /* tempIndex.forEachIndexed { index, i ->
                 Log.i(
                     "expensesssss", "onCreate: ${tempIndex[index]}"
                 )
             }
             tempValue.forEachIndexed { index, i ->
                 Log.i(
                     "expensesssss", "onCreate: ${tempValue[index]}"
                 )
             }*/
                var count = 0
                for (i in 0..41) {//proper hai
                    if (count != resultIndex.size) {
                        if (i == blankSpaces + resultIndex[count] - 1) {
                            //final result crafting
                            tempIncome.add(result[count].toString())
                            count++
                        } else {
                            tempIncome.add("")
                        }
                    } else {
                        tempIncome.add("")
                    }
                }
                /*tempIncome.forEachIndexed { index, s ->
                Log.i(
                    "expensesssssss", "onCreate: $index : ${tempIncome[index]}"
                )
            }*/
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
            /*for (i in income.indices) {
                Log.i(
                    "expensesss", "onCreate: $i : ${income[i]} + size : ${income.size}"
                )
            }*/
            expense = expenseInMonth(selectedDate, it)
            /*for(i in expense.indices){
                Log.i("what", "expenseInMonth: ${expense[i]} : ${expense.size}")
            }*/
           /*  for (i in expense.indices) {
                 Log.i(
                     "Cal", "onCreate: $i : ${expense[i]} + size : ${expense.size}"
                 )
             }*/
           /* Log.i(
                "Cal", "onCreate: ${expense.size} + size : ${income.size}"
            )*/
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
                Log.i("sfh", "daysInMonthArray: $i , $dayOfWeek = ${i - dayOfWeek}")
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