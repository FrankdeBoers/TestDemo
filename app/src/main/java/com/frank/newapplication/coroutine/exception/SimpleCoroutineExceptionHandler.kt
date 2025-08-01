package com.frank.newapplication.coroutine.exception

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

/**
 * 简单的全局协程异常处理器
 * 直接处理所有协程异常，避免应用崩溃
 */
class SimpleCoroutineExceptionHandler : AbstractCoroutineContextElement(CoroutineExceptionHandler), CoroutineExceptionHandler {
    override val key = CoroutineExceptionHandler
    companion object {
        private const val TAG = "SimpleCoroutineExceptionHandler"
        
        // 单例实例
        @Volatile
        private var INSTANCE: SimpleCoroutineExceptionHandler? = null
        
        fun getInstance(): SimpleCoroutineExceptionHandler {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SimpleCoroutineExceptionHandler().also { INSTANCE = it }
            }
        }
    }
    
    /**
     * 处理协程异常
     */
    override fun handleException(context: CoroutineContext, exception: Throwable) {
        // 记录异常信息
        android.util.Log.e(TAG, "捕获到协程异常", exception)
        
        // 根据异常类型进行简单处理
        when (exception) {
            is OutOfMemoryError -> {
                android.util.Log.e(TAG, "内存不足异常，建议清理内存")
                System.gc()
            }
            is SecurityException -> {
                android.util.Log.e(TAG, "安全异常，可能是权限问题")
            }
            is IllegalArgumentException -> {
                android.util.Log.e(TAG, "参数异常，请检查传入的参数")
            }
            is IllegalStateException -> {
                android.util.Log.e(TAG, "状态异常，请检查对象状态")
            }
            is NullPointerException -> {
                android.util.Log.e(TAG, "空指针异常，请检查对象是否为空")
            }
            is ArithmeticException -> {
                android.util.Log.e(TAG, "算术异常，请检查数学运算")
            }
            is ClassCastException -> {
                android.util.Log.e(TAG, "类型转换异常，请检查类型转换")
            }
            is ArrayIndexOutOfBoundsException -> {
                android.util.Log.e(TAG, "数组越界异常，请检查数组索引")
            }
            is StringIndexOutOfBoundsException -> {
                android.util.Log.e(TAG, "字符串越界异常，请检查字符串索引")
            }
            is NumberFormatException -> {
                android.util.Log.e(TAG, "数字格式异常，请检查数字格式")
            }
            is InterruptedException -> {
                android.util.Log.w(TAG, "中断异常，协程被中断")
                Thread.currentThread().interrupt()
            }
            else -> {
                android.util.Log.e(TAG, "其他异常: ${exception.javaClass.simpleName}")
            }
        }
        
        // 可以在这里添加异常上报逻辑
        // reportException(exception)
    }
} 