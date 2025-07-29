package com.frank.newapplication.coroutine

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

/**
 * 协程性能监控辅助类
 * 提供更高级的协程性能监控功能
 */
class CoroutinePerformanceHelper {
    
    companion object {
        private const val TAG = "CoroutinePerformanceHelper"
        
        // 单例实例
        @Volatile
        private var INSTANCE: CoroutinePerformanceHelper? = null
        
        fun getInstance(): CoroutinePerformanceHelper {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: CoroutinePerformanceHelper().also { INSTANCE = it }
            }
        }
    }
    
    // 性能统计数据存储
    private val performanceStats = ConcurrentHashMap<String, PerformanceData>()
    private val totalCoroutineCount = AtomicLong(0)
    private val totalExecutionTime = AtomicLong(0)
    
    // 创建带性能监控的协程作用域
    private val performanceScope = CoroutineScope(
        SupervisorJob() + 
        CoroutinePerformanceInterceptor() + 
        Dispatchers.Default
    )
    
    /**
     * 性能数据类
     */
    data class PerformanceData(
        val coroutineName: String,
        val startTime: Long,
        var endTime: Long = 0,
        var suspendCount: Int = 0,
        var totalSuspendTime: Long = 0,
        var isCompleted: Boolean = false,
        var exception: Throwable? = null
    ) {
        val executionTime: Long
            get() = if (isCompleted) endTime - startTime else System.currentTimeMillis() - startTime
        
        val averageSuspendTime: Long
            get() = if (suspendCount > 0) totalSuspendTime / suspendCount else 0
    }
    
    /**
     * 启动带性能监控的协程
     */
    fun launchWithPerformance(
        name: String = "PerformanceCoroutine",
        block: suspend CoroutineScope.() -> Unit
    ) {
        val coroutineName = "$name-${System.nanoTime()}"
        val performanceData = PerformanceData(coroutineName, System.currentTimeMillis())
        
        performanceStats[coroutineName] = performanceData
        totalCoroutineCount.incrementAndGet()
        
        Log.i(TAG, "🚀 启动性能监控协程: $coroutineName")
        
        performanceScope.launch1 {
            try {
                // 执行协程逻辑
                block()
                
                // 记录成功完成
                performanceData.apply {
                    endTime = System.currentTimeMillis()
                    isCompleted = true
                }
                
                totalExecutionTime.addAndGet(performanceData.executionTime)
                
                Log.i(TAG, "✅ 协程执行成功: $coroutineName")
                Log.i(TAG, "📊 执行详情: 耗时=${performanceData.executionTime}ms, " +
                          "挂起次数=${performanceData.suspendCount}, " +
                          "平均挂起时间=${performanceData.averageSuspendTime}ms")
                
            } catch (e: Exception) {
                // 记录异常完成
                performanceData.apply {
                    endTime = System.currentTimeMillis()
                    isCompleted = true
                    exception = e
                }
                
                totalExecutionTime.addAndGet(performanceData.executionTime)
                
                Log.e(TAG, "❌ 协程执行异常: $coroutineName")
                Log.e(TAG, "💥 异常信息: ${e.message}")
                Log.e(TAG, "📊 执行详情: 耗时=${performanceData.executionTime}ms, " +
                          "挂起次数=${performanceData.suspendCount}")
                
                throw e
            }
        }
    }
    
    /**
     * 模拟挂起操作并记录性能数据
     */
    suspend fun simulateSuspendOperation(
        operationName: String,
        suspendTime: Long = 100L
    ) = withContext(Dispatchers.IO) {
        val coroutineName = "SuspendOperation-$operationName"
        val performanceData = performanceStats.values.find { it.coroutineName.contains(coroutineName) }
        
        performanceData?.let {
            it.suspendCount++
            it.totalSuspendTime += suspendTime
        }
        
        Log.d(TAG, "⏸️ 模拟挂起操作: $operationName, 挂起时间=${suspendTime}ms")
        
        // 模拟耗时操作
        kotlinx.coroutines.delay(suspendTime)
        
        Log.d(TAG, "▶️ 挂起操作完成: $operationName")
    }
    
    /**
     * 获取性能统计报告
     */
    fun getPerformanceReport(): String {
        val completedCoroutines = performanceStats.values.filter { it.isCompleted }
        val runningCoroutines = performanceStats.values.filter { !it.isCompleted }
        
        val avgExecutionTime = if (completedCoroutines.isNotEmpty()) {
            completedCoroutines.map { it.executionTime }.average()
        } else 0.0
        
        val totalSuspendCount = completedCoroutines.sumOf { it.suspendCount }
        val avgSuspendCount = if (completedCoroutines.isNotEmpty()) {
            totalSuspendCount.toDouble() / completedCoroutines.size
        } else 0.0
        
        return buildString {
            appendLine("📈 协程性能统计报告")
            appendLine("==========================================")
            appendLine("总协程数: ${totalCoroutineCount.get()}")
            appendLine("已完成协程: ${completedCoroutines.size}")
            appendLine("运行中协程: ${runningCoroutines.size}")
            appendLine("总执行时间: ${totalExecutionTime.get()}ms")
            appendLine("平均执行时间: ${String.format("%.2f", avgExecutionTime)}ms")
            appendLine("总挂起次数: $totalSuspendCount")
            appendLine("平均挂起次数: ${String.format("%.2f", avgSuspendCount)}")
            appendLine("==========================================")
        }
    }
    
    /**
     * 清理性能统计数据
     */
    fun clearPerformanceStats() {
        performanceStats.clear()
        totalCoroutineCount.set(0)
        totalExecutionTime.set(0)
        Log.i(TAG, "🧹 性能统计数据已清理")
    }
    
    /**
     * 获取指定协程的性能数据
     */
    fun getCoroutinePerformance(coroutineName: String): PerformanceData? {
        return performanceStats[coroutineName]
    }
} 