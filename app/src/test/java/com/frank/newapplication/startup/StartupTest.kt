package com.frank.newapplication.startup

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.frank.newapplication.startup.tasks.*
import org.junit.Test

class StartupTest {

    @Test
    fun testCursor() {
        // 定义所有需要执行的任务
        val allTasks = listOf(
            Task5(),
            Task4(),
            Task3(),
            Task2(),
            Task1()
            // 故意打乱顺序，以验证排序的有效性
        )

        // 执行所有任务
        StartupManager.run(allTasks)

        // 你可以在Logcat中查看 "Startup" 标签的输出来验证执行顺序
        // 预期的顺序是 Task1 -> (Task2, Task3) -> Task4 -> Task5
        // (Task2, Task3) 之间的顺序不固定，因为它们都只依赖Task1
    }

    @Test
    fun testSelf() {
        // 定义所有需要执行的任务
        val allTasks: List<StartUp<*>> = listOf(
            Task5(),
            Task4(),
            Task3(),
            Task2(),
            Task1()
            // 故意打乱顺序，以验证排序的有效性
        )

        // 执行所有任务
        StartupSort.run(allTasks)

        // 你可以在Logcat中查看 "Startup" 标签的输出来验证执行顺序
        // 预期的顺序是 Task1 -> (Task2, Task3) -> Task4 -> Task5
        // (Task2, Task3) 之间的顺序不固定，因为它们都只依赖Task1
    }
}