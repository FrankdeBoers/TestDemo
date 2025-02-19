
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import kotlin.math.max

class WrapLayoutManager : RecyclerView.LayoutManager() {
    private val lineMap = hashMapOf<Int, Int>()

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

            val childWidth = getDecoratedMeasuredWidth(child)
            val childHeight = getDecoratedMeasuredHeight(child)

            // 换行逻辑
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