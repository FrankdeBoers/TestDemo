package com.frank.newapplication.threadpool

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.frank.newapplication.BaseActivity
import com.frank.newapplication.databinding.ActivityThreadBinding
import com.frank.newapplication.utils.CommonUtils.getFileSHA256
import com.frank.newapplication.utils.FileLister
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.RejectedExecutionHandler
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class ThreadPoolActivity : BaseActivity() {

    private lateinit var binding: ActivityThreadBinding

    private val threadPool = ThreadPoolExecutor(
        2,
        4,
        200,
        TimeUnit.MILLISECONDS,
        ArrayBlockingQueue(50),
        object : ThreadFactory {
            private val mCount = AtomicInteger(1)
            override fun newThread(r: Runnable): Thread {
                return Thread(r, "testPool" + mCount.getAndIncrement().toString())
            }
        }, object : RejectedExecutionHandler {
            override fun rejectedExecution(r: Runnable?, executor: ThreadPoolExecutor?) {
                Log.i("FrankTest", "$logTag rejectedExecution:${r.toString()}, executor:${executor?.corePoolSize}")
            }
        }
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThreadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initClick()
    }

    private fun initClick() {
        binding.startThread.setOnClickListener {
            Log.i("FrankTest", "$logTag startThread")
            threadPool.asCoroutineDispatcher()

            // 获取内部存储目录
            val files: List<File> = FileLister(this).listFiles()
            for (file in files) {
                val runnable = Runnable {
                    val sha256 = getFileSHA256(this, file.absolutePath)
                    Log.i("FrankTest", "$logTag file:${file.absolutePath}, sha256:$sha256")
                }
//                threadPool.execute(runnable)
                lifecycleScope.launch(threadPool.asCoroutineDispatcher()) {
                    val sha256 = getFileSHA256(this@ThreadPoolActivity, file.absolutePath)
                    Log.i("FrankTest", "$logTag file:${file.absolutePath}, sha256:$sha256")
                }
            }
        }
    }
}
