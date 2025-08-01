# 简化的Kotlin协程异常保护机制

一个简单易用的全局Kotlin协程异常保护系统，确保协程中的异常不会导致应用崩溃。

## 特性

- **简单易用**：一个全局异常处理器处理所有异常
- **便捷扩展函数**：提供丰富的协程启动扩展函数
- **类型化处理**：针对不同异常类型提供专门的处理逻辑
- **重试机制**：内置重试和超时控制功能
- **结果回调**：支持成功和失败的回调处理

## 核心组件

1. **SimpleCoroutineExceptionHandler**：简单的全局异常处理器
2. **CoroutineExtensions**：协程扩展函数

## 使用方法

### 1. 基础使用

```kotlin
// 使用全局异常处理器启动协程
lifecycleScope.launchWithExceptionHandler {
    // 你的协程代码
    throw RuntimeException("测试异常")
}

// 使用异步协程
val deferred = lifecycleScope.asyncWithExceptionHandler {
    "异步结果"
}
```

### 2. 结果处理

```kotlin
lifecycleScope.launchWithResult(
    onSuccess = { result ->
        // 处理成功结果
    },
    onError = { exception ->
        // 处理异常
    }
) {
    // 协程代码
    "成功结果"
}
```

### 3. 重试机制

```kotlin
lifecycleScope.launchWithRetry(
    maxRetries = 3,
    retryDelay = 1000,
    onRetry = { attempt, exception ->
        // 重试回调
    }
) {
    // 可能失败的操作
    "最终结果"
}
```

### 4. 超时控制

```kotlin
lifecycleScope.launchWithTimeout(
    timeoutMillis = 5000,
    onTimeout = {
        // 超时处理
    }
) {
    // 长时间操作
    "操作结果"
}
```

## 异常处理逻辑

全局异常处理器会根据异常类型进行不同的处理：

- **OutOfMemoryError**：记录日志并触发垃圾回收
- **SecurityException**：记录权限相关异常
- **IllegalArgumentException**：记录参数异常
- **NullPointerException**：记录空指针异常
- **InterruptedException**：恢复中断状态
- **其他异常**：记录异常信息

## 扩展函数列表

| 函数名 | 说明 |
|--------|------|
| `launchWithExceptionHandler` | 使用全局异常处理器启动协程 |
| `asyncWithExceptionHandler` | 使用全局异常处理器启动异步协程 |
| `withExceptionHandler` | 使用全局异常处理器执行协程代码 |
| `launchWithResult` | 启动协程并处理结果 |
| `launchWithRetry` | 启动协程并支持重试 |
| `launchWithTimeout` | 启动协程并支持超时控制 |

## 最佳实践

### 1. 在Application中初始化

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // 预加载全局异常处理器
        SimpleCoroutineExceptionHandler.getInstance()
    }
}
```

### 2. 合理使用扩展函数

```kotlin
// 简单协程使用
lifecycleScope.launchWithExceptionHandler {
    // 简单操作
}

// 需要结果处理的使用
lifecycleScope.launchWithResult(
    onSuccess = { result -> /* 处理成功 */ },
    onError = { exception -> /* 处理异常 */ }
) {
    // 可能失败的操作
}

// 需要重试的使用
lifecycleScope.launchWithRetry(maxRetries = 3) {
    // 网络请求等可能失败的操作
}
```

### 3. 避免在异常处理器中执行耗时操作

```kotlin
// 好的做法：只记录日志
override fun handleException(context: CoroutineContext, exception: Throwable) {
    Log.e(TAG, "异常信息", exception)
}

// 避免：在异常处理器中执行耗时操作
override fun handleException(context: CoroutineContext, exception: Throwable) {
    // 避免在这里执行网络请求、文件操作等耗时操作
}
```

## 测试

系统提供了简化的单元测试：

```kotlin
@Test
fun `测试全局异常处理器`() {
    val handler1 = SimpleCoroutineExceptionHandler.getInstance()
    val handler2 = SimpleCoroutineExceptionHandler.getInstance()
    assertSame(handler1, handler2) // 验证单例模式
}

@Test
fun `测试协程扩展函数`() = runTest {
    val job = testScope.launchWithExceptionHandler {
        throw RuntimeException("测试异常")
    }
    job.join()
    assertTrue(job.isCompleted) // 验证异常被正确处理
}
```

## 注意事项

1. **简单性**：这个实现去掉了复杂的SPI机制和配置管理，专注于核心功能
2. **性能**：异常处理逻辑轻量级，不会影响应用性能
3. **线程安全**：全局异常处理器是线程安全的
4. **内存安全**：使用单例模式，避免内存泄漏

## 总结

这个简化的协程异常保护机制提供了：

- **简单易用**：一个文件搞定所有异常处理
- **功能完整**：支持基础异常处理、重试、超时等功能
- **性能优秀**：轻量级实现，不影响应用性能
- **易于维护**：代码结构清晰，易于理解和修改

通过使用这套机制，可以有效防止协程异常导致的应用崩溃，提升应用的稳定性。 