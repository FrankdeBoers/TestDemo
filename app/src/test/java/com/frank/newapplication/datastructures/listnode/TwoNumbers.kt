package com.frank.newapplication.datastructures.listnode

/**
 * https://leetcode.cn/problems/add-two-numbers/description/
 * 2. 两数相加
 * 中等
 * 给你两个 非空 的链表，表示两个非负的整数。它们每位数字都是按照 逆序 的方式存储的，并且每个节点只能存储 一位 数字。
 *
 * 请你将两个数相加，并以相同形式返回一个表示和的链表。
 *
 * 你可以假设除了数字 0 之外，这两个数都不会以 0 开头。
 *
 * 示例 1：
 * 输入：l1 = [2,4,3], l2 = [5,6,4]
 * 输出：[7,0,8]
 * 解释：342 + 465 = 807.
 *
 * 示例 2：
 * 输入：l1 = [0], l2 = [0]
 * 输出：[0]
 *
 * 示例 3：
 * 输入：l1 = [9,9,9,9,9,9,9], l2 = [9,9,9,9]
 * 输出：[8,9,9,9,0,0,0,1]
 *
 * 提示：
 *
 * 每个链表中的节点数在范围 [1, 100] 内
 * 0 <= Node.val <= 9
 * 题目数据保证列表表示的数字不含前导零
 *
 * **/

class TwoNumbers {
    fun addTwoNumbers(l1: ListNode?, l2: ListNode?): ListNode? {
        var newHead: ListNode? = ListNode(-1)
        val dummy = newHead
        var carry = 0
        var head1 = l1
        var head2 = l2
        while (head1 != null || head2 != null) {
            var currentValue = head1.value() + head2.value() + carry
            carry = 0
            if (currentValue >= 10) {
                currentValue -= 10
                carry = 1
            }

            newHead?.next = ListNode(currentValue)
            newHead = newHead?.next

            head1 = head1?.next
            head2 = head2?.next
        }

        if (carry == 1) {
            newHead?.next = ListNode(1)
        }

        return dummy?.next
    }

    private fun ListNode?.value(): Int {
        return this?.`val` ?: 0
    }
}



