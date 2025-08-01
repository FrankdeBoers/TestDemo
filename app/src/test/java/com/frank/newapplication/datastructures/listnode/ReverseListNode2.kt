package com.frank.newapplication.datastructures.listnode

/***
 * https://leetcode.cn/problems/reverse-linked-list-ii/description/
 * 92. 反转链表 II
 * 中等
 * 给你单链表的头指针 head 和两个整数 left 和 right ，其中 left <= right 。请你反转从位置 left 到位置 right 的链表节点，返回 反转后的链表 。
 *
 * 示例 1：
 * 输入：head = [1,2,3,4,5], left = 2, right = 4
 * 输出：[1,4,3,2,5]
 *
 * 示例 2：
 * 输入：head = [5], left = 1, right = 1
 * 输出：[5]
 *
 * 提示：
 * 链表中节点数目为 n
 * 1 <= n <= 500
 * -500 <= Node.val <= 500
 * 1 <= left <= right <= n
 *
 * 进阶： 你可以使用一趟扫描完成反转吗？
 * */

class ReverseListNode2 {
    fun reverseBetween(head: ListNode?, left: Int, right: Int): ListNode? {
        if (left == 1) {
           return reverse(head, right)
        }
        var pre = head
        var start = left
        while (start > 1) {
            start--
            pre = pre?.next
        }
        pre?.next = reverse(pre?.next, right - left)
        return pre
    }

    private fun reverse(head: ListNode?, count: Int): ListNode? {
        if (head == null || head?.next == null) {
            return head
        }
        var n = count
        var current = head
        var pre: ListNode? = null
        var next = head?.next
        while (n > 0) {
            n--
            current?.next = pre
            pre = current
            current = next
            if (next != null) {
                next = next?.next
            }
        }
        head?.next = current
        return pre
    }



}