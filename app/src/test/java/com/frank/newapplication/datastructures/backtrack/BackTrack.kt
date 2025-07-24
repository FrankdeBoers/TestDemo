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
    val result = mutableListOf<List<Int>>()

    fun permute(nums: IntArray): List<List<Int>> {
        // used记录的是某个节点是否被选择过了
        val used = BooleanArray(nums.size)

        // 记录走过的合法路径
        val track = mutableListOf<Int>()
        backtrack(nums, track, used)
        return result
    }

    private fun backtrack(nums: IntArray, track: MutableList<Int>, used: BooleanArray) {
        // 判断当前路径是否已经到达尾部
        if (nums.size == track.size) {
            result.add(track.toList())
        }

        for (i in 0 until nums.size) {
            // 当前节点，在这个路径下，被使用过了，需要跳过
            if (used.getOrNull(i) == true) {
                continue
            }

            // 处理当前节点
            track.add(nums[i])
            used[i] = true

            // 查找下一个节点
            backtrack(nums, track, used)

            used[i] = false
            track.removeLast()
        }

    }
}