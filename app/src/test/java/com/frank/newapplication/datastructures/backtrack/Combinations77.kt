package com.frank.newapplication.datastructures.backtrack
/***
 * 77. 组合
 * https://leetcode.cn/problems/combinations/description/
 * 给定两个整数 n 和 k，返回范围 [1, n] 中所有可能的 k 个数的组合。
 *
 * 你可以按 任何顺序 返回答案。
 *
 * 示例 1：
 *
 * 输入：n = 4, k = 2
 * 输出：
 * [
 *   [2,4],
 *   [3,4],
 *   [2,3],
 *   [1,2],
 *   [1,3],
 *   [1,4],
 * ]
 * 示例 2：
 *
 * 输入：n = 1, k = 1
 * 输出：[[1]]
 *
 *
 * 提示：
 *
 * 1 <= n <= 20
 * 1 <= k <= n
 *
 * */
class Combinations77 {
    private val result = mutableListOf<List<Int>>()
    private val track = mutableListOf<Int>()


    fun combine(n: Int, k: Int): List<List<Int>> {
        backtrack(n + 1, 1, k)
        return result
    }

    private fun backtrack(n: Int, start: Int, k: Int) {
        if (track.size == k) {
            result.add(track.toList())
        }

        for (i in start until n) {
            // 做选择
            track.add(i)

            // 纵向展开
            backtrack(n, i + 1, k)

            // 撤销选择
            track.removeLast()
        }
    }


}