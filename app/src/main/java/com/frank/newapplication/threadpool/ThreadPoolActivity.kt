package com.frank.newapplication.threadpool

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.frank.newapplication.BaseActivity
import com.frank.newapplication.databinding.ActivityThreadBinding
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
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
        }
        , object : RejectedExecutionHandler {
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
                    val sha256 = getFileSHA256(file.absolutePath)
                    Log.i("FrankTest", "$logTag file:${file.absolutePath}, sha256:$sha256")
                }
//                threadPool.execute(runnable)
                lifecycleScope.launch(threadPool.asCoroutineDispatcher()) {
                    val sha256 = getFileSHA256(file.absolutePath)
                    Log.i("FrankTest", "$logTag file:${file.absolutePath}, sha256:$sha256")
                }
            }
        }
    }

    /**
     * 获取文件的SHA256
     * 支持输入：
     * 1、文件绝对路径，如 /sdcard/DCIM/Camera/IMG_20220802160228119.jpg
     * 2、文件Uri路径，如 content://com.android.providers.media.documents/document/video%3A607
     *
     * */
    fun getFileSHA256(filePath: String): String? {
        val inputStream: InputStream? =
            try {
                if (filePath.startsWith("content")) {
                    this.contentResolver.openInputStream(Uri.parse(filePath))
                } else {
                    FileInputStream(File(filePath))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }

        inputStream ?: return null

        return getStreamSHA256(inputStream)
    }

    /**
     * 获取一段输入流的SHA256
     * @param inputStream 输入流
     * */
    fun getStreamSHA256(inputStream: InputStream): String? {
        val digest: MessageDigest
        val type = "SHA-256"
        digest = try {
            MessageDigest.getInstance(type)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return null
        }

        val buffer = ByteArray(8192)
        var read: Int
        return try {
            while (inputStream.read(buffer).also { read = it } > 0) {
                digest.update(buffer, 0, read)
            }
            val md5sum = digest.digest()
            val bigInt = BigInteger(1, md5sum)
            var output = bigInt.toString(16)
            output = String.format("%32s", output).replace(' ', '0')
            output
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } finally {
            try {
                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}


class FileLister(private val context: Context) {
    fun listFiles(): ArrayList<File> {
        val fileList = ArrayList<File>()
        val privateDir = context.filesDir
        listFilesRecursive(privateDir, fileList)
        return fileList
    }

    private fun listFilesRecursive(directory: File, fileList: ArrayList<File>) {
        val files = directory.listFiles()
        if (files != null) {
            for (file in files) {
                if (file.isDirectory) {
                    // 递归调用，继续查找子目录中的文件
                    listFilesRecursive(file, fileList)
                } else {
                    // 是文件，添加到文件列表中
                    fileList.add(file)
                }
            }
        }
    }
}
