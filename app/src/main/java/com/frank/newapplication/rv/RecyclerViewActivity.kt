package com.frank.newapplication.rv

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.frank.newapplication.databinding.ActivityRvBinding


class RecyclerViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRvBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRvBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private var adapter: MultiSelectAdapter? = null

    private val data = mutableListOf(
//        SelectModel("选项1"),
        SelectModel("选项222222")
    )

    private fun initView() {
        val linearLayout = LinearLayoutManager(binding.root.context)
        binding.rview.setLayoutManager(linearLayout);
        // 创建适配器
        adapter = MultiSelectAdapter { isChecked, model ->
            Log.d("Frank###", "isChecked:$isChecked, model:$model")
            refreshData(isChecked, model)
        }
        // 提交初始数据
        adapter?.submitList(data)
        binding.rview.setAdapter(adapter)
    }

    private fun refreshData(isSelect: Boolean, item: SelectModel) {
        val newData = data.map {
            if (it.title == item.title) {
                it.copy(isChecked = isSelect)
            } else {
                it.copy(isEnable = false)
            }
        }
        // 使用 DiffUtil 高效更新数据
        adapter?.submitList(newData)
    }
}