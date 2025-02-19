import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager

class CustomFlexboxItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.right = 100 // 示例：设置右边距
        Log.i("Test###", "getItemOffsets")
    }

    private fun isLastItemInRow(position: Int, layoutManager: FlexboxLayoutManager, parent: RecyclerView): Boolean {
        var currentRowEnd = -1
        var nextRowStart = -1
        var itemEnd = -1
        Log.d("Test###", "childCount:${parent.childCount} 0:${parent.getChildAt(0)}")
        // 先找到当前 item 所在行的结束位置
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            if (child != null) {
                val childEnd = layoutManager.getDecoratedRight(child)
                Log.d("Test###", "childEnd:$childEnd")
                if (i == position) {
                    itemEnd = childEnd
                }
                if (i > position && nextRowStart == -1 && childEnd > itemEnd) {
                    nextRowStart = childEnd
                    break
                }
                if (childEnd > currentRowEnd) {
                    currentRowEnd = childEnd
                }
            }
        }
        Log.d("Test###", "currentRowEnd:$currentRowEnd, nextRowStart:$nextRowStart")
        // 判断当前 item 是否为本行最后一个
        return itemEnd == currentRowEnd || (nextRowStart != -1 && itemEnd < nextRowStart)
    }
}