package com.frank.newapplication.threadpool

import org.junit.Test
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ExecutorService
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * 测试串行线程是否保序
 *
 * */
class ThreadPoolBlock {
    private val threadPoolA = ThreadPoolExecutor(
        10,
        20,
        100,
        TimeUnit.SECONDS,
        ArrayBlockingQueue(100),
        object : ThreadFactory {
            val num = AtomicInteger(1)
            override fun newThread(r: Runnable?): Thread {
                return Thread(r, "AAA#${num.getAndIncrement()}")
            }
        }
    )

    private val threadPoolB = ThreadPoolExecutor(
        10,
        20,
        100,
        TimeUnit.SECONDS,
        ArrayBlockingQueue(100),
        object : ThreadFactory {
            val num = AtomicInteger(1)
            override fun newThread(r: Runnable?): Thread {
                return Thread(r, "BBB#${num.getAndIncrement()}")
            }
        }
    )

    private var count = 0
    private var atomicCount = AtomicInteger(0)

    private lateinit var runnableA: Runnable
    private lateinit var runnableB: Runnable

    @Test
    fun testBlock() {
        runnableA = Runnable {
            count++
            atomicCount.getAndIncrement()
//            println("Frank## threadPool# runnableA# count:$count, thread:${Thread.currentThread().name}")
            threadPoolA.execute(runnableB)
        }

        runnableB = Runnable {
            count++
//            atomicCount.getAndIncrement()
//            println("Frank## threadPool#runnableB# count:$count, thread:${Thread.currentThread().name}")
            threadPoolB.execute(runnableA)
        }

        threadPoolA.execute(runnableB)

        Thread.sleep(100)
        println("Frank## threadPool# count:$count, atomicCount:${atomicCount.get()}")
    }
}