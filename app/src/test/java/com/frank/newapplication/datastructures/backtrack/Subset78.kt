package com.frank.newapplication.datastructures.backtrack

/**
 * https://leetcode.cn/problems/subsets/description/
 * 78. 子集
 * 中等
 * 相关标签
 * premium lock icon
 * 相关企业
 * 给你一个整数数组 nums ，数组中的元素 互不相同 。返回该数组所有可能的子集（幂集）。
 *
 * 解集 不能 包含重复的子集。你可以按 任意顺序 返回解集。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [1,2,3]
 * 输出：[[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]
 * 示例 2：
 *
 * 输入：nums = [0]
 * 输出：[[],[0]]
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 10
 * -10 <= nums[i] <= 10
 * nums 中的所有元素 互不相同
 *
 * */

class Subset78 {
    val result = mutableListOf<List<Int>>()
    val track = mutableListOf<Int>()

    fun subsets(nums: IntArray): List<List<Int>> {
        result.add(emptyList())
        backtrack(nums, 0)
        return result
    }

    private fun backtrack(nums: IntArray, startIndex: Int) {

        // 前序位置
        result.add(track.toList())

        for (i in startIndex until nums.size) {
            // 访问当前节点，记录新值
            track.add(nums[i])

            backtrack(nums, i + 1)

            // 离开当前节点
            track.removeLast()
        }

    }
}