package com.frank.newapplication.threadpool

import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class ThreadPoolTest {

    @Test
    fun testThreadPool() {
        val threadPool = ThreadPoolExecutor(
            10,
            20,
            30,
            TimeUnit.SECONDS,
            ArrayBlockingQueue(10),
            object : ThreadFactory {
                val count = AtomicInteger(0)
                override fun newThread(r: Runnable?): Thread {
                    return Thread(r, "CustomThreadPool-${count.incrementAndGet()}")
                }

            }
        )

        for (i in 0 until 30) {
            threadPool.execute(MyTask(i))
        }

        // 30秒
        Thread.sleep(30 * 1000)
    }
}

class MyTask(val i: Int) : Runnable {
    override fun run() {
        val instant = java.time.Instant.now()
        val time = instant.toString() // 格式：2024-01-01T12:00:00.123456Z
        val timeOnly = time.substring(11, 23) // 提取 HH:mm:ss.SSS 部分
        val micros = time.substring(24, 27) // 提取微秒部分
        val timeWithMicros = "$timeOnly$micros"
        System.out.println("ThreadPool# threadName:${Thread.currentThread().name}, Task-$i time:$timeWithMicros")
        try {
            Thread.sleep(1000)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}