package com.example.expense.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expense.R
import com.example.expense.data.model.MonthCard
import com.example.expense.data.model.Transaction
import com.example.expense.ui.adapter.CardAdapter
import com.example.expense.ui.adapter.CashAdapter
import com.example.expense.ui.viewmodel.BoardingViewModel
import com.example.expense.ui.viewmodel.CardViewModel
import com.example.expense.ui.viewmodel.TransactionViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.fragment_all.*
import kotlinx.android.synthetic.main.fragment_all.txtNetBalance
import java.util.*

const val DATE = "DATE"
class AllFragment : Fragment(R.layout.fragment_all) {

    private lateinit var transactionViewModel: TransactionViewModel

    private val viewModel: BoardingViewModel by activityViewModels()
    private val cardviewModel: CardViewModel by activityViewModels()
    private var budget: Int = 0
    private var netBalances = 0
    private var cash = 0
    private var credit = 0
    private var debit = 0
    private val instance: Calendar = Calendar.getInstance()
    private val listOfUpcomingTransactions: MutableList<Transaction> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transactionViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)
        viewModel.getLiveBudget().observe(this, {
            it?.let {
                budget = it
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transactionViewModel.getAllTransactions().observe(viewLifecycleOwner, {
            it?.let {

                getTransactions(it, budget)
            }
        })

        val preferences = this.requireActivity()
            .getSharedPreferences("pref", Context.MODE_PRIVATE)
        val check = preferences.getString(OnBoardingFragment.LOGGED, null)
        if (check == "SUCCESS") {
            transactionViewModel.getAmountSpend().observe(viewLifecycleOwner, {
                it?.let {
                    setData(it)
                    setupPieChart()
                }
            })
            transactionViewModel.getAmountSaved().observe(viewLifecycleOwner, {
                it?.let {
                    credit = it
                    lbCredit.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                    lbCredit.text = "CREDIT : ${it}"
                    setupPieChart()
                }
            })
            transactionViewModel.getCash().observe(viewLifecycleOwner, {
                it?.let {
                    cash = it
                    lbCash.text = "CASH : ${it}"
                    setupPieChart()
                }
            })
        } else {
            findNavController().navigate(R.id.onboardingFragment)
        }

        with(recycle_monthCard) {
            layoutManager = LinearLayoutManager(context)
            adapter = CardAdapter {
                val bundle = Bundle()
                bundle.putLong(DATE, it)
                findNavController().navigate(R.id.monthlyFragment, bundle)
            }
        }
        with(rec_listUpcomingTransactions) {
            layoutManager = LinearLayoutManager(context)
            adapter = CashAdapter()
        }
        transactionViewModel.getUpcomingTransactions().observe(viewLifecycleOwner, {
            if(listOfUpcomingTransactions.isNotEmpty()){
                listOfUpcomingTransactions.clear()
            }
            it?.let {
                for (i in it.indices) {
                    if(instance.time.year == it[i].date.year) {
                        if (instance.time.month <= it[i].date.month) {
                            if (instance.time.date <= it[i].date.date || instance.time.month < it[i].date.month) {
                                listOfUpcomingTransactions.add(it[i])
                            }
                        }
                    }else{
                        if(it[i].date.year > instance.time.year) {
                            listOfUpcomingTransactions.add(it[i])
                        }
                    }
                }
            }
            (rec_listUpcomingTransactions.adapter as CashAdapter).submitList(
                listOfUpcomingTransactions
            )
        })
        cardviewModel.getAllCards().observe(viewLifecycleOwner, {
            it.let {
                (recycle_monthCard.adapter as CardAdapter).submitList(it)
            }
        })
        setupPieChart()
    }

    private fun setData(total: Int) {
        netBalances = budget - total
        debit = total
        lbDebit.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
        lbDebit.text = "DEBIT : ${total}"
        if (netBalances > 0) {
            txtNetBalance.text = netBalances.toString()
        }
    }

    private fun getTransactions(transactions: List<Transaction>, budget: Int) {
       /*  if (listOfMonthCard.isNotEmpty()) {
             listOfMonthCard.clear()
         }
        */
        for (j in transactions.indices) {
            when (transactions[j].date.month) {
                0 -> {
                    var total = 0
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 0 && transactions[i].date.year == instance.time.year) {
                            if (transactions[i].expense) {
                                total += transactions[i].amount
                            }
                        }
                    }
                    val category: MutableList<Int> = mutableListOf()
                    val price: MutableList<Int> = mutableListOf()
                    val type: MutableList<Int> = mutableListOf()
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 0 && transactions[i].date.year == instance.time.year) {
                            category.add(transactions[i].category)
                            price.add(transactions[i].amount)
                            if (transactions[i].income) {
                                type.add(0)
                            } else {
                                type.add(1)
                            }
                        }
                    }
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 0 && transactions[i].date.year == instance.time.year) {
                            val id = transactions[i].date.month + transactions[i].date.year

                            when (category.size) {
                                1 -> {
                                    val card = MonthCard(
                                        id, "JAN", total >= budget,
                                        category[0], -1, -1,
                                        price[0], -1, -1,
                                        type[0], -1, -1,
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                                2 -> {
                                    val card = MonthCard(
                                        id, "JAN", total >= budget,
                                        category[0], category[1], -1,
                                        price[0], price[1], -1,
                                        type[0], type[1], -1,
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                                3 -> {
                                    val card = MonthCard(
                                        id, "JAN", total >= budget,
                                        category[0], category[1], category[2],
                                        price[0], price[1], price[2],
                                        type[0], type[1], type[2],
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                            }

                        }
                    }
                }
                1 -> {
                    var total = 0
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 1 && transactions[i].date.year == instance.time.year) {
                            if (transactions[i].expense) {
                                total += transactions[i].amount
                            }
                        }
                    }
                    val category: MutableList<Int> = mutableListOf()
                    val price: MutableList<Int> = mutableListOf()
                    val type: MutableList<Int> = mutableListOf()
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 1 && transactions[i].date.year == instance.time.year) {
                            category.add(transactions[i].category)
                            price.add(transactions[i].amount)
                            if (transactions[i].income) {
                                type.add(0)
                            } else {
                                type.add(1)
                            }
                        }
                    }
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 1 && transactions[i].date.year == instance.time.year) {
                            val id = transactions[i].date.month + transactions[i].date.year

                            when (category.size) {
                                1 -> {
                                    val card = MonthCard(
                                        id, "FEB", total >= budget,
                                        category[0], -1, -1,
                                        price[0], -1, -1,
                                        type[0], -1, -1,
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                                2 -> {
                                    val card = MonthCard(
                                        id, "FEB", total >= budget,
                                        category[0], category[1], -1,
                                        price[0], price[1], -1,
                                        type[0], type[1], -1,
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                                3 -> {
                                    val card = MonthCard(
                                        id, "FEB", total >= budget,
                                        category[0], category[1], category[2],
                                        price[0], price[1], price[2],
                                        type[0], type[1], type[2],
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                            }

                        }
                    }
                }
                2 -> {
                    var total = 0
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 2 && transactions[i].date.year == instance.time.year) {
                            if (transactions[i].expense) {
                                total += transactions[i].amount
                            }
                        }
                    }
                    val category: MutableList<Int> = mutableListOf()
                    val price: MutableList<Int> = mutableListOf()
                    val type: MutableList<Int> = mutableListOf()
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 2 && transactions[i].date.year == instance.time.year) {
                            category.add(transactions[i].category)
                            price.add(transactions[i].amount)
                            if (transactions[i].income) {
                                type.add(0)
                            } else {
                                type.add(1)
                            }
                        }
                    }
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 2 && transactions[i].date.year == instance.time.year) {
                            val id = transactions[i].date.month + transactions[i].date.year

                            when (category.size) {
                                1 -> {
                                    val card = MonthCard(
                                        id, "MARCH", total >= budget,
                                        category[0], -1, -1,
                                        price[0], -1, -1,
                                        type[0], -1, -1,
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                                2 -> {
                                    val card = MonthCard(
                                        id, "MARCH", total >= budget,
                                        category[0], category[1], -1,
                                        price[0], price[1], -1,
                                        type[0], type[1], -1,
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                                3 -> {
                                    val card = MonthCard(
                                        id, "MARCH", total >= budget,
                                        category[0], category[1], category[2],
                                        price[0], price[1], price[2],
                                        type[0], type[1], type[2],
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                            }

                        }
                    }
                }
                3 -> {
                    var total = 0
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 3 && transactions[i].date.year == instance.time.year) {
                            if (transactions[i].expense) {
                                total += transactions[i].amount
                            }
                        }
                    }
                    val category: MutableList<Int> = mutableListOf()
                    val price: MutableList<Int> = mutableListOf()
                    val type: MutableList<Int> = mutableListOf()
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 3 && transactions[i].date.year == instance.time.year) {
                            category.add(transactions[i].category)
                            price.add(transactions[i].amount)
                            if (transactions[i].income) {
                                type.add(0)
                            } else {
                                type.add(1)
                            }
                        }
                    }
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 3 && transactions[i].date.year == instance.time.year) {
                            val id = transactions[i].date.month + transactions[i].date.year

                            when (category.size) {
                                1 -> {
                                    val card = MonthCard(
                                        id, "APRIL", total >= budget,
                                        category[0], -1, -1,
                                        price[0], -1, -1,
                                        type[0], -1, -1,
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                                2 -> {
                                    val card = MonthCard(
                                        id, "APRIL", total >= budget,
                                        category[0], category[1], -1,
                                        price[0], price[1], -1,
                                        type[0], type[1], -1,
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                                3 -> {
                                    val card = MonthCard(
                                        id, "APRIL", total >= budget,
                                        category[0], category[1], category[2],
                                        price[0], price[1], price[2],
                                        type[0], type[1], type[2],
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                            }

                        }
                    }
                }
                4 -> {
                    var total = 0
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 4 && transactions[i].date.year == instance.time.year) {
                            if (transactions[i].expense) {
                                total += transactions[i].amount
                            }
                        }
                    }
                    val category: MutableList<Int> = mutableListOf()
                    val price: MutableList<Int> = mutableListOf()
                    val type: MutableList<Int> = mutableListOf()
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 4 && transactions[i].date.year == instance.time.year) {
                            category.add(transactions[i].category)
                            price.add(transactions[i].amount)
                            if (transactions[i].income) {
                                type.add(0)
                            } else {
                                type.add(1)
                            }
                        }
                    }
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 4 && transactions[i].date.year == instance.time.year) {
                            val id = transactions[i].date.month + transactions[i].date.year

                            when (category.size) {
                                1 -> {
                                    val card = MonthCard(
                                        id, "MAY", total >= budget,
                                        category[0], -1, -1,
                                        price[0], -1, -1,
                                        type[0], -1, -1,
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                                2 -> {
                                    val card = MonthCard(
                                        id, "MAY", total >= budget,
                                        category[0], category[1], -1,
                                        price[0], price[1], -1,
                                        type[0], type[1], -1,
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                                3 -> {
                                    val card = MonthCard(
                                        id, "MAY", total >= budget,
                                        category[0], category[1], category[2],
                                        price[0], price[1], price[2],
                                        type[0], type[1], type[2],
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                            }

                        }
                    }
                }
                5 -> {
                    var total = 0
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 5 && transactions[i].date.year == instance.time.year) {
                            if (transactions[i].expense) {
                                total += transactions[i].amount
                            }
                        }
                    }
                    val category: MutableList<Int> = mutableListOf()
                    val price: MutableList<Int> = mutableListOf()
                    val type: MutableList<Int> = mutableListOf()
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 5 && transactions[i].date.year == instance.time.year) {
                            category.add(transactions[i].category)
                            price.add(transactions[i].amount)
                            if (transactions[i].income) {
                                type.add(0)
                            } else {
                                type.add(1)
                            }
                        }
                    }
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 5 && transactions[i].date.year == instance.time.year) {
                            val id = transactions[i].date.month + transactions[i].date.year

                            when (category.size) {
                                1 -> {
                                    val card = MonthCard(
                                        id, "JUNE", total >= budget,
                                        category[0], -1, -1,
                                        price[0], -1, -1,
                                        type[0], -1, -1,
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                                2 -> {
                                    val card = MonthCard(
                                        id, "JUNE", total >= budget,
                                        category[0], category[1], -1,
                                        price[0], price[1], -1,
                                        type[0], type[1], -1,
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                                3 -> {
                                    val card = MonthCard(
                                        id, "JUNE", total >= budget,
                                        category[0], category[1], category[2],
                                        price[0], price[1], price[2],
                                        type[0], type[1], type[2],
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                            }

                        }
                    }
                }
                6 -> {
                    var total = 0
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 6 && transactions[i].date.year == instance.time.year) {
                            if (transactions[i].expense) {
                                total += transactions[i].amount
                            }
                        }
                    }
                    val category: MutableList<Int> = mutableListOf()
                    val price: MutableList<Int> = mutableListOf()
                    val type: MutableList<Int> = mutableListOf()
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 6 && transactions[i].date.year == instance.time.year) {
                            category.add(transactions[i].category)
                            price.add(transactions[i].amount)
                            if (transactions[i].income) {
                                type.add(0)
                            } else {
                                type.add(1)
                            }
                        }
                    }
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 6 && transactions[i].date.year == instance.time.year) {
                            val id = transactions[i].date.month + transactions[i].date.year

                            when (category.size) {
                                1 -> {
                                    val card = MonthCard(
                                        id, "JULY", total >= budget,
                                        category[0], -1, -1,
                                        price[0], -1, -1,
                                        type[0], -1, -1,
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                                2 -> {
                                    val card = MonthCard(
                                        id, "JULY", total >= budget,
                                        category[0], category[1], -1,
                                        price[0], price[1], -1,
                                        type[0], type[1], -1,
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                                3 -> {
                                    val card = MonthCard(
                                        id, "JULY", total >= budget,
                                        category[0], category[1], category[2],
                                        price[0], price[1], price[2],
                                        type[0], type[1], type[2],
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                            }

                        }
                    }
                }
                7 -> {
                    var total = 0
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 7 && transactions[i].date.year == instance.time.year) {
                            if (transactions[i].expense) {
                                total += transactions[i].amount
                            }
                        }
                    }
                    val category: MutableList<Int> = mutableListOf()
                    val price: MutableList<Int> = mutableListOf()
                    val type: MutableList<Int> = mutableListOf()
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 7 && transactions[i].date.year == instance.time.year) {
                            category.add(transactions[i].category)
                            price.add(transactions[i].amount)
                            if (transactions[i].income) {
                                type.add(0)
                            } else {
                                type.add(1)
                            }
                        }
                    }
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 7 && transactions[i].date.year == instance.time.year) {
                            val id = transactions[i].date.month + transactions[i].date.year

                            when (category.size) {
                                1 -> {
                                    val card = MonthCard(
                                        id, "AUGUST", total >= budget,
                                        category[0], -1, -1,
                                        price[0], -1, -1,
                                        type[0], -1, -1,
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                                2 -> {
                                    val card = MonthCard(
                                        id, "AUGUST", total >= budget,
                                        category[0], category[1], -1,
                                        price[0], price[1], -1,
                                        type[0], type[1], -1,
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                                3 -> {
                                    val card = MonthCard(
                                        id, "AUGUST", total >= budget,
                                        category[0], category[1], category[2],
                                        price[0], price[1], price[2],
                                        type[0], type[1], type[2],
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                            }

                        }
                    }
                }
                8 -> {
                    var total = 0
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 8 && transactions[i].date.year == instance.time.year) {
                            if (transactions[i].expense) {
                                total += transactions[i].amount
                            }
                        }
                    }
                    val category: MutableList<Int> = mutableListOf()
                    val price: MutableList<Int> = mutableListOf()
                    val type: MutableList<Int> = mutableListOf()
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 8 && transactions[i].date.year == instance.time.year) {
                            category.add(transactions[i].category)
                            price.add(transactions[i].amount)
                            if (transactions[i].income) {
                                type.add(0)
                            } else {
                                type.add(1)
                            }
                        }
                    }
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 8 && transactions[i].date.year == instance.time.year) {
                            val id = transactions[i].date.month + transactions[i].date.year

                            when (category.size) {
                                1 -> {
                                    val card = MonthCard(
                                        id, "SEPT", total >= budget,
                                        category[0], -1, -1,
                                        price[0], -1, -1,
                                        type[0], -1, -1,
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                                2 -> {
                                    val card = MonthCard(
                                        id, "SEPT", total >= budget,
                                        category[0], category[1], -1,
                                        price[0], price[1], -1,
                                        type[0], type[1], -1,
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                                3 -> {
                                    val card = MonthCard(
                                        id, "SEPT", total >= budget,
                                        category[0], category[1], category[2],
                                        price[0], price[1], price[2],
                                        type[0], type[1], type[2],
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                            }

                        }
                    }
                }
                9 -> {
                    var total = 0
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 9 && transactions[i].date.year == instance.time.year) {
                            if (transactions[i].expense) {
                                total += transactions[i].amount
                            }
                        }
                    }
                    val category: MutableList<Int> = mutableListOf()
                    val price: MutableList<Int> = mutableListOf()
                    val type: MutableList<Int> = mutableListOf()
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 9 && transactions[i].date.year == instance.time.year) {
                            category.add(transactions[i].category)
                            price.add(transactions[i].amount)
                            if (transactions[i].income) {
                                type.add(0)
                            } else {
                                type.add(1)
                            }
                        }
                    }
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 9 && transactions[i].date.year == instance.time.year) {
                            val id = transactions[i].date.month + transactions[i].date.year

                            when (category.size) {
                                1 -> {
                                    val card = MonthCard(
                                        id, "OCT", total >= budget,
                                        category[0], -1, -1,
                                        price[0], -1, -1,
                                        type[0], -1, -1,
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                                2 -> {
                                    val card = MonthCard(
                                        id, "OCT", total >= budget,
                                        category[0], category[1], -1,
                                        price[0], price[1], -1,
                                        type[0], type[1], -1,
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                                3 -> {
                                    val card = MonthCard(
                                        id, "OCT", total >= budget,
                                        category[0], category[1], category[2],
                                        price[0], price[1], price[2],
                                        type[0], type[1], type[2],
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                            }

                        }
                    }
                }
                10 -> {
                    var total = 0
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 10 && transactions[i].date.year == instance.time.year) {
                            if (transactions[i].expense) {
                                total += transactions[i].amount
                            }
                        }
                    }
                    val category: MutableList<Int> = mutableListOf()
                    val price: MutableList<Int> = mutableListOf()
                    val type: MutableList<Int> = mutableListOf()
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 10 && transactions[i].date.year == instance.time.year) {
                            category.add(transactions[i].category)
                            price.add(transactions[i].amount)
                            if (transactions[i].income) {
                                type.add(0)
                            } else {
                                type.add(1)
                            }
                        }
                    }
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 10 && transactions[i].date.year == instance.time.year) {
                            val id = transactions[i].date.month + transactions[i].date.year

                            when (category.size) {
                                1 -> {
                                    val card = MonthCard(
                                        id, "NOV", total >= budget,
                                        category[0], -1, -1,
                                        price[0], -1, -1,
                                        type[0], -1, -1,
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                                2 -> {
                                    val card = MonthCard(
                                        id, "NOV", total >= budget,
                                        category[0], category[1], -1,
                                        price[0], price[1], -1,
                                        type[0], type[1], -1,
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                                3 -> {
                                    val card = MonthCard(
                                        id, "NOV", total >= budget,
                                        category[0], category[1], category[2],
                                        price[0], price[1], price[2],
                                        type[0], type[1], type[2],
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                            }

                        }
                    }
                }
                11 -> {
                    var total = 0
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 11 && transactions[i].date.year == instance.time.year) {
                            if (transactions[i].expense) {
                                total += transactions[i].amount
                            }
                        }
                    }
                    val category: MutableList<Int> = mutableListOf()
                    val price: MutableList<Int> = mutableListOf()
                    val type: MutableList<Int> = mutableListOf()
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 11 && transactions[i].date.year == instance.time.year) {
                            category.add(transactions[i].category)
                            price.add(transactions[i].amount)
                            if (transactions[i].income) {
                                type.add(0)
                            } else {
                                type.add(1)
                            }
                        }
                    }
                    for (i in transactions.indices) {
                        if (transactions[i].date.month == 11 && transactions[i].date.year == instance.time.year) {
                            val id = transactions[i].date.month + transactions[i].date.year

                            when (category.size) {
                                1 -> {
                                    val card = MonthCard(
                                        id, "DEC", total >= budget,
                                        category[0], -1, -1,
                                        price[0], -1, -1,
                                        type[0], -1, -1,
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                                2 -> {
                                    val card = MonthCard(
                                        id, "DEC", total >= budget,
                                        category[0], category[1], -1,
                                        price[0], price[1], -1,
                                        type[0], type[1], -1,
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                                3 -> {
                                    val card = MonthCard(
                                        id, "DEC", total >= budget,
                                        category[0], category[1], category[2],
                                        price[0], price[1], price[2],
                                        type[0], type[1], type[2],
                                        transactions[i].date
                                    )
                                    cardviewModel.insertUser(card)
                                }
                            }

                        }
                    }
                }
            }
        }

    }


    private fun setupPieChart() {
        // setup Pie entries
        val pieEntries = arrayListOf<PieEntry>()
        var first: Float = debit.toFloat()
        var second: Float = credit.toFloat()
        var third: Float = cash.toFloat()
        var forth: Float = netBalances.toFloat()


        pieEntries.add(PieEntry(first))
        pieEntries.add(PieEntry(second))
        pieEntries.add(PieEntry(third))
        pieEntries.add(PieEntry(forth))
// setup Pie chart animations
        pieChart.animateXY(1000, 1000)

        // setup PieChart Entries Colors
        val pieDataSet = PieDataSet(pieEntries, "This is Pie Chart Label")
        pieDataSet.setColors(
            resources.getColor(R.color.red),
            resources.getColor(R.color.green),
            resources.getColor(R.color.lightMango),
            resources.getColor(R.color.colorPrimaryDark)
        )

        // setup pie data set in piedata
        val pieData = PieData(pieDataSet)

        // setip text in pieChart centre
        pieChart.centerText = "Expenses"
        pieChart.setCenterTextColor(resources.getColor(android.R.color.black))
        pieChart.setCenterTextSize(10f)

        // hide the piechart entries tags
        pieChart.legend.isEnabled = false

//        now hide the description of piechart
        pieChart.description.isEnabled = false
        pieChart.description.text = "Expenses"

        pieChart.holeRadius = 65f
        // this enabled the values on each pieEntry
       // pieChart.setDrawValues(true)

        pieChart.data = pieData
    }
}