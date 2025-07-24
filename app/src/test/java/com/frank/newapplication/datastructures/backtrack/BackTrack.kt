package com.frank.newapplication.datastructures.backtrack

/**
 * https://leetcode.cn/problems/permutations/
 * 46. 全排列
 * 中等
 * 相关标签
 * premium lock icon
 * 相关企业
 * 给定一个不含重复数字的数组 nums ，返回其 所有可能的全排列 。你可以 按任意顺序 返回答案。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [1,2,3]
 * 输出：[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
 * 示例 2：
 *
 * 输入：nums = [0,1]
 * 输出：[[0,1],[1,0]]
 * 示例 3：
 *
 * 输入：nums = [1]
 * 输出：[[1]]
 *
 *
 * */
class BackTrack {
    private val result = mutableListOf<List<Int>>()

    // 输入一组不重复的数字，返回他们的全排列
    fun permute(nums: IntArray): List<List<Int>> {
        // 记录 路径
        val track = mutableListOf<Int>()
        // 路径 中的元素会被标记为true，避免重复使用
        val used = BooleanArray(nums.size)

        backtrack(nums, track, used)

        return result
    }

    // 路径：记录在track中
    // 选择列表：nums 中不存在track的那些元素（used[i] = false）
    // 结束条件：nums 中的元素全部都在track中出现
    private fun backtrack(nums: IntArray, track: MutableList<Int>, used: BooleanArray) {
        // 触发结束条件
        if (track.size == nums.size) {
            result.add(track.toList())
            return
        }

        for (i in 0 until nums.size) {
            // 排除不合法的选择
            if (used.getOrNull(i) == true) {
                // nums[i] 已经在track中，跳过
                continue
            }

            // 做选择
            track.add(nums[i])
            used[i] = true

            // 进入下一层决策树
            backtrack(nums, track, used)

            // 取消选择
            track.removeLast()
            used[i] = false
        }
    }
}