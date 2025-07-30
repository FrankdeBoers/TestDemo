package com.frank.newapplication

import org.junit.Test
import java.util.Date
import kotlin.random.Random

class LogTest {

    @Test
    fun testLog() {
        val time = "2020"
//        // 使用传统方式打印日志
//        log(-1, "testLog# 111 $time")
        // 使用Lambda
        logInline(1) { "testLog# 111 $time" }
    }

    private fun log(level: Int, message: String) {
        if (level < 0) {
            println("log## $message")
        }
    }

    private inline fun logInline(level: Int, noinline message: () -> String) {
        if (level > 0) {
            println("logInline## ${message()}")
            test111()
        }
    }

    private inline fun test111() {
        var i = 0
        if (Random.nextBoolean()) {
            i++
        }
    }
}
