import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import kotlin.math.max

class WrapLayoutManager : RecyclerView.LayoutManager() {
    private val lineMap = hashMapOf<Int, Int>()

    private val customPaddingEnd = 100
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        lineMap.clear()
        // 上次绘制时，所在的行数，用于判断是否换行了
        // 如果换行了，说明
        var preLine = 0
        var currentWidth = paddingLeft
        var currentHeight = paddingTop
        var lineHeight = 0

        for (i in 0 until itemCount) {
            val child = recycler.getViewForPosition(i)
            addView(child)
            measureChildWithMargins(child, 0, 0)

            var childWidth = getDecoratedMeasuredWidth(child)
            val childHeight = getDecoratedMeasuredHeight(child)

            /**
             * 不加paddingEnd      加paddingEnd         结果：是否加paddingEnd
             * 不换行               换行                 不加
             * 不换行               不换行                加
             * 换行                 换行                 加
             * 换行                 不换行               此情况不存在
             * 单View超过总宽        单View超过总宽        不加
             *
             * 默认是要加paddingEnd的
             */
            // 不加end不换行，加了end导致了换行；只有这种情况不能加end。其他情况都加
            val isNeedPaddingEnd = childWidth < width - paddingRight && // 单View不超过总宽
                    !(currentWidth + childWidth < width - paddingRight && currentWidth + childWidth + customPaddingEnd > width - paddingRight) // 加了end后不会导致额外换行
            Log.d("Test###", "i:$i, isNeedPaddingEnd:$isNeedPaddingEnd")
            if (isNeedPaddingEnd) {
                childWidth += customPaddingEnd
            }
            // 换行逻辑
            // 当前行的总宽度超过了父View，需要换行，设置padding
            if (currentWidth + childWidth > width - paddingRight) {
                preLine += 1
                currentWidth = paddingLeft
                currentHeight += lineHeight
                lineHeight = 0
            }
            lineMap[i] = preLine
            Log.d("Test###", "i:$i, currentHeight:$currentHeight")
            // 布局子项
            layoutDecorated(
                child, currentWidth, currentHeight,
                currentWidth + childWidth, currentHeight + childHeight
            )

            currentWidth += childWidth
            lineHeight = max(lineHeight.toDouble(), childHeight.toDouble()).toInt()
        }

        // 设置 RecyclerView 的总高度以支持垂直滚动
        setMeasuredDimension(width, currentHeight + lineHeight + paddingBottom)
        lineMap.forEach { key, value ->
            Log.d("Test###", "key:$key, value:$value")
        }
    }

    override fun canScrollVertically(): Boolean {
        return true // 允许垂直滚动
    }
}