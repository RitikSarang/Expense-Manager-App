package com.example.expense.ui.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expense.R
import com.example.expense.data.model.Transaction
import com.example.expense.ui.adapter.CashAdapter
import com.example.expense.ui.viewmodel.BoardingViewModel
import com.example.expense.ui.viewmodel.TransactionViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.fragment_montly.*
import java.util.*


class MonthlyFragment : Fragment(R.layout.fragment_montly) {
    private val budgetViewModel : BoardingViewModel by activityViewModels()
    private val transactionViewModel: TransactionViewModel by activityViewModels()
    private val monthTransactions : MutableList<Transaction> = mutableListOf()
    private var amountSaved = 0
    private var amountSpend = 0
    private var budget = 0
    private var netBalance = 0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(rec_listMonthTransactions) {
            layoutManager = LinearLayoutManager(context)
            adapter = CashAdapter()
        }

        val bundle: Long = arguments?.getLong(DATE)!!
        val date = Date(bundle)
        budgetViewModel.getLiveBudget().observe(viewLifecycleOwner,{
             it?.let { budget=it }
        })
        transactionViewModel.getAllTransactions().observe(viewLifecycleOwner, {
            it?.let { transactionsMonthly ->
                for (i in transactionsMonthly.indices) {
                    if (transactionsMonthly[i].date.month == date.month && transactionsMonthly[i].date.year == date.year) {
                        monthTransactions.add(transactionsMonthly[i])
                        if(transactionsMonthly[i].income){
                            amountSaved += transactionsMonthly[i].amount
                        }else{
                            amountSpend += transactionsMonthly[i].amount
                        }
                    }
                }
                setupPieChart()
            }
            (rec_listMonthTransactions.adapter as CashAdapter).submitList(monthTransactions)
            setData(it)
        })
    }

    private fun setData(transaction: List<Transaction>?) {
        transaction?.let {
            with(transaction){
                netBalance = budget - amountSpend
                txtAmountSaved.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                txtAmountSaved.text = amountSaved.toString()
                txtAmountSpend.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                txtAmountSpend.text = amountSpend.toString()
                if(netBalance>0) {
                    txtNetBalance.setTextColor(Color.BLACK)
                    txtNetBalance.text = netBalance.toString()
                }
            }
        }
    }

    private fun setupPieChart() {
        // setup Pie entries
        val pieEntries = arrayListOf<PieEntry>()
        val first: Float = amountSaved.toFloat()
        val second: Float = amountSpend.toFloat()


        pieEntries.add(PieEntry(second))
        pieEntries.add(PieEntry(first))

// setup Pie chart animations
        pieChartMonthly.animateXY(1000, 1000)

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
        pieChartMonthly.centerText = "Expenses"
        pieChartMonthly.setCenterTextColor(resources.getColor(android.R.color.black))
        pieChartMonthly.setCenterTextSize(10f)

        // hide the piechart entries tags
        pieChartMonthly.legend.isEnabled = false

//        now hide the description of piechart
        pieChartMonthly.description.isEnabled = false
        pieChartMonthly.description.text = "Expenses"

        pieChartMonthly.holeRadius = 65f
        // this enabled the values on each pieEntry
        pieData.setDrawValues(true)

        pieChartMonthly.data = pieData
    }
}