package com.frank.newapplication.datastructures.listnode

/***
 * https://leetcode.cn/problems/remove-duplicates-from-sorted-list-ii/description/
 * 82. 删除排序链表中的重复元素 II
 * 中等
 * 给定一个已排序的链表的头 head ， 删除原始链表中所有重复数字的节点，只留下不同的数字 。返回 已排序的链表 。
 *
 * 示例 1：
 * 输入：head = [1,2,3,3,4,4,5]
 * 输出：[1,2,5]
 *
 * 示例 2：
 * 输入：head = [1,1,1,2,3]
 * 输出：[2,3]

 * 提示：
 * 链表中节点数目在范围 [0, 300] 内
 * -100 <= Node.val <= 100
 * 题目数据保证链表已经按升序 排列
 *
 * */
class RemoveDumFormList {
    // 双指针，左指针维护符合条件的，右指针移动
    fun deleteDuplicates(head: ListNode?): ListNode? {
        val dummy = ListNode(101)
        dummy.next = head
        var left: ListNode? = dummy
        var right = head

        while (right != null) {
            // 如果相等，则一直找到不相等的
            if (right.next != null && right.`val` == right.next!!.`val`) {
                val dupValue = right.`val`
                while (right != null && right.next?.`val` == dupValue) {
                    right = right.next
                }
                // 找到了不相等的，left指向他
                left?.next = right
                right = right?.next
            } else {
                left = left?.next
                right = right.next
            }
        }
        return dummy.next
    }

}


class ListNode(var `val`: Int) {
    var next: ListNode? = null
}
