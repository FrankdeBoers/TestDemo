package com.frank.newapplication.datastructures.dp

/**
 * https://leetcode.cn/problems/coin-change/description/
 * 322. 零钱兑换
 * 中等
 * 给你一个整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。
 *
 * 计算并返回可以凑成总金额所需的 最少的硬币个数 。如果没有任何一种硬币组合能组成总金额，返回 -1 。
 *
 * 你可以认为每种硬币的数量是无限的。
 *
 * 示例 1：
 * 输入：coins = [1, 2, 5], amount = 11
 * 输出：3
 * 解释：11 = 5 + 5 + 1
 *
 * 示例 2：
 * 输入：coins = [2], amount = 3
 * 输出：-1
 * 示例 3：
 *
 * 输入：coins = [1], amount = 0
 * 输出：0
 *
 * 提示：
 * 1 <= coins.length <= 12
 * 1 <= coins[i] <= 231 - 1
 * 0 <= amount <= 104
 * */

class CoinChange_322 {
    // 存储的是coin对应的最少硬币数量，方便dp
    val map = hashMapOf<Int, Int>()

    fun coinChange(coins: IntArray, amount: Int): Int {

        return dp(coins, amount)
    }

    // dp返回的是输入的target，所需要的最少硬币数
    private fun dp(coins: IntArray, target: Int): Int {
        // 如果目标值是0，则无需再加硬币数
        if (target == 0) {
            return 0
        }

        // 如果目标值已经是负数，说明当前路径已经无法走下去，硬币都是正数，直接返回
        if (target < -1) {
            return -1
        }

        if (map.containsKey(target)) {
            return map[target]!!
        }

        var minCount = Int.MAX_VALUE
        // 有点像回溯全排列，需要从头开始
        // for循环控制的是横向展开
        for (i in 0 until coins.size) {
            val coin = coins[i]
            // 固定一个硬币后，求剩余硬币的最少数
            // 这里是纵向展开
            val subResult = dp(coins, target - coin)
            if (subResult == 0) {
                minCount = Math.min(minCount, subResult)
                map[coin] = minCount
            }
            // 如果小于0，则当前路径不再继续
            if (subResult < 0) {
                // continue控制的是外层for循环，固定下一个数
                continue
            }
        }

        return if (minCount == Int.MAX_VALUE) -1 else minCount
    }

}