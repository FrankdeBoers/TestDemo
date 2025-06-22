package com.frank.newapplication.datastructures

import org.junit.Test
import java.util.LinkedList
import java.util.Queue

// Note: The TreeNode data class is already defined in BinaryTreeTraversal.kt
// in the same package, so we can reuse it here without re-declaring it.

class BinaryTreeBFSTest {

    /**
     * Creates and returns the root of a sample binary tree.
     * The tree structure is:
     *      1
     *     / \
     *    2   3
     *   / \   \
     *  4   5   6
     */
    private fun createSampleTree(): TreeNode {
        val root = TreeNode(1)
        root.left = TreeNode(2)
        root.right = TreeNode(3)
        root.left?.left = TreeNode(4)
        root.left?.right = TreeNode(5)
        root.right?.right = TreeNode(6)
        return root
    }

    /**
     * TODO: Implement the level-order traversal (BFS) for the binary tree.
     *
     * Level-order traversal visits nodes level by level, from left to right within each level.
     * This is typically implemented using a queue.
     *
     * Algorithm:
     * 1. Create a queue and add the root node to it if it's not null.
     * 2. While the queue is not empty:
     *    a. Dequeue a node.
     *    b. Visit the node (add its value to the result list).
     *    c. Enqueue its left child if it exists.
     *    d. Enqueue its right child if it exists.
     *
     * For the sample tree, the expected output should be: [1, 2, 3, 4, 5, 6]
     *
     * @param root The root node of the binary tree.
     * @return A list of integers representing the nodes' values in level-order.
     */
    fun levelOrderTraversal(root: TreeNode?): List<Int> {
        // Your BFS code goes here
        val resultList = mutableListOf<Int>()
        var depth = 1

        // 队列
        val queue: Queue<TreeNode> = LinkedList()
        queue.offer(root)
        while (queue.isNotEmpty()) {
            val size = queue.size
            // 遍历完当前层，depth加一
            for (i in 0 until size) {
                val current = queue.poll()
                if (current != null) {
                    resultList.add(current.value)
//                    println("LevelOrder## value:${current.value}, depth:$depth")
                    queue.offer(current.left)
                    queue.offer(current.right)
                }
                println("LevelOrder## value:${current?.value} queue:${queue.mapNotNull { it?.value }}")
            }
            depth++
        }

        return resultList // Placeholder return
    }

    @Test
    fun testLevelOrderTraversal() {
        val root = createSampleTree()
        println("Starting level-order traversal (BFS)...")

        // Call your function here
        val result = levelOrderTraversal(root)

        println("Level-order traversal finished.")
        println("LevelOrder## Result: $result")
        // Expected output: [1, 2, 3, 4, 5, 6]
    }
} 