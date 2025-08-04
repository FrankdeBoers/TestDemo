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
import com.frank.newapplication.databinding.ActivityNestedScrollDemoBinding

/**
 * 简化的嵌套滚动演示Activity
 * 展示基本的嵌套滚动功能
 */
class NestedScrollDemoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNestedScrollDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNestedScrollDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNestedScrollDemo()
    }

    /**
     * 设置嵌套滚动演示
     */
    private fun setupNestedScrollDemo() {
        // 外层RecyclerView
        val outerAdapter = SimpleOuterAdapter()
        binding.recyclerViewOuter.apply {
            layoutManager = LinearLayoutManager(this@NestedScrollDemoActivity)
            adapter = outerAdapter
            isNestedScrollingEnabled = true
        }
    }

    /**
     * 简化的外层适配器
     */
    private inner class SimpleOuterAdapter : RecyclerView.Adapter<SimpleOuterAdapter.OuterViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OuterViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_simple_outer, parent, false)
            return OuterViewHolder(view)
        }

        override fun onBindViewHolder(holder: OuterViewHolder, position: Int) {
            holder.bind(position)
        }

        override fun getItemCount(): Int = 5

        inner class OuterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerViewInner)
            private val titleText: TextView = itemView.findViewById(R.id.titleText)

            fun bind(position: Int) {
                titleText.text = "外层项目 $position"
                
                // 为每个内层RecyclerView设置不同的数据
                val innerAdapter = SimpleInnerAdapter(position)
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = innerAdapter
                    isNestedScrollingEnabled = true
                }
            }
        }
    }

    /**
     * 简化的内层适配器
     */
    private inner class SimpleInnerAdapter(private val outerPosition: Int) : 
        RecyclerView.Adapter<SimpleInnerAdapter.InnerViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_simple_inner, parent, false)
            return InnerViewHolder(view)
        }

        override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
            holder.bind(position)
        }

        override fun getItemCount(): Int = 10

        inner class InnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val contentText: TextView = itemView.findViewById(R.id.contentText)

            fun bind(position: Int) {
                contentText.text = "内层项目 ${outerPosition}-${position}"
            }
        }
    }
} 