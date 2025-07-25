package com.frank.newapplication.datastructures.backtrack

/**
 * https://leetcode.cn/problems/subsets-ii/description/
 * 90. 子集 II
 * 中等
 * 相关标签
 * premium lock icon
 * 相关企业
 * 给你一个整数数组 nums ，其中可能包含重复元素，请你返回该数组所有可能的 子集（幂集）。
 *
 * 解集 不能 包含重复的子集。返回的解集中，子集可以按 任意顺序 排列。 *
 *
 * 示例 1：
 * 输入：nums = [1,2,2]
 * 输出：[[],[1],[1,2],[1,2,2],[2],[2,2]]
 *
 * 示例 2：
 * 输入：nums = [0]
 * 输出：[[],[0]]
 *
 *
 * 提示：
 * 1 <= nums.length <= 10
 * -10 <= nums[i] <= 10
 *
 * **/

class Subset2_90 {
    // 先排序，然后再剪枝
    private val result = mutableListOf<List<Int>>()
    private val track = mutableListOf<Int>()
    fun subsetsWithDup(nums: IntArray): List<List<Int>> {
        nums.sort()
        backtrack(nums, 0)
        return result
    }

    private fun backtrack(nums: IntArray, start: Int) {
        result.add(track.toList())

        for (i in start until nums.size) {
            // 如果数字相同，则继续找下一个不同的
            if (i > start && nums[i - 1] == nums[i]) {
                continue
            }

            track.add(nums[i])

            backtrack(nums, i + 1)

            track.removeLast()
        }
    }
}