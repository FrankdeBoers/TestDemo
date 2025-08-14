package com.frank.newapplication.androidx_rv

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

    private lateinit var adapter: MultiSelectAdapter

    private fun initView() {
        val linearLayout = LinearLayoutManager(binding.root.context)
        binding.rview.setLayoutManager(linearLayout);
        // 取消item动画
//        binding.rview.itemAnimator = null
        // 创建适配器
        adapter = MultiSelectAdapter { clickedPosition ->
            // 点击后更改数据
            Log.d("Frank###", "clickedPosition:$clickedPosition")
            refreshData(clickedPosition)
        }
        // 设置稳定ID，解决ViewHolder复用问题
        adapter.setHasStableIds(true)
        // 提交初始数据
        adapter.submitList(adapter.data)
        binding.rview.setAdapter(adapter)
    }

    private fun refreshData(clickedPosition: Int) {
        val newData = adapter.data.mapIndexed { index, value ->
            if (index == clickedPosition) {
                // 更改选中态
                value.copy(isChecked = !value.isChecked)
            } else {
                value.copy()
            }
        }
        Log.d("Frank###", "newData:$newData")
        adapter.refreshData(newData)
    }
}