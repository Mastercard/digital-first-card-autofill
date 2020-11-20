package com.mastercard.android.test.savecardsref.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mastercard.android.test.savecardsref.R

class CardListAdapter(private val dataSet: List<Card>, private val onClick: (Card) -> Unit) :
    RecyclerView.Adapter<CardListAdapter.ViewHolder>() {

    class ViewHolder(view: View, onClick: (Card) -> Unit) : RecyclerView.ViewHolder(view) {
        var currentCard: Card? = null
        val textView: TextView = view.findViewById(R.id.text_card)

        init {
            view.setOnClickListener { currentCard?.let { onClick(it) } }
        }

        fun bind(card: Card) {
            currentCard = card
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(viewGroup.context).inflate(R.layout.text_row_item, viewGroup, false),
        onClick
    )

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView.text = dataSet[position].number
        viewHolder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size
}