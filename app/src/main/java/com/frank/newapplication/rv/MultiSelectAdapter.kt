package com.frank.newapplication.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.frank.newapplication.R


class MultiSelectAdapter(var dataList: List<MultiSelectModel>, private val clickCallback: (Boolean, MultiSelectModel) -> Unit) : RecyclerView
.Adapter<MultiSelectViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiSelectViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false)
        return MultiSelectViewHolder(view)
    }

    override fun onBindViewHolder(holder: MultiSelectViewHolder, position: Int) {
        holder.textView.text = dataList[position].title
        holder.mSelectCheckBox.isClickable = false
        holder.mSelectCheckBox.setOnCheckedChangeListener() { v, event ->
            dataList[position].isChecked = holder.mSelectCheckBox.isChecked
            clickCallback.invoke(holder.mSelectCheckBox.isChecked, dataList[position])
            true
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}

class MultiSelectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textView: TextView = itemView.findViewById<TextView>(R.id.mSelectTV)
    val mSelectCheckBox: CheckBox = itemView.findViewById<CheckBox>(R.id.mSelectCheckBox)
}

data class MultiSelectModel(
    val title: String,
    var isChecked: Boolean = false,
    val isEnable: Boolean = true,
)
