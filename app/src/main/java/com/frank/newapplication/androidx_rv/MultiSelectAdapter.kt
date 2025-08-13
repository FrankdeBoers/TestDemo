package com.frank.newapplication.androidx_rv

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

    var counter = AtomicInteger(0)

    var data = mutableListOf(
        SelectModel(id = 1, isChecked = false),
        SelectModel(id = 2, isChecked = false),
        SelectModel(id = 3, isChecked = false),
        SelectModel(id = 4, isChecked = false),
        SelectModel(id = 5, isChecked = false),
        SelectModel(id = 6, isChecked = false),
        SelectModel(id = 7, isChecked = false),
        SelectModel(id = 8, isChecked = false),
        SelectModel(id = 9, isChecked = false),
        SelectModel(id = 10, isChecked = false),
        SelectModel(id = 11, isChecked = false),
        SelectModel(id = 12, isChecked = false),
        SelectModel(id = 13, isChecked = false),
        SelectModel(id = 14, isChecked = false),
        SelectModel(id = 15, isChecked = false),
        SelectModel(id = 16, isChecked = false),
        SelectModel(id = 17, isChecked = false),
        SelectModel(id = 18, isChecked = false),
        SelectModel(id = 19, isChecked = false),
        SelectModel(id = 20, isChecked = false),
        SelectModel(id = 21, isChecked = false),
        SelectModel(id = 22, isChecked = false),
        SelectModel(id = 23, isChecked = false),
        SelectModel(id = 24, isChecked = false),
        SelectModel(id = 25, isChecked = false),
        SelectModel(id = 26, isChecked = false),
        SelectModel(id = 27, isChecked = false),
        SelectModel(id = 28, isChecked = false),
        SelectModel(id = 29, isChecked = false),
        SelectModel(id = 30, isChecked = false)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false)
        counter.getAndIncrement()
        view.findViewById<TextView>(R.id.mSelectTV).tag = counter.get()
        view.tag = counter.get()
        // 设置tag
        Log.d("Frank##Adapter", "onCreateViewHolder tag:${view.findViewById<TextView>(R.id.mSelectTV).tag}")
        return SelectViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectViewHolder, position: Int) {
        Log.d("Frank##Adapter", "before onBindViewHolder tag:${holder.textView.tag}, isChecked:${holder.mSelectCheckBox.isChecked}, holder:${holder.hashCode()}")
        holder.mSelectCheckBox.isChecked = data.getOrNull(position)?.isChecked ?: false
        holder.textView.text = "Tag is " + holder.textView.tag.toString()
        holder.textView.setOnClickListener {
            clickCallback.invoke(position)
        }
        Log.d("Frank##Adapter", "after onBindViewHolder tag:${holder.textView.tag}, isChecked:${holder.mSelectCheckBox.isChecked}, holder:${holder.hashCode()}")
    }

    fun refreshData(newData: List<SelectModel>) {
        // 使用 DiffUtil 高效更新数据
        submitList(newData)
        data = newData.toMutableList()
    }
}

// 数据
data class SelectModel(
    var id: Int,
    var isChecked: Boolean = false,
)

/**
 * DiffUtil.ItemCallback 实现，用于高效比较数据项变化
 * 进入页面，item1-->onCreate，选中态false      item2-->onCreate，选中态false
 * 首次点击item1 item1-->onCreateViewHolder
 * 重复点击item1 item1-->onBindViewHolder
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



// ViewHolder
class SelectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textView: TextView = itemView.findViewById<TextView>(R.id.mSelectTV)
    val mSelectCheckBox: CheckBox = itemView.findViewById<CheckBox>(R.id.mSelectCheckBox)
}