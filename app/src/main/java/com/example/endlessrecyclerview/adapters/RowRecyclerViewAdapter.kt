package com.example.endlessrecyclerview.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.bohdan.kolomiiets.eventsreminder.base.adapter.BaseAdapter
import com.bohdan.kolomiiets.eventsreminder.base.adapter.BaseViewHolder
import com.example.endlessrecyclerview.R
import com.example.endlessrecyclerview.models.Row

class RowRecyclerViewAdapter: BaseAdapter<Row>() {
    private var customItemCallback: CustomItemCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder {
        return RowViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_item, parent, false))
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Row>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.bind(getData()[position], position)
    }
    fun attachCustomItemCallback(callback: CustomItemCallback) {
        customItemCallback = callback
    }

    fun detachCustomItemCallback() {
        customItemCallback = null
    }

    inner class RowViewHolder(itemView: View): BaseViewHolder<Row>(itemView) {
        private val rowId: TextView = itemView.findViewById(R.id.row_id)
        private val rowTitle: TextView = itemView.findViewById(R.id.row_title)
        private val toastBtn: Button = itemView.findViewById(R.id.toast_btn)
        private val snackBtn: Button = itemView.findViewById(R.id.snack_btn)

        override fun bind(model: Row, position: Int) {
            rowId.text = model.id.toString()
            rowTitle.text = model.title
            customItemCallback?.let { customItemCallback ->
                toastBtn.setOnClickListener {
                    customItemCallback.onToastBtn(model, position)
                }
                snackBtn.setOnClickListener {
                    customItemCallback.onSnackBtn(model, position)
                }
            }
        }
    }
}