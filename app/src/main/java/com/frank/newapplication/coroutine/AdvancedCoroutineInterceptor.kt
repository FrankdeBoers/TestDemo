package com.frank.newapplication.coroutine

import android.util.Log
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.Continuation
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

/**
 * 高级协程性能监控拦截器
 * 提供更详细的协程执行监控和性能分析
 */
class AdvancedCoroutineInterceptor : AbstractCoroutineContextElement(ContinuationInterceptor), ContinuationInterceptor {
    
    companion object {
        private const val TAG = "AdvancedCoroutineInterceptor"

        /**
         * 获取当前线程的简化堆栈跟踪
         */
        fun getStackTrace(): String {
            val stackTrace = Thread.currentThread().stackTrace
            return stackTrace.take(5).joinToString(" -> ") {
                "${it.className}.${it.methodName}:${it.lineNumber}"
            }
        }
        
        // 全局统计
        private val totalCoroutines = AtomicLong(0)
        private val activeCoroutines = AtomicLong(0)
        private val totalExecutionTime = AtomicLong(0)
        private val totalSuspendCount = AtomicLong(0)
        
        // 协程执行记录
        private val coroutineRecords = ConcurrentHashMap<String, CoroutineRecord>()
        
        /**
         * 获取全局统计信息
         */
        fun getGlobalStats(): String {
            return buildString {
                appendLine("🌐 全局协程统计")
                appendLine("总协程数: ${totalCoroutines.get()}")
                appendLine("活跃协程数: ${activeCoroutines.get()}")
                appendLine("总执行时间: ${totalExecutionTime.get()}ms")
                appendLine("总挂起次数: ${totalSuspendCount.get()}")
                appendLine("平均执行时间: ${if (totalCoroutines.get() > 0) totalExecutionTime.get() / totalCoroutines.get() else 0}ms")
                appendLine("平均挂起次数: ${if (totalCoroutines.get() > 0) totalSuspendCount.get() / totalCoroutines.get() else 0}")
            }
        }
        
        /**
         * 清理所有统计数据
         */
        fun clearAllStats() {
            coroutineRecords.clear()
            totalCoroutines.set(0)
            activeCoroutines.set(0)
            totalExecutionTime.set(0)
            totalSuspendCount.set(0)
            Log.i(TAG, "🧹 所有统计数据已清理")
        }
    }
    
    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> {
        return AdvancedContinuation(continuation)
    }
    
    /**
     * 协程执行记录
     */
    data class CoroutineRecord(
        val id: String,
        val name: String,
        val startTime: Long,
        var endTime: Long = 0,
        var suspendCount: Int = 0,
        var totalSuspendTime: Long = 0,
        var lastSuspendTime: Long = 0,
        var isCompleted: Boolean = false,
        var exception: Throwable? = null,
        val threadName: String = Thread.currentThread().name,
        val stackTrace: String = getStackTrace()
    ) {
        val executionTime: Long
            get() = if (isCompleted) endTime - startTime else System.currentTimeMillis() - startTime
        
        val averageSuspendTime: Long
            get() = if (suspendCount > 0) totalSuspendTime / suspendCount else 0
        
        val isSlow: Boolean
            get() = executionTime > 1000 // 超过1秒认为是慢协程
    }
    
    /**
     * 高级协程包装器
     */
    private class AdvancedContinuation<T>(
        private val delegate: Continuation<T>
    ) : Continuation<T> {
        
        private val coroutineId = "Coroutine-${System.nanoTime()}"
        private val coroutineName = "AdvancedCoroutine-${Thread.currentThread().name}"
        private val record = CoroutineRecord(
            id = coroutineId,
            name = coroutineName,
            startTime = System.currentTimeMillis()
        )
        
        init {
            coroutineRecords[coroutineId] = record
            totalCoroutines.incrementAndGet()
            activeCoroutines.incrementAndGet()
            
            Log.i(TAG, "🚀 高级协程启动: $coroutineName")
            Log.d(TAG, "📍 启动位置: ${record.stackTrace}")
        }
        
        override val context: CoroutineContext = delegate.context
        
        override fun resumeWith(result: Result<T>) {
            val endTime = System.currentTimeMillis()
            record.apply {
                this.endTime = endTime
                isCompleted = true
            }
            
            activeCoroutines.decrementAndGet()
            totalExecutionTime.addAndGet(record.executionTime)
            totalSuspendCount.addAndGet(record.suspendCount.toLong())
            
            when {
                result.isSuccess -> {
                    Log.i(TAG, "✅ 高级协程完成: $coroutineName")
                    logPerformanceDetails(record, "成功")
                }
                result.isFailure -> {
                    record.exception = result.exceptionOrNull()
                    Log.e(TAG, "❌ 高级协程异常: $coroutineName")
                    Log.e(TAG, "💥 异常: ${result.exceptionOrNull()?.message}")
                    logPerformanceDetails(record, "异常")
                }
            }
            
            // 如果是慢协程，输出警告
            if (record.isSlow) {
                Log.w(TAG, "⚠️ 检测到慢协程: $coroutineName, 耗时=${record.executionTime}ms")
                Log.w(TAG, "📍 慢协程位置: ${record.stackTrace}")
            }
            
            delegate.resumeWith(result)
        }
        
        /**
         * 记录挂起操作
         */
        fun recordSuspend() {
            record.suspendCount++
            record.lastSuspendTime = System.currentTimeMillis()
            Log.d(TAG, "⏸️ 协程挂起: $coroutineName (第${record.suspendCount}次)")
        }
        
        /**
         * 记录恢复操作
         */
        fun recordResume() {
            val resumeTime = System.currentTimeMillis()
            val suspendDuration = resumeTime - record.lastSuspendTime
            record.totalSuspendTime += suspendDuration
            
            Log.d(TAG, "▶️ 协程恢复: $coroutineName, 挂起时长=${suspendDuration}ms")
        }
        
        /**
         * 记录性能详情
         */
        private fun logPerformanceDetails(record: CoroutineRecord, status: String) {
            Log.i(TAG, "📊 性能详情 [$status]:")
            Log.i(TAG, "   - 协程ID: ${record.id}")
            Log.i(TAG, "   - 执行时间: ${record.executionTime}ms")
            Log.i(TAG, "   - 挂起次数: ${record.suspendCount}")
            Log.i(TAG, "   - 总挂起时间: ${record.totalSuspendTime}ms")
            Log.i(TAG, "   - 平均挂起时间: ${record.averageSuspendTime}ms")
            Log.i(TAG, "   - 执行线程: ${record.threadName}")
        }
    }
    
    /**
     * 获取指定协程的记录
     */
    fun getCoroutineRecord(coroutineId: String): CoroutineRecord? {
        return coroutineRecords[coroutineId]
    }
    
    /**
     * 获取所有慢协程
     */
    fun getSlowCoroutines(): List<CoroutineRecord> {
        return coroutineRecords.values.filter { it.isSlow }
    }
    
    /**
     * 获取活跃协程列表
     */
    fun getActiveCoroutines(): List<CoroutineRecord> {
        return coroutineRecords.values.filter { !it.isCompleted }
    }
    
    /**
     * 获取协程执行统计
     */
    fun getCoroutineStats(): String {
        val completed = coroutineRecords.values.filter { it.isCompleted }
        val active = coroutineRecords.values.filter { !it.isCompleted }
        val slow = getSlowCoroutines()
        
        return buildString {
            appendLine("📈 协程执行统计")
            appendLine("==========================================")
            appendLine("已完成协程: ${completed.size}")
            appendLine("活跃协程: ${active.size}")
            appendLine("慢协程: ${slow.size}")
            appendLine("平均执行时间: ${if (completed.isNotEmpty()) completed.map { it.executionTime }.average() else 0.0}ms")
            appendLine("平均挂起次数: ${if (completed.isNotEmpty()) completed.map { it.suspendCount }.average() else 0.0}")
            appendLine("==========================================")
            
            if (slow.isNotEmpty()) {
                appendLine("🐌 慢协程列表:")
                slow.forEach { record ->
                    appendLine("  - ${record.name}: ${record.executionTime}ms")
                }
            }
        }
    }


} 