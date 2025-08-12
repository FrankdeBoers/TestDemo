package com.frank.newapplication.rv

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.frank.newapplication.databinding.ActivityRvBinding
import kotlin.random.Random


class RecyclerViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRvBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRvBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private var adapter: MultiSelectAdapter? = null

    private var data = mutableListOf(
        SelectModel(id = 1, "选项1"),
        SelectModel(id = 2, "选项222222")
    )

    private fun initView() {
        val linearLayout = LinearLayoutManager(binding.root.context)
        binding.rview.setLayoutManager(linearLayout);
        // 创建适配器
        adapter = MultiSelectAdapter { isChecked, position ->
            Log.d("Frank###", "isChecked:$isChecked, position:$position")
            refreshData(isChecked, position)
        }
        // 提交初始数据
        adapter?.submitList(data)
        binding.rview.setAdapter(adapter)
    }

    var index1 = 0
    private fun refreshData(isSelect: Boolean, position: Int) {
        val newData = data.mapIndexed { index, value ->
            if (index== position) {
                value.copy(title = index1++.toString())
            } else {
                value.copy()
            }
        }
        Log.d("Frank###", "newData:$newData")
        // 使用 DiffUtil 高效更新数据
        adapter?.submitList(newData)
        data = newData.toMutableList()
    }
}