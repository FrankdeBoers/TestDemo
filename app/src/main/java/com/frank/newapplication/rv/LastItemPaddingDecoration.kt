import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexboxcustom.FlexboxLayoutManager

class LastItemPaddingDecoration(context: Context, private val layoutManager: FlexboxLayoutManager) : RecyclerView.ItemDecoration() {
    // 从资源文件中获取 paddingEnd 的数值（例如 16dp）
    private val paddingEnd = 60

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) return

        // 获取所有 FlexLine（每行的布局信息）
        val flexLines = layoutManager.flexLines
        var currentPosition = 0
        var i = 0
        for (flexLine in flexLines) {
            val lineEnd = currentPosition + flexLine.itemCount - 1
            Log.d("Test###", "LastItemPaddingDecoration i:$i, firstIndex:${flexLine.firstIndex}, itemCount:${flexLine.itemCount}, ")
            i++
            if (position <= lineEnd) {
                // 如果是当前行的最后一个 Item，设置 paddingEnd
                if (position == lineEnd) {
                    outRect.right = paddingEnd
                }
                break
            }
            currentPosition += flexLine.itemCount
        }
    }
}