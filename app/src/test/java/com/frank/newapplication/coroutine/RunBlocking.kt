package com.frank.newapplication.coroutine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.concurrent.Executors

class RunBlocking {
    val threadPool = Executors.newFixedThreadPool(64)

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
}