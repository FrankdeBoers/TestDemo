package com.frank.newapplication.coroutine.exception

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 简化的协程扩展函数
 * 提供便捷的异常保护启动方式
 */

/**
 * 使用全局异常处理器启动协程
 */
fun CoroutineScope.launchWithExceptionHandler(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return launch(context + SimpleCoroutineExceptionHandler.getInstance(), start, block)
}

/**
 * 使用全局异常处理器启动异步协程
 */
fun <T> CoroutineScope.asyncWithExceptionHandler(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T
): Deferred<T> {
    return async(context + SimpleCoroutineExceptionHandler.getInstance(), start, block)
}

/**
 * 使用全局异常处理器启动协程并等待完成
 */
suspend fun <T> withExceptionHandler(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> T
): T {
    return withContext(context + SimpleCoroutineExceptionHandler.getInstance(), block)
}

/**
 * 使用全局异常处理器启动协程并处理结果
 */
fun <T> CoroutineScope.launchWithResult(
    context: CoroutineContext = EmptyCoroutineContext,
    onSuccess: (T) -> Unit = {},
    onError: (Throwable) -> Unit = {},
    block: suspend CoroutineScope.() -> T
): Job {
    return launchWithExceptionHandler(context) {
        try {
            val result = block()
            onSuccess(result)
        } catch (e: Throwable) {
            onError(e)
            throw e // 重新抛出异常，让全局异常处理器处理
        }
    }
}

/**
 * 使用全局异常处理器启动协程并处理结果（无返回值版本）
 */
fun CoroutineScope.launchWithResult(
    context: CoroutineContext = EmptyCoroutineContext,
    onSuccess: () -> Unit = {},
    onError: (Throwable) -> Unit = {},
    block: suspend CoroutineScope.() -> Unit
): Job {
    return launchWithExceptionHandler(context) {
        try {
            block()
            onSuccess()
        } catch (e: Throwable) {
            onError(e)
            throw e // 重新抛出异常，让全局异常处理器处理
        }
    }
}

/**
 * 使用全局异常处理器启动协程并重试
 */
fun <T> CoroutineScope.launchWithRetry(
    context: CoroutineContext = EmptyCoroutineContext,
    maxRetries: Int = 3,
    retryDelay: Long = 1000,
    onRetry: (Int, Throwable) -> Unit = { _, _ -> },
    block: suspend CoroutineScope.() -> T
): Job {
    return launchWithExceptionHandler(context) {
        var lastException: Throwable? = null
        repeat(maxRetries) { attempt ->
            try {
                val result = block()
                return@launchWithExceptionHandler
            } catch (e: Throwable) {
                lastException = e
                if (attempt < maxRetries - 1) {
                    onRetry(attempt + 1, e)
                    delay(retryDelay * (attempt + 1)) // 指数退避
                }
            }
        }
        // 所有重试都失败了，抛出最后一个异常
        throw lastException ?: RuntimeException("未知错误")
    }
}

/**
 * 使用全局异常处理器启动协程并超时控制
 */
fun <T> CoroutineScope.launchWithTimeout(
    context: CoroutineContext = EmptyCoroutineContext,
    timeoutMillis: Long,
    onTimeout: () -> Unit = {},
    block: suspend CoroutineScope.() -> T
): Job {
    return launchWithExceptionHandler(context) {
        try {
            withTimeout(timeoutMillis) {
                block()
            }
        } catch (e: TimeoutCancellationException) {
            onTimeout()
            throw e
        }
    }
} 