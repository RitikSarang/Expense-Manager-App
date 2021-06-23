package com.example.expense.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expense.R
import com.example.expense.data.model.Category
import com.example.expense.data.model.Transaction
import com.example.expense.data.model.Type
import com.example.expense.readableFormat
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.cash_transaction_list_item.*

class CreditAdapter() : ListAdapter<Transaction, CreditAdapter.ViewHolder>(DiffCallBacks()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.credit_transaction_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CreditAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(item: Transaction) {
            with(item) {
                txtDate.text = date.readableFormat()
                if (income) {
                    txtTotal.text = "+${amount}"
                    txtTotal.setTextColor(Color.parseColor("#7CB342"))
                } else {
                    txtTotal.text = "-${amount}"
                    txtTotal.setTextColor(Color.parseColor("#FF0600"))
                }

                when (category) {
                    Category.MILK.ordinal -> {
                        txtItem.text = Category.MILK.name
                    }
                    Category.NEWSPAPER.ordinal -> {
                        txtItem.text = Category.NEWSPAPER.name
                    }
                    Category.GROCERY.ordinal -> {
                        txtItem.text = Category.GROCERY.name
                    }
                    Category.BILL.ordinal -> {
                        txtItem.text = Category.BILL.name
                    }
                }

                when (type) {
                    Type.CASH.ordinal -> {
                        txtTransactionType.text = Type.CASH.name
                    }
                    Type.CREDIT.ordinal -> {
                        txtTransactionType.text = Type.CREDIT.name
                    }
                    Type.DEBIT.ordinal -> {
                        txtTransactionType.text = Type.DEBIT.name
                    }
                }
            }

        }
    }
}

private class DiffCallBacks : DiffUtil.ItemCallback<Transaction>() {
    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem == newItem
    }

}