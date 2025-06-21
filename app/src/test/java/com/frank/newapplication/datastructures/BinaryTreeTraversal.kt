package com.frank.newapplication.datastructures

import org.junit.Test
import java.util.*

/**
 * Represents a node in a binary tree.
 * @param value The value of the node.
 * @param left The left child of the node.
 * @param right The right child of the node.
 */
data class TreeNode(
    val value: Int,
    var left: TreeNode? = null,
    var right: TreeNode? = null
)

class BinaryTreeTraversalTest {

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

    @Test
    fun testPreOrderTraversal() {
        val resultList = mutableListOf<TreeNode>()
        val root = createSampleTree()
        println("Starting pre-order traversal...")
        // Call your function here
        preOrderTraversal(root, resultList)
        println("\nPre-order traversal finished.")
        resultList.forEach {
            println("Frank## TreeNode## result:${it.value}")
        }
        // Expected output: 1, 2, 4, 5, 3, 6
    }

    /**
     * TODO: Implement the pre-order traversal for the binary tree.
     *
     * Pre-order traversal visits nodes in the following order:
     * 1. Visit the current node (Root).
     * 2. Traverse the left subtree.
     * 3. Traverse the right subtree.
     *
     * For the sample tree, the expected output should be: 1, 2, 4, 5, 3, 6
     *
     * @param root The root node of the binary tree.
     */
    fun preOrderTraversal(root: TreeNode?, resultList: MutableList<TreeNode>) {
        // Your code goes here
        // 终止条件
        if (root == null) {
            return
        }

        println("Frank## Tree## visit:${root.value} start")
        resultList.add(root)
        preOrderTraversal(root.left, resultList)
        preOrderTraversal(root.right, resultList)
        println("Frank## Tree## visit:${root.value} end")
    }

    /**
     * TODO: Implement the post-order traversal for the binary tree.
     *
     * Post-order traversal visits nodes in the following order:
     * 1. Traverse the left subtree.
     * 2. Traverse the right subtree.
     * 3. Visit the current node (Root).
     *
     * For the sample tree, the expected output should be: 4, 5, 2, 6, 3, 1
     *
     * @param root The root node of the binary tree.
     * @param resultList A list to store the traversal result.
     */
    fun postOrderTraversal(root: TreeNode?, resultList: MutableList<TreeNode>) {
        // Your code goes here
        if (root == null) {
            return
        }

        println("TreeNode## postOrder## visit:${root.value} start")
        postOrderTraversal(root.left, resultList)
        postOrderTraversal(root.right, resultList)
        resultList.add(root)
        println("TreeNode## postOrder## visit:${root.value} end")
    }

    @Test
    fun testPostOrderTraversal() {
        val resultList = mutableListOf<TreeNode>()
        val root = createSampleTree()
        println("Starting post-order traversal...")
        // Call your function here
        postOrderTraversal(root, resultList)
        println("\nPost-order traversal finished.")
        val resultValues = resultList.map { it.value }.joinToString(", ")
        println("TreeNode## postOrder## $resultValues")
        // Expected output: 4, 5, 2, 6, 3, 1
    }
} 