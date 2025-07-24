package com.frank.newapplication.datastructures.tree

import java.util.LinkedList
import java.util.Queue


class TreeNode(var `val`: Int) {
    var left: TreeNode? = null
    var right: TreeNode? = null
}

class Solution {

}

class LevelTraversal {
    /// 层序遍历的实现
    fun levelOrder(root: TreeNode?): List<List<Int>> {
        if (root == null) {
            return listOf()
        }
        // 使用queue先进先出
        val queue: Queue<TreeNode> = LinkedList<TreeNode>()
        queue.add(root!!)

        val resultList = mutableListOf<List<Int>>()

        while (queue.isNotEmpty()) {
            val currentLevelSize = queue.size
            val currentLevelList = mutableListOf<Int>()
            // 去除当前层所有的item，对其进行访问
            for (i in 0 until currentLevelSize) {
                val node = queue.poll()!!
                currentLevelList.add(node.`val`)
                if (node.left != null) {
                    queue.add(node.left)
                }

                if (node.right != null) {
                    queue.add(node.right)
                }
            }
            resultList.add(currentLevelList.toList())
        }

        return resultList.toList()
    }
}