package com.frank.newapplication.datastructures.backtrack

/**
 * https://leetcode.cn/problems/combination-sum-ii/description/
 * 40. 组合总和 II
 * 中等
 * 给定一个候选人编号的集合 candidates 和一个目标数 target ，找出 candidates 中所有可以使数字和为 target 的组合。
 *
 * candidates 中的每个数字在每个组合中只能使用 一次 。
 *
 * 注意：解集不能包含重复的组合。
 *
 * 示例 1:
 * 输入: candidates = [10,1,2,7,6,1,5], target = 8,
 * 输出:
 * [
 * [1,1,6],
 * [1,2,5],
 * [1,7],
 * [2,6]
 * ]
 *
 * 示例 2:
 * 输入: candidates = [2,5,2,1,2], target = 5,
 * 输出:
 * [
 * [1,2,2],
 * [5]
 * ]
 *
 * 提示:
 *
 * 1 <= candidates.length <= 100
 * 1 <= candidates[i] <= 50
 * 1 <= target <= 30
 * */

class Combination2_40 {
    private val result = mutableListOf<List<Int>>()
    private val track = mutableListOf<Int>()
    private var levelSum = 0

    fun combinationSum2(candidates: IntArray, target: Int): List<List<Int>> {
        candidates.sort()
        backtrack(candidates, target, 0)
        return result
    }

    private fun backtrack(candidates: IntArray, target: Int, start: Int) {
        if (levelSum == target) {
            result.add(track.toList())
        }

        // base case 当前路径总和超过限制，不再继续
        if (levelSum > target) {
            return
        }

        for (i in start until candidates.size) {
            if (i > start && candidates[i - 1] == candidates[i]) {
                continue
            }

            track.add(candidates[i])
            levelSum += candidates[i]

            backtrack(candidates, target, i + 1)

            track.removeLast()
            levelSum -= candidates[i]
        }
    }
}