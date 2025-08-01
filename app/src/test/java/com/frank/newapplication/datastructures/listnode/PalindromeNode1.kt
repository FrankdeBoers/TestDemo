package com.frank.newapplication.datastructures.listnode

/**
 * https://leetcode.cn/problems/palindrome-linked-list/
 * 234. 回文链表
 * 给你一个单链表的头节点 head ，请你判断该链表是否为回文链表。如果是，返回 true ；否则，返回 false 。
 * 示例 1：
 * 输入：head = [1,2,2,1]
 * 输出：true
 */

class PalindromeNode1 {

    // 方案1 使用额外空间
    fun isPalindrome(head: ListNode?): Boolean {
        val list = mutableListOf<Int>()
        var newHead = head
        while (newHead != null) {
            list.add(newHead.`val`)
            newHead = newHead.next
        }

        var left = 0
        var right = list.size
        while (left <= right) {
            if (list[left] != list[right]) {
                return false
            }
            left++
            right--
        }
        return true
    }

    var left: ListNode? = null
    var result = true

    fun isPalindrome1(head: ListNode?): Boolean {
        left = head
        traversal(head)
        return result
    }

    private fun traversal(head: ListNode?) {
        if (head == null) {
            return
        }
        traversal(head.next)
        if (left.value() != head.value()) {
            result = false
            return
        }
    }

    private fun ListNode?.value(): Int {
        return this?.`val` ?: -1
    }


}