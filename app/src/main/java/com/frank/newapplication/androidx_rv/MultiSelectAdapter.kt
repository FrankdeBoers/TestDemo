package com.frank.newapplication.androidx_rv

import android.annotation.SuppressLint
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
class MultiSelectAdapter(private val clickCallback: (Int) -> Unit) : ListAdapter<SelectModel, SelectViewHolder>(MultiSelectDiffCallback()) {

    companion object {
        var counter = AtomicInteger(0)
    }

    var data = mutableListOf(
        SelectModel(id = 1, isChecked = false),
        SelectModel(id = 2, isChecked = false)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false)
        counter.getAndIncrement()
        view.findViewById<TextView>(R.id.mSelectTV).tag = counter.get()
        view.tag = counter.get()
        // 设置tag
        Log.d("Frank##", "onCreateViewHolder tag:${view.findViewById<TextView>(R.id.mSelectTV).tag}")
        return SelectViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SelectViewHolder, position: Int) {
        Log.d("Frank##", "onBindViewHolder tag:${holder.textView.tag}, isChecked:${holder.mSelectCheckBox.isChecked}, holder:${holder.hashCode()}")
        holder.mSelectCheckBox.isChecked = data.getOrNull(position)?.isChecked ?: false
        holder.textView.text = "Tag is " + holder.textView.tag.toString()
        holder.textView.setOnClickListener {
            clickCallback.invoke(position)
        }
    }

    fun refreshData(newData: List<SelectModel>) {
        // 使用 DiffUtil 高效更新数据
        submitList(newData)
        data = newData.toMutableList()
    }
}

// ViewHolder
class SelectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textView: TextView = itemView.findViewById<TextView>(R.id.mSelectTV)
    val mSelectCheckBox: CheckBox = itemView.findViewById<CheckBox>(R.id.mSelectCheckBox)
}

// 数据
data class SelectModel(
    var id: Int,
    var isChecked: Boolean = false,
)

/**
 * DiffUtil.ItemCallback 实现，用于高效比较数据项变化
 */
class MultiSelectDiffCallback : DiffUtil.ItemCallback<SelectModel>() {

    override fun areItemsTheSame(oldItem: SelectModel, newItem: SelectModel): Boolean {
        // 使用 title 作为唯一标识符，因为每个选项的标题应该是唯一的
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SelectModel, newItem: SelectModel): Boolean {
        // 比较所有相关字段，判断内容是否相同
        return oldItem.id == newItem.id &&
                oldItem.isChecked == newItem.isChecked
    }
}
