package com.frank.newapplication

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }


        fun sortArray(nums: IntArray): IntArray {
            quickSort(nums, 0, nums.size - 1)
            return nums
        }

        private fun quickSort(nums: IntArray, start: Int, end: Int) {
            if (start < end) {
                val p = part(nums, start, end)
                quickSort(nums, start, p - 1)
                quickSort(nums, p + 1, end)
            }

        }

        // 分区函数：将数组划分为小于基准值和大于基准值的两部分
        private fun part(nums: IntArray, start: Int, end: Int): Int {
            // 选择start位置为基准值（固定mid=start，避免混淆）
            val mid = start
            val baseValue = nums[mid]
            var left = start
            var right = end

            while (left < right) {
                // 先移动右指针：跳过所有≥基准值的元素
                while (left < right && nums[right] >= baseValue) {
                    right--
                }
                // 再移动左指针：跳过所有≤基准值的元素
                while (left < right && nums[left] <= baseValue) {
                    left++
                }
                // 交换不满足条件的左右元素
                if (left < right) {
                    swap(nums, left, right)
                }
            }

            // ---- 关键修正点 ----
            // 此时left==right，且指向最后一个≤baseValue的元素
            // 将基准值交换到正确位置（left处）
            swap(nums, mid, left)
            return left // 返回基准值最终位置
        }

        private fun swap(nums: IntArray, index1: Int, index2: Int) {
            val temp = nums[index1]
            nums[index1] = nums[index2]
            nums[index2] = temp
        }

}