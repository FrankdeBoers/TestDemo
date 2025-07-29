# 协程性能监控使用指南

## 概述

本项目提供了三种不同级别的协程性能监控方案，帮助开发者监控和分析协程的执行性能。

## 监控方案

### 1. 基础ContinuationInterceptor (`CoroutinePerformanceInterceptor`)

**特点：**
- 简单易用，性能开销小
- 提供基本的协程执行时间监控
- 记录协程挂起次数和异常信息

**使用方式：**
```kotlin
// 创建带监控的协程作用域
val performanceScope = CoroutineScope(
    SupervisorJob() + 
    CoroutinePerformanceInterceptor() + 
    Dispatchers.Default
)

// 启动协程
performanceScope.launch {
    // 你的协程代码
    delay(1000L)
}
```

**输出示例：**
```
✅ 协程执行完成: Coroutine-main-1234567890
📊 执行统计: 总耗时=1005ms, 挂起次数=1
```

### 2. 性能监控辅助类 (`CoroutinePerformanceHelper`)

**特点：**
- 提供更详细的性能统计
- 支持自定义协程名称
- 包含性能报告生成功能
- 单例模式，全局统计

**使用方式：**
```kotlin
// 获取单例实例
val helper = CoroutinePerformanceHelper.getInstance()

// 启动带监控的协程
helper.launchWithPerformance("网络请求协程") {
    // 模拟网络请求
    delay(800L)
}

// 获取性能报告
val report = helper.getPerformanceReport()
Log.i("TAG", report)
```

**输出示例：**
```
📈 协程性能统计报告
==================================================
总协程数: 5
已完成协程: 4
运行中协程: 1
总执行时间: 2500ms
平均执行时间: 625.00ms
总挂起次数: 8
平均挂起次数: 2.00
==================================================
```

### 3. 高级协程拦截器 (`AdvancedCoroutineInterceptor`)

**特点：**
- 最全面的监控功能
- 自动检测慢协程（>1秒）
- 记录协程启动位置（堆栈跟踪）
- 提供全局统计和详细分析
- 支持慢协程警告

**使用方式：**
```kotlin
// 创建高级监控作用域
val advancedScope = CoroutineScope(
    SupervisorJob() + 
    AdvancedCoroutineInterceptor() + 
    Dispatchers.Default
)

// 启动协程
advancedScope.launch {
    delay(1500L) // 会被标记为慢协程
}

// 获取全局统计
val globalStats = AdvancedCoroutineInterceptor.getGlobalStats()
Log.i("TAG", globalStats)

// 获取协程执行统计
val stats = advancedInterceptor.getCoroutineStats()
Log.i("TAG", stats)
```

**输出示例：**
```
🚀 高级协程启动: AdvancedCoroutine-main
📍 启动位置: CoroutineActivity.demonstrateAdvancedInterceptor:123 -> ...
⚠️ 检测到慢协程: AdvancedCoroutine-main, 耗时=1505ms
📍 慢协程位置: CoroutineActivity.demonstrateAdvancedInterceptor:123 -> ...

📈 协程执行统计
============================================================
已完成协程: 3
活跃协程: 1
慢协程: 1
平均执行时间: 567.33ms
平均挂起次数: 1.67
============================================================
🐌 慢协程列表:
  - AdvancedCoroutine-main: 1505ms
```

## 实际应用场景

### 1. 开发调试阶段
```kotlin
// 在开发环境中启用详细监控
if (BuildConfig.DEBUG) {
    val scope = CoroutineScope(AdvancedCoroutineInterceptor())
    // 使用scope启动协程
}
```

### 2. 性能优化分析
```kotlin
// 分析特定操作的性能
helper.launchWithPerformance("数据库批量操作") {
    database.batchInsert(largeDataList)
}

// 查看性能报告
val report = helper.getPerformanceReport()
```

### 3. 生产环境监控
```kotlin
// 只监控关键操作
val criticalScope = CoroutineScope(
    CoroutinePerformanceInterceptor() + 
    Dispatchers.IO
)

criticalScope.launch {
    // 关键业务逻辑
    processPayment()
}
```

## 性能考虑

1. **基础拦截器**：性能开销最小，适合生产环境
2. **辅助类**：中等开销，适合开发调试
3. **高级拦截器**：开销较大，建议仅在开发环境使用

## 最佳实践

1. **选择合适的监控级别**：根据使用场景选择合适的监控方案
2. **定期清理统计数据**：避免内存泄漏
3. **合理设置慢协程阈值**：根据业务需求调整
4. **结合日志级别**：在发布版本中降低日志级别

## 清理数据

```kotlin
// 清理辅助类数据
helper.clearPerformanceStats()

// 清理高级拦截器数据
AdvancedCoroutineInterceptor.clearAllStats()
```

## 注意事项

1. 监控代码本身会影响协程性能，建议在开发环境使用
2. 大量协程时注意内存使用，定期清理统计数据
3. 生产环境建议使用基础拦截器或完全关闭监控
4. 堆栈跟踪功能会增加性能开销，谨慎使用 