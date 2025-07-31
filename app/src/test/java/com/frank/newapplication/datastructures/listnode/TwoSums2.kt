package com.frank.newapplication.datastructures.listnode

import java.util.Deque
import java.util.LinkedList

/***
 * https://leetcode.cn/problems/add-two-numbers-ii/
 * 445. 两数相加 II
 * 中等
 * 给你两个 非空 链表来代表两个非负整数。数字最高位位于链表开始位置。它们的每个节点只存储一位数字。将这两数相加会返回一个新的链表。
 *
 * 你可以假设除了数字 0 之外，这两个数字都不会以零开头。
 *
 * 示例1：
 * 输入：l1 = [7,2,4,3], l2 = [5,6,4]
 * 输出：[7,8,0,7]
 *
 * 示例2：
 * 输入：l1 = [2,4,3], l2 = [5,6,4]
 * 输出：[8,0,7]
 * 示例3：
 *
 * 输入：l1 = [0], l2 = [0]
 * 输出：[0]
 *
 * 提示：
 * 链表的长度范围为 [1, 100]
 * 0 <= node.val <= 9
 * 输入数据保证链表代表的数字无前导 0
 *
 * 进阶：如果输入链表不能翻转该如何解决？
 * */

class TwoSums2 {
    fun addTwoNumbers(l1: ListNode?, l2: ListNode?): ListNode? {
        val stack1: Deque<Int> = LinkedList<Int>()
        val stack2: Deque<Int> = LinkedList<Int>()
        val resultStack: Deque<Int> = LinkedList<Int>()

        var head1 = l1
        var head2 = l2
        var newHead: ListNode? = ListNode(-1)

        val dummy = newHead
        var carry = 0

        while (head1 != null) {
            stack1.push(head1.value())
            head1 = head1.next
        }

        while (head2 != null) {
            stack2.push(head2.value())
            head2 = head2.next
        }

        while (stack1.isNotEmpty() || stack2.isNotEmpty() || carry == 1) {
            var currentValue = stack1.popSafe() + stack2.popSafe() + carry
            carry = 0
            if (currentValue >= 10) {
                currentValue -= 10
                carry = 1
            }

            resultStack.push(currentValue)
        }

        while (resultStack.isNotEmpty()) {
            newHead?.next = ListNode(resultStack.pop())
            newHead = newHead?.next
        }

        return dummy?.next
    }

    private fun ListNode?.value(): Int {
        return this?.`val` ?: 0
    }

    private fun Deque<Int>.popSafe(): Int {
        return if (this.isNotEmpty()) {
            this.pop()
        } else {
            0
        }
    }
}