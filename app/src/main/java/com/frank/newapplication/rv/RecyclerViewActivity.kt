package com.frank.newapplication.rv

import CustomFlexboxItemDecoration
import WrapLayoutManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.frank.newapplication.databinding.ActivityRvBinding
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent


class RecyclerViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRvBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRvBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }
    val max = 2
    var adapter: MultiSelectAdapter? = null

    private val data = mutableListOf(
            MultiSelectModel("选项1111111111111111111"),
            MultiSelectModel("选项12312" ),
            MultiSelectModel("选项3333333333333" ),
            MultiSelectModel("选项12" ),
            MultiSelectModel("选项22222222" ),
            MultiSelectModel("选项123123123" ),
            MultiSelectModel("选项1" ),
            MultiSelectModel("选项123123123" ),
            MultiSelectModel("选项12" ),
            MultiSelectModel("选项123123123123123" ),
            MultiSelectModel("选项12" ),
            MultiSelectModel("选项123123" ),
            MultiSelectModel("选项1231" ),
            MultiSelectModel("选项123" ),
            MultiSelectModel("选项12312312312312312333252345234abcdefghijklmnopqrstuvwxyz---abcdefghijklmnopqrstuvwxyz" ),
            MultiSelectModel("选项1231231231231231232" ),
            MultiSelectModel("选项1231231231231231236" ),
        )

    private fun initView() {
        //设置流式布局
        val flexboxLayoutManager = WrapLayoutManager();
//        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP); //设置是否换行
//        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW); // 设置主轴排列方式
//        flexboxLayoutManager.setAlignItems(AlignItems.STRETCH);
//        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        binding.rview.setLayoutManager(flexboxLayoutManager);
        // 创建适配器
        adapter = MultiSelectAdapter(data) { isChecked, model ->
            Log.d("Test###", "isChecked:$isChecked, model:$model")
            checkDisable()
        }
        binding.rview.setAdapter(adapter)

//        binding.rview.addItemDecoration(CustomFlexboxItemDecoration())
    }

    private fun checkDisable() {
        // 当前已经选中的数量
        val checkedSize = data.filter { it.isChecked }.size
        if (checkedSize >= max) {
            val newData = data.map {
                it.copy(isEnable = false)
            }
            adapter?.dataList = newData
            adapter?.notifyDataSetChanged() // todo frank
        }
    }
}