package com.frank.newapplication.coroutine

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Test
import java.util.concurrent.Executors

class RunBlocking {
    val threadPool = Executors.newFixedThreadPool(64)

    // 测试死锁
    @Test
    fun runblocking() {
        repeat(64) {
            threadPool.submit {
                runBlocking(Dispatchers.IO) {
                    println("Frank## runBlocking $this")
                    // 在协程环境中不应该调用sleep，这里是为了模拟耗时计算和调用
                    Thread.sleep(5000L)
                    runBlocking {
                        // 因为死锁，这里无法打印
                        launch(Dispatchers.IO) {
                            println("Frank## launch $this")
                        }
                    }
                }
            }
        }


        Thread.sleep(5000)

        // 别的协程执行不了，这里也无法打印
        runBlocking(Dispatchers.IO) {
            println("Frank## runBlocking2")
        }

        Thread.sleep(Long.MAX_VALUE)
    }


    // 测试底层线程池
    @Test
    fun testScheduler() {
        repeat(1) { index ->
            runBlocking(Dispatchers.Default + CoroutineName("第${index + 1}个协程")) {
                println("Frank# testScheduler Default thread:${Thread.currentThread().name}")
                withContext(Dispatchers.IO) {
                    println("Frank# testScheduler IO thread:${Thread.currentThread().name}")
                }
                println("第${index + 1}个协程 结束\n")
                Thread.sleep(10)
            }
        }

        Thread.sleep(5000000)
    }
}