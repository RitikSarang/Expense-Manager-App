package com.example.expense.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expense.R
import com.example.expense.data.model.Category
import com.example.expense.data.model.MonthCard
import com.example.expense.data.model.Transaction
import com.example.expense.data.model.Type
import com.example.expense.readableFormat
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.cash_transaction_list_item.*
import kotlinx.android.synthetic.main.month_card_list_item.*
import java.util.*

class CardAdapter(private val listener : (Long) -> Unit) : ListAdapter<MonthCard, CardAdapter.ViewHolder>(DiffCallBac()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.month_card_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        init {
            itemView.setOnClickListener {
                listener.invoke(getItem(adapterPosition).date.time)
            }
        }
        fun bind(item: MonthCard) {
            with(item) {

                txtMonth.text = month
                if (error) {
                    txtErrorText.isVisible = true
                    cardViewBG.background.setTint(Color.RED)

                } else {
                    txtErrorText.isVisible = false
                    cardViewBG.background.setTint(Color.GREEN)
                    //cardViewBG.setBackgroundColor(Color.GREEN)
                }

                if (item1 == -1) {
                    txtItem1.isVisible = false
                } else {
                    when (item1) {
                        Category.MILK.ordinal -> {
                            txtItem1.isVisible = true
                            txtItem1.text = Category.MILK.name
                        }
                        Category.NEWSPAPER.ordinal -> {
                            txtItem1.isVisible = true
                            txtItem1.text = Category.NEWSPAPER.name
                        }
                        Category.GROCERY.ordinal -> {
                            txtItem1.isVisible = true
                            txtItem1.text = Category.GROCERY.name
                        }
                        Category.BILL.ordinal -> {
                            txtItem1.isVisible = true
                            txtItem1.text = Category.BILL.name
                        }
                    }
                }
                if (item2 == -1) {
                    txtItem2.isVisible = false
                } else {
                    when (item2) {
                        Category.MILK.ordinal -> {
                            txtItem2.isVisible = true
                            txtItem2.text = Category.MILK.name
                        }
                        Category.NEWSPAPER.ordinal -> {
                            txtItem2.isVisible = true
                            txtItem2.text = Category.NEWSPAPER.name
                        }
                        Category.GROCERY.ordinal -> {
                            txtItem2.isVisible = true
                            txtItem2.text = Category.GROCERY.name
                        }
                        Category.BILL.ordinal -> {
                            txtItem2.isVisible = true
                            txtItem2.text = Category.BILL.name
                        }
                    }
                }
                if (item3 == -1) {
                    txtItem3.isVisible = false
                } else {
                    when (item3) {
                        Category.MILK.ordinal -> {
                            txtItem3.isVisible = true
                            txtItem3.text = Category.MILK.name
                        }
                        Category.NEWSPAPER.ordinal -> {
                            txtItem3.isVisible = true
                            txtItem3.text = Category.NEWSPAPER.name
                        }
                        Category.GROCERY.ordinal -> {
                            txtItem3.isVisible = true
                            txtItem3.text = Category.GROCERY.name
                        }
                        Category.BILL.ordinal -> {
                            txtItem3.isVisible = true
                            txtItem3.text = Category.BILL.name
                        }
                    }

                }

                if (item1Price == -1) {
                    txtItem1Expense.isVisible = false
                } else {
                   if(item1Type == 0){
                       //income
                       txtItem1Expense.isVisible = true
                       txtItem1Expense.setTextColor(Color.GREEN)
                       txtItem1Expense.text = "+${item1Price}"
                   }else{
                       //expense
                       txtItem1Expense.isVisible = true
                       txtItem1Expense.setTextColor(Color.RED)
                       txtItem1Expense.text = "-${item1Price}"
                   }
                }
                if (item2Price == -1) {
                    txtItem2Expense.isVisible = false
                } else {
                    if(item2Type == 0){
                        //income
                        txtItem2Expense.isVisible = true
                        txtItem2Expense.setTextColor(Color.GREEN)
                        txtItem2Expense.text = "+${item2Price}"
                    }else{
                        //expense
                        txtItem2Expense.isVisible = true
                        txtItem2Expense.setTextColor(Color.RED)
                        txtItem2Expense.text = "-${item2Price}"
                    }
                }
                if (item3Price == -1) {
                    txtItem3Expense.isVisible = false
                } else {
                    if(item3Type == 0){
                        //income
                        txtItem3Expense.isVisible = true
                        txtItem3Expense.setTextColor(Color.GREEN)
                        txtItem3Expense.text = "+${item3Price}"
                    }else{
                        //expense
                        txtItem3Expense.isVisible = true
                        txtItem3Expense.setTextColor(Color.RED)
                        txtItem3Expense.text = "-${item3Price}"
                    }
                }
            }

        }
    }
}

private class DiffCallBac : DiffUtil.ItemCallback<MonthCard>() {
    override fun areItemsTheSame(oldItem: MonthCard, newItem: MonthCard): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MonthCard, newItem: MonthCard): Boolean {
        return oldItem == newItem
    }

}