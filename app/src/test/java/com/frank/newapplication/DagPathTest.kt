import org.junit.Test
import java.util.Deque
import java.util.LinkedList

// 定义DAG路径测试类
class DagPathTest {
    // 测试方法
    @Test
    fun testAllPaths() {
        // 节点总数
        val n = 6
        // 边集，定义DAG结构
        val edges = arrayOf(
            intArrayOf(2, 1),
            intArrayOf(3, 1),
            intArrayOf(4, 2),
            intArrayOf(5, 2),
            intArrayOf(5, 4)
        )
        println("Frank1## testAllPaths")
        buildGraph(6, edges)
    }

    private fun buildGraph(n: Int, edges: Array<IntArray>) {
        val result = mutableListOf<Int>()
        // 入度表，每一个数对应的入度，为0就可以执行
        val inDegreeMap = hashMapOf<Int, Int>()
        // 输入源是依赖表 [2, 1] [3, 1] []
        // 影响表，每一个数会影响的其他数。 比如 1 影响 2，1 影响 3.
        val affectMap = hashMapOf<Int, MutableList<Int>>()
        // 零度栈，用于最后的遍历
        val zeroDequeue: Deque<Int> = LinkedList<Int>()

        edges.forEach { edge ->
            // [2,1] [3,1]
            // 构建入度表
            var currentDegree = inDegreeMap[edge[0]] ?: 0
            currentDegree++
            inDegreeMap[edge[0]] = currentDegree

            // 构建影响表
            val affectedList = affectMap[edge[1]] ?: mutableListOf()
            affectedList.add(edge[0])
            affectMap[edge[1]] = affectedList
        }

        // 构建零度表
        inDegreeMap.filter { it.value == 0 }.forEach {
            zeroDequeue.offer(it.key)
        }

        println("Frank1## inDegreeMap:" + inDegreeMap)
        println("Frank1## affectMap:" + affectMap)

        // 开始执行零度表
        while (!zeroDequeue.isEmpty()) {
            val first = zeroDequeue.pop()
            result.add(first)

            // 影响表，每一个数会影响的其他数。 比如 1 影响 2，1 影响 3.
            // 现在1执行了
            // 找出受影响的表，入度减一
            affectMap[first]?.forEach { value ->
                // 2、3这样的列表
                val newDegree = (inDegreeMap.get(value) ?: 0) - 1
                inDegreeMap[value] = newDegree
                if (newDegree <= 0) {
                    zeroDequeue.offer(value)
                }
            }
        }

        println("Frank1##" + result)
    }


} 