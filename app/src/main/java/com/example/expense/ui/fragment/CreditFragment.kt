package com.example.expense.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expense.R
import com.example.expense.data.model.Transaction
import com.example.expense.ui.adapter.CashAdapter
import com.example.expense.ui.adapter.CreditAdapter
import com.example.expense.ui.viewmodel.BoardingViewModel
import com.example.expense.ui.viewmodel.TransactionViewModel
import kotlinx.android.synthetic.main.fragment_cash.*
import kotlinx.android.synthetic.main.fragment_credit.*
import kotlinx.android.synthetic.main.fragment_credit.rec_listTransactions
import kotlinx.android.synthetic.main.fragment_credit.txtTotal
import java.util.*


class CreditFragment : Fragment(R.layout.fragment_credit) {
    lateinit var boardingViewModel: BoardingViewModel
    lateinit var transactionViewModel: TransactionViewModel
    private var listOfTransactions: MutableList<Transaction> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        boardingViewModel = ViewModelProvider(this).get(BoardingViewModel::class.java)
        transactionViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        boardingViewModel.getLiveBudget().observe(viewLifecycleOwner,{
            txtTotal.text = "Rs.${it}"
        })

        with(rec_listTransactions) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = CreditAdapter()
        }

        transactionViewModel.getAllTransactions().observe(viewLifecycleOwner, {
            val instance = Calendar.getInstance().time
            if (listOfTransactions.isNotEmpty()) {
                listOfTransactions.clear()
            }
            var total = 0
            for (i in it.indices) {

                if (instance.month == it[i].date.month && instance.year == it[i].date.year && it[i].type == 1) {
                    listOfTransactions.add(it[i])

                    if(it[i].income){
                        total+=it[i].amount
                    }
                    transactionViewModel.setIncomeTotal(total)
                }
            }
            if (listOfTransactions.isEmpty()) {
                txtErrors.isVisible = true
            } else {
                txtErrors.isVisible = false
                (rec_listTransactions.adapter as CreditAdapter).submitList(listOfTransactions)
            }
        })
    }
}