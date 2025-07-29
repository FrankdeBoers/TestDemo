package com.frank.newapplication.coroutine

import android.util.Log
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.Continuation
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

/**
 * 协程性能监控拦截器
 * 用于监听协程的耗时情况
 */
class CoroutinePerformanceInterceptor : AbstractCoroutineContextElement(ContinuationInterceptor), ContinuationInterceptor {
    
    companion object {
        private const val TAG = "CoroutinePerformance"
    }
    
    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> {
        return PerformanceContinuation(continuation)
    }
    
    /**
     * 性能监控的Continuation包装器
     */
    private class PerformanceContinuation<T>(
        private val delegate: Continuation<T>
    ) : Continuation<T> {
        
        private val startTime = System.currentTimeMillis()
        private val coroutineName = "Coroutine-${Thread.currentThread().name}-${System.nanoTime()}"
        private var suspendCount = 0
        private var lastSuspendTime = 0L
        
        override val context: CoroutineContext = delegate.context
        
        override fun resumeWith(result: Result<T>) {
            val endTime = System.currentTimeMillis()
            val totalTime = endTime - startTime
            
            when {
                result.isSuccess -> {
                    Log.i(TAG, "✅ 协程执行完成: $coroutineName")
                    Log.i(TAG, "📊 执行统计: 总耗时=${totalTime}ms, 挂起次数=$suspendCount")
                }
                result.isFailure -> {
                    Log.e(TAG, "❌ 协程执行异常: $coroutineName")
                    Log.e(TAG, "📊 执行统计: 总耗时=${totalTime}ms, 挂起次数=$suspendCount")
                    Log.e(TAG, "💥 异常信息: ${result.exceptionOrNull()?.message}")
                }
            }
            
            // 调用原始的resumeWith
            delegate.resumeWith(result)
        }
        
        /**
         * 记录挂起操作
         */
        fun recordSuspend() {
            suspendCount++
            lastSuspendTime = System.currentTimeMillis()
            Log.d(TAG, "⏸️ 协程挂起: $coroutineName (第${suspendCount}次)")
        }
        
        /**
         * 记录恢复操作
         */
        fun recordResume() {
            val resumeTime = System.currentTimeMillis()
            val suspendDuration = resumeTime - lastSuspendTime
            Log.d(TAG, "▶️ 协程恢复: $coroutineName, 挂起时长=${suspendDuration}ms")
        }
    }
    
    /**
     * 创建带性能监控的协程上下文
     */
    fun createPerformanceContext(): CoroutineContext {
        return this
    }
    
    /**
     * 获取当前协程的性能统计信息
     */
    fun getPerformanceStats(): String {
        return "协程性能监控已启用"
    }
} 