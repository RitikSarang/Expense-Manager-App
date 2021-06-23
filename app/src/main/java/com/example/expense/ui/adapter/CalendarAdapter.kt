package com.example.expense.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expense.R
import com.example.expense.ui.adapter.CalendarAdapter.OnItemListener



class CalendarAdapter(
    private var daysOfMonth: ArrayList<String>?,
    private var income: MutableList<String>?,
    private var expense: MutableList<String>?,
    private var onItemListener: OnItemListener?
) : RecyclerView.Adapter<CalendarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.calender_cell,
            parent,
            false
        )
        val layoutParams: ViewGroup.LayoutParams = view.layoutParams
        layoutParams.height = (parent.height * 0.18888888888).toInt()
        return CalendarViewHolder(view, onItemListener)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.dayOfMonth?.text = daysOfMonth!![position]

        if (income!!.isNotEmpty()) {
            if (income!![position] == "") {
                holder.income?.text = ""
            } else {
                holder.income?.text = "+${income!![position]}"
            }
        }
        if (expense!!.isNotEmpty()) {

            if (expense!![position] == "") {
                holder.expense?.text = ""
            } else {
                holder.expense?.text = "-${expense!![position]}"
            }

        }
    }

    override fun getItemCount(): Int {
        return daysOfMonth!!.size
    }

    interface OnItemListener {
        fun onItemClick(position: Int, dayText: String?)
    }

}

class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    var dayOfMonth: TextView? = null
    var income: TextView? = null
    var expense: TextView? = null
    private var onItemListener: OnItemListener? = null

    constructor (itemView: View, onItemListener: OnItemListener?) : this(itemView) {
        dayOfMonth = itemView.findViewById(R.id.cellDayText)
        income = itemView.findViewById(R.id.cellIncome)
        expense = itemView.findViewById(R.id.cellExpense)

        this.onItemListener = onItemListener
        itemView.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        onItemListener!!.onItemClick(adapterPosition, dayOfMonth!!.text as String)
    }
}
