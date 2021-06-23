package com.example.expense.ui.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.expense.R
import com.example.expense.data.model.Category
import com.example.expense.data.model.Transaction
import com.example.expense.data.model.Type
import com.example.expense.readableFormat
import com.example.expense.ui.viewmodel.TransactionViewModel
import kotlinx.android.synthetic.main.fragment_add_transaction.*
import java.util.*

class AddTransactionFragment : Fragment(R.layout.fragment_add_transaction) {
    lateinit var viewModel: TransactionViewModel
    private var category = mutableListOf<String>()
    private var types = mutableListOf<String>()
    private val instance = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textInputEditText_names.doOnTextChanged { text, _, _, _ ->
            if (text!!.isEmpty()) {
                textInputLayout_names.error = "Name field can't be empty"
            } else {
                textInputLayout_names.error = null
            }
        }
        textInputEditText_amount.doOnTextChanged { text,  _, _, _ ->
            if (text!!.isEmpty()) {
                textInputLayout_amount.error = "Amount field can't be empty"
            } else {
                textInputLayout_amount.error = null
            }
        }

        val year = instance.get(Calendar.YEAR)
        val month = instance.get(Calendar.MONTH)
        val day = instance.get(Calendar.DAY_OF_MONTH)

        txt_input_date.setOnClickListener {
            val dateFormat = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { _, myear, mmonth, mday ->
                    instance.set(Calendar.YEAR, myear)
                    instance.set(Calendar.MONTH, mmonth)
                    instance.set(Calendar.DAY_OF_MONTH, mday)
                    txt_input_date.setText(instance.time.readableFormat())
                },
                year,
                month,
                day
            )
            dateFormat.show()
        }

        btn_expense.setOnClickListener {
            saveExpenseData()
        }
        btn_income.setOnClickListener {
            saveIncomeData()
        }

        Category.values().forEach {
            category.add(it.name)
        }

        Type.values().forEach {
            types.add(it.name)
        }

        sp_category.adapter = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            category
        )

        sp_transaction.adapter = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            types
        )

        sp_transaction.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                updateViews(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    private fun updateViews(position: Int) {
        when(position){
            Type.CASH.ordinal -> {
                btn_income.isVisible = true
                btn_expense.isVisible = true
            }
            Type.CREDIT.ordinal -> {
                btn_income.isVisible = true
                btn_expense.isVisible = false
            }
            Type.DEBIT.ordinal -> {
                btn_income.isVisible = false
                btn_expense.isVisible = true
            }
        }
    }


    private fun saveExpenseData() {
        val name = textInputEditText_names.text.toString()
        val amount = textInputEditText_amount.text.toString()
        val spCategory = sp_category.selectedItemPosition
        val spType = sp_transaction.selectedItemPosition
        val comments = et_comment.editText?.text.toString()

        validateField(name, amount, spCategory, instance.time, spType, comments)
    }

    private fun validateField(
        name: String,
        amount: String,
        spCategory: Int,
        date: Date,
        spType: Int,
        comments: String
    ) {
        if (name.isNotBlank() && comments.isNotBlank() && amount.isNotBlank()) {
            val transaction = Transaction(
                0,
                name,
                amount.toInt(),
                date,
                spCategory,
                spType,
                comments,
                income = false,
                expense = true
            )

            viewModel.insertTransaction(transaction)
            requireActivity().onBackPressed()
        } else {
            if (name.isBlank()) {
                textInputLayout_names.error = "Name field can't be empty"
            } else {
                textInputEditText_names.error = null
            }
            if (amount.isBlank()) {
                textInputLayout_amount.error = "Amount field can't be empty"
            } else {
                textInputLayout_amount.error = null
            }
            if (comments.isBlank()) {
                et_comment.error = "Comment field can't be empty"
            } else {
                et_comment.error = null
            }
        }
    }

    private fun saveIncomeData() {
        val name = textInputEditText_names.text.toString()
        val amount = textInputEditText_amount.text.toString()
        val spCategory = sp_category.selectedItemPosition
        val spType = sp_transaction.selectedItemPosition
        val comments = et_comment.editText?.text.toString()
        validateIncomeField(name, amount, spCategory, instance.time, spType, comments)
    }

    private fun validateIncomeField(
        name: String,
        amount: String,
        spCategory: Int,
        date: Date,
        spType: Int,
        comments: String
    ) {
        if (name.isNotBlank() && comments.isNotBlank() && amount.isNotBlank()) {
            val transaction = Transaction(
                0,
                name,
                amount.toInt(),
                date,
                spCategory,
                spType,
                comments,
                income = true,
                expense = false
            )

            viewModel.insertTransaction(transaction)
            requireActivity().onBackPressed()
        } else {
            if (name.isBlank()) {
                textInputLayout_names.error = "Name field can't be empty"
            } else {
                textInputEditText_names.error = null
            }
            if (amount.isBlank()) {
                textInputLayout_amount.error = "Amount field can't be empty"
            } else {
                textInputLayout_amount.error = null
            }
            if (comments.isBlank()) {
                et_comment.error = "Comment field can't be empty"
            } else {
                et_comment.error = null
            }
        }
    }

}