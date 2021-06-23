package com.example.expense.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expense.R
import com.example.expense.data.model.Transaction
import com.example.expense.ui.adapter.CashAdapter
import com.example.expense.ui.viewmodel.BoardingViewModel
import com.example.expense.ui.viewmodel.TransactionViewModel
import kotlinx.android.synthetic.main.fragment_cash.*
import java.util.*


class CashFragment : Fragment(R.layout.fragment_cash) {
    lateinit var viewModel: BoardingViewModel
    lateinit var transactionViewModel: TransactionViewModel
    private var listOfTransactions: MutableList<Transaction> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(BoardingViewModel::class.java)
        transactionViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLiveBudget().observe(viewLifecycleOwner,{
            it?.let {
                txtTotal.text = "Rs.${it}"
            }
        })

        with(rec_listTransactions) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = CashAdapter()
        }

        transactionViewModel.getAllTransactions().observe(viewLifecycleOwner, {
            val instance = Calendar.getInstance().time
            if (listOfTransactions.isNotEmpty()) {
                listOfTransactions.clear()
            }
            var total = 0
            for (i in it.indices) {
                if (instance.month == it[i].date.month && instance.year == it[i].date.year && it[i].type == 0) {
                    listOfTransactions.add(it[i])

                    if (it[i].income) {
                        total += it[i].amount
                    }
                    transactionViewModel.setIncomeTotal(total)
                }
            }
            if (listOfTransactions.isEmpty()) {
                txtError.isVisible = true
            } else {
                txtError.isVisible = false
                (rec_listTransactions.adapter as CashAdapter).submitList(listOfTransactions)
            }
        })
    }
}