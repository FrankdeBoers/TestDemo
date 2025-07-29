package com.frank.newapplication.coroutine

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicLong

/**
 * 协程性能分析器
 * 专门用于分析getCallerClassName等方法的性能表现
 */
object CoroutinePerformanceAnalyzer {
    
    private const val TAG = "CoroutinePerformanceAnalyzer"
    
    /**
     * 性能测试结果数据类
     */
    data class PerformanceResult(
        val methodName: String,
        val totalCalls: Int,
        val totalTime: Long, // 纳秒
        val averageTime: Double, // 纳秒
        val minTime: Long, // 纳秒
        val maxTime: Long, // 纳秒
        val p50Time: Long, // 纳秒
        val p90Time: Long, // 纳秒
        val p95Time: Long, // 纳秒
        val p99Time: Long // 纳秒
    ) {
        override fun toString(): String {
            return buildString {
                appendLine("📊 $methodName 性能测试结果")
                appendLine("=============================")
                appendLine("总调用次数: $totalCalls")
                appendLine("总耗时: ${formatTime(totalTime)}")
                appendLine("平均耗时: ${formatTime(averageTime.toLong())}")
                appendLine("最小耗时: ${formatTime(minTime)}")
                appendLine("最大耗时: ${formatTime(maxTime)}")
                appendLine("P50耗时: ${formatTime(p50Time)}")
                appendLine("P90耗时: ${formatTime(p90Time)}")
                appendLine("P95耗时: ${formatTime(p95Time)}")
                appendLine("P99耗时: ${formatTime(p99Time)}")
                appendLine("=============================")
            }
        }
        
        /**
         * 格式化时间显示，智能选择单位
         */
        private fun formatTime(nanoseconds: Long): String {
            return when {
                nanoseconds >= 1_000_000 -> {
                    val milliseconds = nanoseconds / 1_000_000.0
                    String.format("%.3fms", milliseconds)
                }
                nanoseconds >= 1_000 -> {
                    val microseconds = nanoseconds / 1_000.0
                    String.format("%.3fμs", microseconds)
                }
                else -> {
                    String.format("%dns", nanoseconds)
                }
            }
        }
        
        /**
         * 格式化时间显示（纳秒）
         */
        private fun formatTime(nanoseconds: Double): String {
            return when {
                nanoseconds >= 1_000_000 -> {
                    val milliseconds = nanoseconds / 1_000_000.0
                    String.format("%.3fms", milliseconds)
                }
                nanoseconds >= 1_000 -> {
                    val microseconds = nanoseconds / 1_000.0
                    String.format("%.3fμs", microseconds)
                }
                else -> {
                    String.format("%.0fns", nanoseconds)
                }
            }
        }
    }
    
    /**
     * 分析getCallerClassName的性能
     */
    suspend fun analyzeGetCallerClassNamePerformance(
        iterations: Int = 1000,
        warmupIterations: Int = 100
    ): PerformanceResult = withContext(Dispatchers.Default) {
        
        Log.i(TAG, "开始分析getCallerClassName性能，迭代次数: $iterations")
        
        // 预热
        repeat(warmupIterations) {
            CoroutineUtils.getCallerClassName()
        }
        
        // 实际测试
        val times = mutableListOf<Long>()

        
        repeat(iterations) {
            val startTime = System.nanoTime()
            CoroutineUtils.getCallerClassName()
            val endTime = System.nanoTime()
            times.add(endTime - startTime) // 直接使用纳秒
        }
        
        // 计算统计信息
        times.sort()
        val totalTime = times.sum()
        val averageTime = times.average()
        val minTime = times.first()
        val maxTime = times.last()
        val p50Time = times[times.size * 50 / 100]
        val p90Time = times[times.size * 90 / 100]
        val p95Time = times[times.size * 95 / 100]
        val p99Time = times[times.size * 99 / 100]
        
        PerformanceResult(
            methodName = "getCallerClassName",
            totalCalls = iterations,
            totalTime = totalTime,
            averageTime = averageTime,
            minTime = minTime,
            maxTime = maxTime,
            p50Time = p50Time,
            p90Time = p90Time,
            p95Time = p95Time,
            p99Time = p99Time
        )
    }
    
    /**
     * 分析不同方法的性能对比
     */
    suspend fun compareMethodsPerformance(
        iterations: Int = 1000
    ): List<PerformanceResult> = withContext(Dispatchers.Default) {
        
        Log.i(TAG, "开始对比不同方法的性能")
        
        val results = mutableListOf<PerformanceResult>()
        
        // 测试getCallerClassName
        results.add(analyzeGetCallerClassNamePerformance(iterations, 100))
        
        // 测试getCallerFullClassName
        results.add(analyzeMethodPerformance("getCallerFullClassName", iterations) {
            CoroutineUtils.getCallerFullClassName()
        })
        
        // 测试getCallerInfo
        results.add(analyzeMethodPerformance("getCallerInfo", iterations) {
            CoroutineUtils.getCallerInfo()
        })
        
        // 测试优化版本
        results.add(analyzeMethodPerformance("getCallerClassNameOptimized", iterations) {
            CoroutineUtils.getCallerClassNameOptimized()
        })
        
        results
    }
    
    /**
     * 分析指定方法的性能
     */
    private suspend fun analyzeMethodPerformance(
        methodName: String,
        iterations: Int,
        method: () -> Any
    ): PerformanceResult = withContext(Dispatchers.Default) {
        
        // 预热
        repeat(100) {
            method()
        }
        
        // 实际测试
        val times = mutableListOf<Long>()
        
        repeat(iterations) {
            val startTime = System.nanoTime()
            method()
            val endTime = System.nanoTime()
            times.add(endTime - startTime) // 直接使用纳秒
        }
        
        // 计算统计信息
        times.sort()
        val totalTime = times.sum()
        val averageTime = times.average()
        val minTime = times.first()
        val maxTime = times.last()
        val p50Time = times[times.size * 50 / 100]
        val p90Time = times[times.size * 90 / 100]
        val p95Time = times[times.size * 95 / 100]
        val p99Time = times[times.size * 99 / 100]
        
        PerformanceResult(
            methodName = methodName,
            totalCalls = iterations,
            totalTime = totalTime,
            averageTime = averageTime,
            minTime = minTime,
            maxTime = maxTime,
            p50Time = p50Time,
            p90Time = p90Time,
            p95Time = p95Time,
            p99Time = p99Time
        )
    }
    
    /**
     * 分析不同调用深度的性能影响
     */
    suspend fun analyzeCallDepthPerformance(
        maxDepth: Int = 10,
        iterationsPerDepth: Int = 100
    ): List<PerformanceResult> = withContext(Dispatchers.Default) {
        
        Log.i(TAG, "开始分析调用深度对性能的影响")
        
        val results = mutableListOf<PerformanceResult>()
        
        for (depth in 1..maxDepth) {
            val result = analyzeCallDepthPerformance1(depth, iterationsPerDepth)
            results.add(result)
        }
        
        results
    }
    
    /**
     * 分析指定调用深度的性能
     */
    private suspend fun analyzeCallDepthPerformance1(
        depth: Int,
        iterations: Int
    ): PerformanceResult = withContext(Dispatchers.Default) {
        
        val times = mutableListOf<Long>()
        
        repeat(iterations) {
            val startTime = System.nanoTime()
            callWithDepth(depth) {
                CoroutineUtils.getCallerClassName()
            }
            val endTime = System.nanoTime()
            times.add(endTime - startTime) // 直接使用纳秒
        }
        
        times.sort()
        val totalTime = times.sum()
        val averageTime = times.average()
        val minTime = times.first()
        val maxTime = times.last()
        val p50Time = times[times.size * 50 / 100]
        val p90Time = times[times.size * 90 / 100]
        val p95Time = times[times.size * 95 / 100]
        val p99Time = times[times.size * 99 / 100]
        
        PerformanceResult(
            methodName = "getCallerClassName(depth=$depth)",
            totalCalls = iterations,
            totalTime = totalTime,
            averageTime = averageTime,
            minTime = minTime,
            maxTime = maxTime,
            p50Time = p50Time,
            p90Time = p90Time,
            p95Time = p95Time,
            p99Time = p99Time
        )
    }
    
    /**
     * 递归调用指定深度
     */
    private fun callWithDepth(depth: Int, action: () -> Unit) {
        if (depth <= 1) {
            action()
        } else {
            callWithDepth(depth - 1, action)
        }
    }
    
    /**
     * 生成性能报告
     */
    fun generatePerformanceReport(results: List<PerformanceResult>): String {
        return buildString {
            appendLine("🚀 协程性能分析报告")
            appendLine("=============================")
            appendLine("测试时间: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date())}")
            appendLine("设备信息: ${android.os.Build.MODEL} (${android.os.Build.VERSION.RELEASE})")
            appendLine("=============================")
            
            results.forEach { result ->
                appendLine(result.toString())
                appendLine()
            }
            
            // 性能对比
            if (results.size > 1) {
                appendLine("📈 性能对比分析")
                appendLine("=============================")
                val sortedByAvg = results.sortedBy { it.averageTime }
                sortedByAvg.forEachIndexed { index, result ->
                    appendLine("${index + 1}. ${result.methodName}: ${formatTime(result.averageTime.toLong())}")
                }
                appendLine("=============================")
            }
        }
    }
    
    /**
     * 格式化时间显示，智能选择单位
     */
    private fun formatTime(nanoseconds: Long): String {
        return when {
            nanoseconds >= 1_000_000 -> {
                val milliseconds = nanoseconds / 1_000_000.0
                String.format("%.3fms", milliseconds)
            }
            nanoseconds >= 1_000 -> {
                val microseconds = nanoseconds / 1_000.0
                String.format("%.3fμs", microseconds)
            }
            else -> {
                String.format("%dns", nanoseconds)
            }
        }
    }
} 