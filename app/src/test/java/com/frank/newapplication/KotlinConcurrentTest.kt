package com.frank.newapplication

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Test

class KotlinConcurrentTest {
    var counter = 0

    @Test
    fun testKotlin() {
        singleCoroutine()

        Thread.sleep(100_000)
    }

    private suspend fun add() = withContext(Dispatchers.Default) {
        delay(1)
        counter += 1
        println("Frank## add counter:$counter threadName:${Thread.currentThread().name}}")
    }

    // 单协程
    private fun singleCoroutine() {
        runBlocking {
            GlobalScope.launch(Dispatchers.IO) {
                repeat(10) {
                    add()
                }

                println("Frank## singleCoroutine counter:$counter threadName:${Thread.currentThread().name}}")
            }
        }
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

}
