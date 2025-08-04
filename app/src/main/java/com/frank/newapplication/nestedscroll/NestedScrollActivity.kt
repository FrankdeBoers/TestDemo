package com.frank.newapplication.nestedscroll

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.NestedScrollingParent3
import androidx.core.view.NestedScrollingParentHelper
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.frank.newapplication.R
import com.frank.newapplication.databinding.ActivityNestedScrollBinding

/**
 * 嵌套滚动演示Activity
 * 展示多种嵌套滚动场景：
 * 1. 外层ScrollView + 内层RecyclerView
 * 2. 外层RecyclerView + 内层RecyclerView
 * 3. 自定义NestedScrollingParent
 */
class NestedScrollActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNestedScrollBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNestedScrollBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNestedScrollDemo()
        setupCustomNestedScrollParent()
    }

    /**
     * 设置嵌套滚动演示
     */
    private fun setupNestedScrollDemo() {
        // 外层ScrollView + 内层RecyclerView
        val outerAdapter = OuterAdapter()
        binding.recyclerViewOuter.apply {
            layoutManager = LinearLayoutManager(this@NestedScrollActivity)
            adapter = outerAdapter
        }

        // 设置嵌套滚动
        binding.recyclerViewOuter.isNestedScrollingEnabled = true
    }

    /**
     * 设置自定义嵌套滚动父容器
     */
    private fun setupCustomNestedScrollParent() {
        binding.customNestedParent.setupContent()
    }

    /**
     * 外层RecyclerView适配器
     */
    private inner class OuterAdapter : RecyclerView.Adapter<OuterAdapter.OuterViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OuterViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_outer_recycler, parent, false)
            return OuterViewHolder(view)
        }

        override fun onBindViewHolder(holder: OuterViewHolder, position: Int) {
            holder.bind(position)
        }

        override fun getItemCount(): Int = 10

        inner class OuterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerViewInner)
            private val titleText: TextView = itemView.findViewById(R.id.titleText)

            fun bind(position: Int) {
                titleText.text = "外层项目 $position"
                
                // 为每个内层RecyclerView设置不同的数据
                val innerAdapter = InnerAdapter(position)
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = innerAdapter
                    isNestedScrollingEnabled = true
                }
            }
        }
    }

    /**
     * 内层RecyclerView适配器
     */
    private inner class InnerAdapter(private val outerPosition: Int) : 
        RecyclerView.Adapter<InnerAdapter.InnerViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_inner_recycler, parent, false)
            return InnerViewHolder(view)
        }

        override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
            holder.bind(position)
        }

        override fun getItemCount(): Int = 15

        inner class InnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val contentText: TextView = itemView.findViewById(R.id.contentText)

            fun bind(position: Int) {
                contentText.text = "内层项目 ${outerPosition}-${position}"
            }
        }
    }
}

/**
 * 自定义嵌套滚动父容器
 * 实现NestedScrollingParent3接口，提供完整的嵌套滚动支持
 */
class CustomNestedScrollParent @JvmOverloads constructor(
    context: android.content.Context,
    attrs: android.util.AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.constraintlayout.widget.ConstraintLayout(context, attrs, defStyleAttr), NestedScrollingParent3 {

    private val nestedScrollingParentHelper = NestedScrollingParentHelper(this)
    private var headerView: View? = null
    private var contentView: View? = null
    private var headerHeight = 0
    private var scrollOffset = 0

    override fun onFinishInflate() {
        super.onFinishInflate()
        // 查找子视图
        if (childCount >= 2) {
            headerView = getChildAt(0)
            contentView = getChildAt(1)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        headerHeight = headerView?.measuredHeight ?: 0
    }

    /**
     * 设置内容
     */
    fun setupContent() {
        // 创建头部视图
        val header = TextView(context).apply {
            text = "这是头部内容，可以滚动隐藏"
            setBackgroundColor(0xFF2196F3.toInt())
            setTextColor(0xFFFFFFFF.toInt())
            textSize = 18f
            gravity = android.view.Gravity.CENTER
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                200
            ).apply {
                topToTop = LayoutParams.PARENT_ID
            }
        }

        // 创建内容视图
        val content = RecyclerView(context).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ContentAdapter()
            isNestedScrollingEnabled = true
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            ).apply {
                topToBottom = header.id
            }
        }

        addView(header)
        addView(content)
        
        headerView = header
        contentView = content
    }

    // NestedScrollingParent3 接口实现
    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        return axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        nestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes, type)
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        nestedScrollingParentHelper.onStopNestedScroll(target, type)
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        // 处理未消费的滚动
        if (dyUnconsumed < 0 && scrollOffset > 0) {
            // 向上滚动，隐藏头部
            val consumedY = minOf(-dyUnconsumed, scrollOffset)
            scrollOffset -= consumedY
            consumed[1] = -consumedY
            updateHeaderTranslation()
        } else if (dyUnconsumed > 0 && scrollOffset < headerHeight) {
            // 向下滚动，显示头部
            val consumedY = minOf(dyUnconsumed, headerHeight - scrollOffset)
            scrollOffset += consumedY
            consumed[1] = consumedY
            updateHeaderTranslation()
        }
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        // 兼容旧版本
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        // 预滚动处理
        if (dy > 0 && scrollOffset < headerHeight) {
            // 向下滚动，优先显示头部
            val consumedY = minOf(dy, headerHeight - scrollOffset)
            scrollOffset += consumedY
            consumed[1] = consumedY
            updateHeaderTranslation()
        } else if (dy < 0 && scrollOffset > 0) {
            // 向上滚动，优先隐藏头部
            val consumedY = minOf(-dy, scrollOffset)
            scrollOffset -= consumedY
            consumed[1] = -consumedY
            updateHeaderTranslation()
        }
    }

    override fun getNestedScrollAxes(): Int {
        return nestedScrollingParentHelper.nestedScrollAxes
    }

    /**
     * 更新头部视图的平移
     */
    private fun updateHeaderTranslation() {
        headerView?.translationY = -scrollOffset.toFloat()
        Log.d("NestedScroll", "Header translation: ${-scrollOffset}")
    }

    /**
     * 内容适配器
     */
    private inner class ContentAdapter : RecyclerView.Adapter<ContentAdapter.ContentViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
            return ContentViewHolder(view)
        }

        override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
            holder.bind(position)
        }

        override fun getItemCount(): Int = 50

        inner class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(position: Int) {
                (itemView as TextView).text = "自定义嵌套滚动内容项目 $position"
            }
        }
    }
} 