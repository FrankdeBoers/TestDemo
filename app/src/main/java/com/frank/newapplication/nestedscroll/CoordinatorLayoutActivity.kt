package com.frank.newapplication.nestedscroll

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.frank.newapplication.R
import com.frank.newapplication.databinding.ActivityCoordinatorLayoutBinding
import com.google.android.material.appbar.AppBarLayout

/**
 * CoordinatorLayout嵌套滚动演示
 * 展示AppBarLayout + RecyclerView的嵌套滚动效果
 */
class CoordinatorLayoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCoordinatorLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoordinatorLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupCoordinatorLayout()
        setupRecyclerView()
    }

    /**
     * 设置CoordinatorLayout
     */
    private fun setupCoordinatorLayout() {
        // 设置AppBarLayout的滚动监听
        binding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            println("Frank## OffsetChange verticalOffset:$verticalOffset")
            val scrollRange = appBarLayout.totalScrollRange
            val scrollPercentage = Math.abs(verticalOffset).toFloat() / scrollRange.toFloat()
            
            // 根据滚动百分比调整标题透明度
            binding.collapsingTitle.alpha = scrollPercentage
            binding.expandingTitle.alpha = 1f - scrollPercentage
        })
    }

    /**
     * 设置RecyclerView
     */
    private fun setupRecyclerView() {
        val adapter = CoordinatorAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@CoordinatorLayoutActivity)
            this.adapter = adapter
            isNestedScrollingEnabled = true
        }
    }

    /**
     * RecyclerView适配器
     */
    private inner class CoordinatorAdapter : RecyclerView.Adapter<CoordinatorAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_coordinator, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(position)
        }

        override fun getItemCount(): Int = 100

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val titleText: TextView = itemView.findViewById(R.id.titleText)
            private val contentText: TextView = itemView.findViewById(R.id.contentText)

            fun bind(position: Int) {
                titleText.text = "项目 $position"
                contentText.text = "这是CoordinatorLayout嵌套滚动的演示内容。当向上滚动时，AppBarLayout会收起，向下滚动时会展开。"
            }
        }
    }
} 