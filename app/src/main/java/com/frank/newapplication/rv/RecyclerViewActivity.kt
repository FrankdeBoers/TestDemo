package com.frank.newapplication.rv

import LastItemPaddingDecoration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.frank.newapplication.R
import com.frank.newapplication.databinding.ActivityRvBinding
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import org.w3c.dom.Text


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
        MultiSelectModel("选项12312"),
        MultiSelectModel("选项3333333333333"),
        MultiSelectModel("选项12"),
        MultiSelectModel("选项22222222"),
        MultiSelectModel("选项123123123"),
        MultiSelectModel("选项1"),
        MultiSelectModel("选项123123123"),
        MultiSelectModel("选项12"),
        MultiSelectModel("选项123123123123123"),
        MultiSelectModel("选项12"),
        MultiSelectModel("选项123123"),
        MultiSelectModel("选项1231"),
        MultiSelectModel("选项123"),
        MultiSelectModel("选项12312312312312312333252345234abcdefghijklmnopqrstuvwxyz---abcdefghijklmnopqrstuvwxyz"),
        MultiSelectModel("选项1231231231231231232"),
        MultiSelectModel("选项1231231231231231236"),
    )

    private fun initView() {
        //设置流式布局
        val flexboxLayoutManager = FlexboxLayoutManager(this);
        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP); //设置是否换行
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW); // 设置主轴排列方式
        flexboxLayoutManager.setAlignItems(AlignItems.STRETCH);
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);

//        val flexboxLayoutManager = WrapLayoutManager();
        binding.rview.setLayoutManager(flexboxLayoutManager);
        // 创建适配器
        adapter = MultiSelectAdapter(data) { isChecked, model ->
            Log.d("Test###", "isChecked:$isChecked, model:$model")
            checkDisable()
        }
        binding.rview.setAdapter(adapter)

        binding.rview.addItemDecoration(LastItemPaddingDecoration(this, flexboxLayoutManager))

        val text0 = TextView(this).apply {
            text = "aaaa1234567890abcd"
            setBackgroundColor(com.google.android.material.R.color.design_default_color_primary)
        }

        val text1 = TextView(this).apply {
            text = "bbbb1234567890abcd"
            setBackgroundColor(com.google.android.material.R.color.design_default_color_primary)

        }

        val text2 = TextView(this).apply {
            text = "cccc1234567890abcd"
            setBackgroundColor(com.google.android.material.R.color.design_default_color_primary)

        }
        val text3 = TextView(this).apply {
            text = "dddd1234567890abcd"
            setBackgroundColor(com.google.android.material.R.color.design_default_color_primary)

        }
        val text4 = TextView(this).apply {
            text = "eeee1234567890abcd"
            setBackgroundColor(com.google.android.material.R.color.design_default_color_primary)

        }
        binding.chipGroup.addView(text0)
        binding.chipGroup.addView(text1)
        binding.chipGroup.addView(text2)
        binding.chipGroup.addView(text3)
        binding.chipGroup.addView(text4)

        // 监听 ChipGroup 的布局变化
        binding.chipGroup.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int,
            ) {
                val childCount = binding.chipGroup.childCount
                var lastEndX = 0
                var lastIndex = 0

                for (i in 0 until childCount) {
                    val chip = binding.chipGroup.getChildAt(i) as TextView
                    val endX = chip.right
                    Log.d("Test###", "i:$i, endX:$endX, lastEndX:$lastEndX")
                    if (!(endX < lastEndX || i == childCount - 1)) {
                        // 当前 Chip 是新的一行或者是最后一个 Chip
                        if (i > 0) {
                            // 修改上一行最后一个 Chip 的边距
                            val lastChip = binding.chipGroup.getChildAt(lastIndex) as TextView
                            val params = lastChip.layoutParams as ChipGroup.LayoutParams
                            params.marginEnd = 0 // 设置右边距
                            lastChip.layoutParams = params
                        }
                        lastIndex = i
                    }
                    lastEndX = endX
                }
                // 移除监听，避免重复计算
                binding.chipGroup.removeOnLayoutChangeListener(this)
            }
        })


        val flexboxLayout = binding.flexboxLayout
        flexboxLayout.addOnLayoutChangeListener(listener)

    }

    private val listener = object : View.OnLayoutChangeListener {
        override fun onLayoutChange(
            v: View?,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
            oldLeft: Int,
            oldTop: Int,
            oldRight: Int,
            oldBottom: Int,
        ) {
            var lastEndX = 0
            var lastIndex = 0
            for (i in 0 until binding.flexboxLayout.childCount) {
                val child = binding.flexboxLayout.getChildAt(i)
                val endX = child.right
                Log.d("Test###", "onLayoutChange i:$i, endX:$endX, lastEndX:$lastEndX childCount:${binding.flexboxLayout.childCount}")
                if (endX < lastEndX || i == binding.flexboxLayout.childCount - 1) {
                    if (i > 0) {
                        val lastChild = binding.flexboxLayout.getChildAt(lastIndex)
                        val layoutParams = lastChild.layoutParams as FlexboxLayout.LayoutParams
                        layoutParams.width = lastChild.measuredWidth + 200 // 设置右边距
                        lastChild.layoutParams = layoutParams
                    }
                    lastIndex = i
                }
                lastEndX = endX
            }
            binding.flexboxLayout.removeOnLayoutChangeListener(this)
        }

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