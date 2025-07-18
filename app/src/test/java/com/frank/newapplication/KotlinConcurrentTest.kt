package com.frank.newapplication

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Test
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class KotlinConcurrentTest {
    val threadPoolExecutor = ThreadPoolExecutor(10,
        Int.MAX_VALUE,
        1_000_000_000_000,
        TimeUnit.SECONDS,
        LinkedBlockingQueue(),
        object : ThreadFactory {
            private val mCount = AtomicInteger(1)
            override fun newThread(r: Runnable): Thread {
                return Thread(r, "CustomThread" + "#" + mCount.getAndIncrement())
            }
        })

    var counter = 0

    @Test
    fun testKotlin() {
        singleCoroutine()
        Thread.sleep(100_000)
    }

    // 单协程
    private fun singleCoroutine() {
        GlobalScope.launch(Dispatchers.IO) {
            repeat(10000) {
                add()
            }
            println("Frank## counter:$counter, name:${Thread.currentThread().name}")
        }
    }

    private suspend fun add() = withContext(threadPoolExecutor.asCoroutineDispatcher()) {
        counter += 1
        println("Frank## add counter:$counter, name:${Thread.currentThread().name}")
        
    }

    // 多协程
    private fun multiCoroutine() {
        runBlocking {
            repeat(10) {
                GlobalScope.launch(Dispatchers.IO) {
                    add()
                }
            }
        }
    }

    @Test
    fun testThread(): Unit {
        var counter = 0

        val thread1 = Thread {
            counter += 1
        }
        thread1.start()
        thread1.join() // 主线程或thread2等待thread1执行完
        println("Frank## thread1 counter:$counter")

        val thread2 = Thread {
            // 这里再去修改C
            counter += 1
        }
        thread2.start()
        thread2.join()
        println("Frank## thread2 counter:$counter")

        val thread3 = Thread {
            // 这里再去修改C
            counter += 1
        }
        thread3.start()
        thread3.join()
        println("Frank## thread3 counter:$counter")

        val thread4 = Thread {
            // 这里再去修改C
            counter += 1
        }
        thread4.start()
        thread4.join()
        println("Frank## thread4 counter:$counter")

        val thread5 = Thread {
            // 这里再去修改C
            counter += 1
        }
        thread5.start()
        thread5.join()
        println("Frank## thread5 counter:$counter")

        for (i in 6..10) {
            val thread = Thread {
                counter += 1 // 每个线程自增
            }
            thread.start()
            thread.join() // 顺序等待每个线程执行完
            println("Frank## thread$i counter:$counter") // 输出当前counter
        }
    }

    @Test
    fun testThreadSleep(): Unit {
        var counter = 1

        for (i in 1 until 1000) {
            val thread = Thread {
                counter += 1 // 每个线程自增
            }
            thread.start()
            Thread.sleep(0)
            println("Frank## thread$i counter:$counter") // 输出当前counter
        }
    }



}
