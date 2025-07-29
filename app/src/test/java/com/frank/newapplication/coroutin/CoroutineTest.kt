package com.frank.newapplication.coroutin

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.junit.Test

class CoroutineTest {

    @Test
    fun testDelay0() {
        val job = GlobalScope.launch {
            val startTime = System.currentTimeMillis()
            // 耗时100ms
            heavyCPU(Int.MAX_VALUE * 2L)
//            println("testDelay0 cost ${System.currentTimeMillis() - startTime}ms")
            // 测试delay(0)是否能插入挂起点
            delay(1L)

            heavyCPU(Int.MAX_VALUE * 1L)
            println("testDelay0 finish")
        }


        Thread.sleep(50L)
        job.cancel()
        println("testDelay0 job.cancel")

        Thread.sleep(10000000000L)
    }


    private fun heavyCPU(times: Long) {
        var result = 0L
        for (i in 0 until  times) {
            result += 1L
        }
    }
}