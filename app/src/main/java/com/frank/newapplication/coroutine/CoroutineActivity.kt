package com.frank.newapplication.coroutine

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.frank.newapplication.BaseActivity
import com.frank.newapplication.databinding.ActivityCoroutineBinding
import com.frank.newapplication.utils.CommonUtils.getFileSHA256
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class CoroutineActivity : BaseActivity() {

    private lateinit var binding: ActivityCoroutineBinding
    @Volatile
    var a = true
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext: CoroutineContext, throwable: Throwable ->
        Log.e("FrankTest", "$logTag coroutine# exceptionHandler:${throwable}")
    }

    private val jobScope = CoroutineScope(Job() + exceptionHandler)
    private val supervisorScope = CoroutineScope(SupervisorJob() + exceptionHandler)

    override fun onCreate(savedInstanceState: Bundle?) {
        Int.MAX_VALUE
        super.onCreate(savedInstanceState)
        binding = ActivityCoroutineBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initClick()
    }

    private fun initClick() {
        Log.i("FrankTest", "$logTag coroutine# name0:${Thread.currentThread().name}")
        binding.startCoroutine.setOnClickListener {
            lifecycleScope.launch {
                Log.i("FrankTest", "$logTag coroutine# name1:${Thread.currentThread().name}")
                val startTime = System.currentTimeMillis()
                val file1 = "/data/data/com.frank.newapplication/files/HoYowave_mac_x64_1.13.0-alpha(0117.094509.dev.1a1ee434ac).dmg"
                val file2 = "/data/data/com.frank.newapplication/files/HoYowave_mac_x64_1.13.0-alpha(0117.094509.dev.1a1ee434ac)_副本2.dmg"
                val hash1 = async {
                    delay(300L)
                    Log.i("FrankTest", "$logTag coroutine# hash1 start name:${Thread.currentThread().name}")
                    getHash(file1)
                }
                val hash2 = async {
                    delay(100L)
                    getHash(file2)
                    Log.i("FrankTest", "$logTag coroutine# hash2 start name:${Thread.currentThread().name}")
                }

                coroutineScope {

                }

                delay(2000L)
                Log.i("FrankTest", "$logTag coroutine# delay 2000ms")
                delay(100L)
                val result = "hash1:${hash1.await()}, hash2:${hash2.await()}"
                Log.i("FrankTest", "$logTag coroutine# cost:${System.currentTimeMillis() - startTime},result:$result")
            }

            val scope = CoroutineScope(EmptyCoroutineContext)
            scope.launch {
                Log.i("FrankTest", "$logTag coroutine# name3:${Thread.currentThread().name}")
            }
        }

        // 普通Job
        binding.jobCoroutine.setOnClickListener {
            jobScope.launch {
                delay(100L)
                Log.i("FrankTest", "$logTag jobCoroutine1")
                throw Exception("jobCoroutine1 failed!!")
            }

            jobScope.launch {
                delay(200L)
                Log.i("FrankTest", "$logTag jobCoroutine2")
            }

            jobScope.launch {
                delay(300L)
                Log.i("FrankTest", "$logTag jobCoroutine3")
            }
        }

        // SupervisorJob
        binding.supervisorJobCoroutine.setOnClickListener {
            supervisorScope.launch {
                delay(100L)
                Log.i("FrankTest", "$logTag supervisorJobCoroutine1")
                throw Exception("supervisorJobCoroutine1 failed!!")
            }

            supervisorScope.launch {
                delay(200L)
                Log.i("FrankTest", "$logTag supervisorJobCoroutine2")
            }

            supervisorScope.launch {
                delay(300L)
                Log.i("FrankTest", "$logTag supervisorJobCoroutine3")
                throw Exception("supervisorJobCoroutine3 failed!!")
            }
        }

        binding.multiThreadVisible.setOnClickListener {
            var i = 0

            for (j in 0 until 1000) {
                Thread({
                    i++
                }).start()
                Thread({
                    i++
                }).start()
            }


            Thread.sleep(200)
            Log.i("FrankTest", "$logTag multiThreadVisible i:$i")
        }
    }

    private suspend fun getHash(path: String) = withContext(Dispatchers.IO) {
        Log.i("FrankTest", "$logTag coroutine# name2:${Thread.currentThread().name}")
        getFileSHA256(this@CoroutineActivity, path)
    }
}