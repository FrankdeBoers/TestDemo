package com.frank.newapplication.rv

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.frank.newapplication.R
import java.util.concurrent.atomic.AtomicInteger

// RV adapter
class MultiSelectAdapter(private val clickCallback: (Boolean, SelectModel) -> Unit) : ListAdapter<SelectModel, SelectViewHolder>(MultiSelectDiffCallback()) {

    companion object {
        var counter = AtomicInteger(1)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false)
        view.findViewById<TextView>(R.id.mSelectTV).setTag(counter.getAndIncrement())
        Log.d("Frank##", "onCreateViewHolder tag:${view.findViewById<TextView>(R.id.mSelectTV).tag}")
        return SelectViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectViewHolder, position: Int) {
        val item = getItem(position)
        Log.d("Frank##", "onBindViewHolder title:${holder.textView.text}, isChecked:${holder.mSelectCheckBox.isChecked }")
        holder.mSelectCheckBox.isChecked = item.isChecked
        holder.textView.text = item.title
        holder.textView.setOnClickListener {
            clickCallback.invoke(!holder.mSelectCheckBox.isChecked, item)
        }
    }
}

// ViewHolder
class SelectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textView: TextView = itemView.findViewById<TextView>(R.id.mSelectTV)
    val mSelectCheckBox: CheckBox = itemView.findViewById<CheckBox>(R.id.mSelectCheckBox)
}

// 数据
data class SelectModel(
    val title: String,
    var isChecked: Boolean = false,
    val isEnable: Boolean = true,
)

/**
 * DiffUtil.ItemCallback 实现，用于高效比较数据项变化
 */
class MultiSelectDiffCallback : DiffUtil.ItemCallback<SelectModel>() {
    
    override fun areItemsTheSame(oldItem: SelectModel, newItem: SelectModel): Boolean {
        // 使用 title 作为唯一标识符，因为每个选项的标题应该是唯一的
        return oldItem.title == newItem.title
    }
    
    override fun areContentsTheSame(oldItem: SelectModel, newItem: SelectModel): Boolean {
        // 比较所有相关字段，判断内容是否相同
        return oldItem.title == newItem.title &&
               oldItem.isChecked == newItem.isChecked &&
               oldItem.isEnable == newItem.isEnable
    }
}
