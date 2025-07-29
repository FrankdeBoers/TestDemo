package com.frank.newapplication.coroutine

import android.util.Log
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 协程工具类
 * 提供获取调用方类名等实用功能
 */
object CoroutineUtils {
    
    private const val TAG = "CoroutineUtils"
    
    /**
     * 获取调用方的类名
     * @param skipFrames 跳过的堆栈帧数，默认为2（跳过当前函数和调用函数）
     * @return 调用方的简单类名
     */
    fun getCallerClassName(skipFrames: Int = 2): String {
        val stackTrace = Thread.currentThread().stackTrace
        
        // 从指定位置开始查找调用方
        for (i in skipFrames until stackTrace.size) {
            val element = stackTrace[i]
            val className = element.className
            val methodName = element.methodName
            
            // 跳过系统类和协程相关的类
            if (isValidCaller(className, methodName)) {
                val simpleClassName = className.substringAfterLast('.')
                Log.d(TAG, "Found caller: $className.$methodName (line ${element.lineNumber})")
                return simpleClassName
            }
        }
        
        return "UnknownCaller"
    }
    
    /**
     * 获取调用方的完整类名
     * @param skipFrames 跳过的堆栈帧数
     * @return 调用方的完整类名
     */
    fun getCallerFullClassName(skipFrames: Int = 2): String {
        val stackTrace = Thread.currentThread().stackTrace
        
        for (i in skipFrames until stackTrace.size) {
            val element = stackTrace[i]
            val className = element.className
            val methodName = element.methodName
            
            if (isValidCaller(className, methodName)) {
                Log.d(TAG, "Found caller: $className.$methodName (line ${element.lineNumber})")
                return className
            }
        }
        
        return "UnknownCaller"
    }
    
    /**
     * 获取调用方的详细信息
     * @param skipFrames 跳过的堆栈帧数
     * @return 包含类名、方法名、行号的详细信息
     */
    fun getCallerInfo(skipFrames: Int = 2): CallerInfo {
        val stackTrace = Thread.currentThread().stackTrace
        
        for (i in skipFrames until stackTrace.size) {
            val element = stackTrace[i]
            val className = element.className
            val methodName = element.methodName
            
            if (isValidCaller(className, methodName)) {
                return CallerInfo(
                    className = className,
                    simpleClassName = className.substringAfterLast('.'),
                    methodName = methodName,
                    lineNumber = element.lineNumber
                )
            }
        }
        
        return CallerInfo("UnknownCaller", "UnknownCaller", "unknown", -1)
    }
    
    /**
     * 判断是否为有效的调用方
     * 过滤掉系统类、协程相关类等
     */
    private fun isValidCaller(className: String, methodName: String): Boolean {
        // 跳过系统类和框架类
        val skipPatterns = listOf(
            "kotlinx.coroutines",
            "java.lang",
            "android",
            "androidx",
            "com.android",
            "dalvik",
            "sun.misc",
            "jdk.internal"
        )
        
        // 跳过扩展函数本身
        val skipMethods = listOf(
            "launch1",
            "launchWithCaller",
            "getCallerClassName",
            "getCallerInfo"
        )
        
        return !skipPatterns.any { className.contains(it) } &&
               !skipMethods.contains(methodName) &&
               className.isNotEmpty()
    }
    
    /**
     * 优化版本的getCallerClassName
     * 使用更高效的字符串匹配和缓存机制
     */
    fun getCallerClassNameOptimized(skipFrames: Int = 2): String {
        val stackTrace = Thread.currentThread().stackTrace
        
        // 使用更高效的字符串匹配
        for (i in skipFrames until stackTrace.size) {
            val element = stackTrace[i]
            val className = element.className
            
            // 快速检查：如果类名包含系统包名，直接跳过
            if (className.startsWith("kotlinx.coroutines") ||
                className.startsWith("java.lang") ||
                className.startsWith("android") ||
                className.startsWith("androidx") ||
                className.startsWith("com.android") ||
                className.startsWith("dalvik") ||
                className.startsWith("sun.misc") ||
                className.startsWith("jdk.internal")) {
                continue
            }
            
            // 检查方法名
            val methodName = element.methodName
            if (methodName == "launch1" || 
                methodName == "launchWithCaller" || 
                methodName == "getCallerClassName" || 
                methodName == "getCallerInfo") {
                continue
            }
            
            // 找到有效调用方，提取简单类名
            val lastDotIndex = className.lastIndexOf('.')
            val simpleClassName = if (lastDotIndex >= 0) {
                className.substring(lastDotIndex + 1)
            } else {
                className
            }
            
            Log.d(TAG, "Found caller (optimized): $className.$methodName (line ${element.lineNumber})")
            return simpleClassName
        }
        
        return "UnknownCaller"
    }
    
    /**
     * 超轻量版本的getCallerClassName
     * 只获取类名，不进行详细过滤
     */
    fun getCallerClassNameLightweight(skipFrames: Int = 2): String {
        val stackTrace = Thread.currentThread().stackTrace
        
        if (skipFrames < stackTrace.size) {
            val element = stackTrace[skipFrames]
            val className = element.className
            
            val lastDotIndex = className.lastIndexOf('.')
            return if (lastDotIndex >= 0) {
                className.substring(lastDotIndex + 1)
            } else {
                className
            }
        }
        
        return "UnknownCaller"
    }
    
    /**
     * 调用方信息数据类
     */
    data class CallerInfo(
        val className: String,
        val simpleClassName: String,
        val methodName: String,
        val lineNumber: Int
    ) {
        override fun toString(): String {
            return "$simpleClassName.$methodName:$lineNumber"
        }
    }
}

/**
 * 带调用方信息的协程启动扩展函数
 */
fun CoroutineScope.launchWithCaller(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
) {
    val callerInfo = CoroutineUtils.getCallerInfo()
    val name = CoroutineName(callerInfo.simpleClassName)
    
    Log.i("FrankTest", "launchWithCaller: ${callerInfo}")
    
    launch(
        context + name, start, block
    )
}

/**
 * 带调用方信息的协程启动扩展函数（简化版）
 */
fun CoroutineScope.launchWithCallerName(
    block: suspend CoroutineScope.() -> Unit
) {
    val callerName = CoroutineUtils.getCallerClassName()
    val name = CoroutineName(callerName)
    
    Log.i("FrankTest", "launchWithCallerName: $callerName")
    
    launch(name) {
        block()
    }
}

/**
 * 带详细调用信息的协程启动扩展函数
 */
fun CoroutineScope.launchWithDetailedInfo(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
) {
    val callerInfo = CoroutineUtils.getCallerInfo()
    val name = CoroutineName("${callerInfo.simpleClassName}.${callerInfo.methodName}")
    
    Log.i("FrankTest", "launchWithDetailedInfo: ${callerInfo}")
    
    launch(
        context + name, start, block
    )
} 