package com.frank.newapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.frank.newapplication.BitmapActivity.BitmapActivity
import com.frank.newapplication.coroutine.CoroutineActivity
import com.frank.newapplication.databinding.ActivityMainBinding
import com.frank.newapplication.glide.GlideHelper
import com.frank.newapplication.handler.BarrierActivity2
import com.frank.newapplication.http.OkhttpActivity
import com.frank.newapplication.rv.RecyclerViewActivity
import com.frank.newapplication.threadpool.ThreadPoolActivity
import com.frank.newapplication.weakreference.WeakReferenceActivity
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.flexboxcustom.FlexboxLayout
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

class MainActivity : BaseActivity() {

    private val webSocket = SocketManager()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 动态创建FlexboxLayout作为主布局
        val flexboxLayout = binding.flexboxLayout

        // 1. 图片展示控件 PhotoView
        val photoView = PhotoView(this).apply {
            layoutParams = FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                400
            )
            id = View.generateViewId()
        }
        flexboxLayout.addView(photoView)
        GlideHelper().loadGif(this, "/data/data/com.frank.newapplication/files/1720081461864.gif", photoView)

        // 2. 底部回复区
        val bottomReplyLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                60
            )
            gravity = Gravity.CENTER_VERTICAL
        }
        val layoutHeadView = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(60, 60)
        }
        val tvReplyCount = TextView(this).apply {
            text = "999条回复"
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        val imgDot = ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(8, 8)
            setImageResource(R.drawable.ic_launcher_background)
        }
        val tvReplyTime = TextView(this).apply {
            text = "最后更新于 3分钟前"
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        val imgReplyRightArrow = ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(32, 32)
            setImageResource(R.drawable.ic_launcher_background)
        }
        bottomReplyLayout.addView(layoutHeadView)
        bottomReplyLayout.addView(tvReplyCount)
        bottomReplyLayout.addView(imgDot)
        bottomReplyLayout.addView(tvReplyTime)
        bottomReplyLayout.addView(imgReplyRightArrow)
        flexboxLayout.addView(bottomReplyLayout)

        // 3. 所有功能按钮
        val btnList = listOf(
            Pair("glide加载Gif") {
                GlideHelper().loadGif(this, "/data/data/com.frank.newapplication/files/1720081461864.gif", photoView)
            },
            Pair("长链接") {
                webSocket.connect()
            },
            Pair("发消息") {
                webSocket.sendMessage("心跳包测试消息")
            },
            Pair("启动第二Activity") {
                startActivity(Intent(this, SecondActivity::class.java))
            },
            Pair("启动Weak") {
                startActivity(Intent(this, WeakReferenceActivity::class.java))
            },
            Pair("线程池") {
                startActivity(Intent(this, ThreadPoolActivity::class.java))
            },
            Pair("协程性能监控") {
                // 启动协程性能监控演示页面
                // 包含三种不同级别的协程监控方案：
                // 1. 基础ContinuationInterceptor监控
                // 2. 性能监控辅助类
                // 3. 高级协程拦截器（包含慢协程检测）
                startActivity(Intent(this, CoroutineActivity::class.java))
            },
            Pair("图片加载") {
                startActivity(Intent(this, BitmapActivity::class.java))
            },
            Pair("OkHttp") {
                startActivity(Intent(this, OkhttpActivity::class.java))
            },
            Pair("Barrier") {
                startActivity(Intent(this, BarrierActivity2::class.java))
            },
            Pair("RecyclerView测试") {
                startActivity(Intent(this, RecyclerViewActivity::class.java))
            },
            Pair("安全页面（禁止截屏）") {
                startActivity(Intent(this, Class.forName("com.example.syncbarrierdemo.SecureActivity")))
            },
            Pair("KeyStore用法演示") {
                startActivity(Intent(this, KeyStoreDemoActivity::class.java))
            },
            Pair("Fragment版生物识别演示") {
                startActivity(Intent(this, BiometricFragmentHostActivity::class.java))
            },
            Pair("Thread状态演示") {
                startActivity(Intent(this, ThreadStateActivity::class.java))
            },
            Pair("ServiceManager sCache查看") {
                startActivity(Intent(this, ServiceManagerCacheActivity::class.java))
            },
            Pair("Canvas截图演示") {
                startActivity(Intent(this, CanvasScreenshotActivity::class.java))
            }
        )
        btnList.forEach { (text, action) ->
            val btn = Button(this).apply {
                this.text = text
                layoutParams = FlexboxLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(16, 16, 16, 16)
                }
                setOnClickListener { action.invoke() }
            }
            flexboxLayout.addView(btn)
        }

        Log.i("Main##", "model:${Build.MODEL}")
        isGifFile("/data/data/com.frank.newapplication/files/happy-cat.gif")
        isGifFile(this, "/data/data/com.frank.newapplication/files/happy-cat_副本.jpg") {
            Log.i("Main##", "getMimeType# isGifFile#:$it Callback")
        }
        val startTime = System.currentTimeMillis()
        val mime = getMimeType(File("/data/data/com.frank.newapplication/files/happy-cat_副本.jpg"))
        Log.i(
            "Main##", "getMimeType# MimeType:$mime, cost:" +
                    (System.currentTimeMillis() - startTime).toString() + "ms"
        )
        Log.i("FrankTest", "$logTag classLoader## String: " + String.Companion::class.java.classLoader)
        Log.i("FrankTest", "$logTag classLoader## Activity: " + Activity::class.java.classLoader)
        Log.i("FrankTest", "$logTag classLoader## BarrierActivity2: " + MainActivity::class.java.classLoader)
    }

    fun isGif(inputStream: InputStream): Boolean {
        val signature = ByteArray(6)
        inputStream.mark(6)  // 标记当前位置，以便在读取后重置流
        val bytesRead = inputStream.read(signature, 0, 6)
        inputStream.reset()  // 重置流到标记的位置
        Log.i("Main##", "isGif# bytesRead:$bytesRead, signature:$signature")
        if (bytesRead < 6) {
            return false
        }

        val gif87a = byteArrayOf('G'.toByte(), 'I'.toByte(), 'F'.toByte(), '8'.toByte(), '7'.toByte(), 'a'.toByte())
        val gif89a = byteArrayOf('G'.toByte(), 'I'.toByte(), 'F'.toByte(), '8'.toByte(), '9'.toByte(), 'a'.toByte())

        return signature.contentEquals(gif87a) || signature.contentEquals(gif89a)
    }

    fun isGifFile(filePath: String): Boolean {
        val signature = ByteArray(6)
        var fileInputStream: FileInputStream? = null
        return try {
            fileInputStream = FileInputStream(filePath)
            val bytesRead = fileInputStream.read(signature, 0, 6)
            Log.i("Main##", "isGif# bytesRead:$bytesRead, signature:$signature")
            if (bytesRead < 6) {
                return false
            }

            val gif87a = byteArrayOf('G'.toByte(), 'I'.toByte(), 'F'.toByte(), '8'.toByte(), '7'.toByte(), 'a'.toByte())
            val gif89a = byteArrayOf('G'.toByte(), 'I'.toByte(), 'F'.toByte(), '8'.toByte(), '9'.toByte(), 'a'.toByte())

            signature.contentEquals(gif87a) || signature.contentEquals(gif89a)
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } finally {
            fileInputStream?.close()
        }
    }

    fun isGifFile(context: Context, filePath: String, callback: (Boolean) -> Unit) {
        Glide.with(context)
            .load(filePath)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean,
                ): Boolean {
                    callback(false)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean,
                ): Boolean {
                    val isGif = resource is GifDrawable
                    callback(isGif)
                    Log.i("Main##", "isGifFile# onResourceReady isGif:$isGif, resource:${resource}")
                    return false
                }
            })
            .submit()
    }


    fun getMimeType(file: File): String? {
        val buffer = ByteArray(8)
        try {
            FileInputStream(file).use { inputStream ->
                inputStream.read(buffer, 0, buffer.size)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        for (i in 0..7) {
            Log.i("Main##", "getMimeType# onResourceReady buffer:${buffer[i]}")
        }

        return when {
            isGif(buffer) -> "image/gif"
            isJpeg(buffer) -> "image/jpeg"
            isPng(buffer) -> "image/png"
            else -> null
        }
    }

    fun isGif(buffer: ByteArray): Boolean {
        val gifHeader87a = byteArrayOf('G'.toByte(), 'I'.toByte(), 'F'.toByte(), '8'.toByte(), '7'.toByte(), 'a'.toByte())
        val gifHeader89a = byteArrayOf('G'.toByte(), 'I'.toByte(), 'F'.toByte(), '8'.toByte(), '9'.toByte(), 'a'.toByte())
        return buffer.copyOfRange(0, 6).contentEquals(gifHeader87a) || buffer.copyOfRange(0, 6).contentEquals(gifHeader89a)
    }

    fun isJpeg(buffer: ByteArray): Boolean {
        val jpegHeader = byteArrayOf(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte())
        return buffer.copyOfRange(0, 3).contentEquals(jpegHeader)
    }

    fun isPng(buffer: ByteArray): Boolean {
        val pngHeader = byteArrayOf(0x89.toByte(), 0x50.toByte(), 0x4E.toByte(), 0x47.toByte(), 0x0D.toByte(), 0x0A.toByte(), 0x1A.toByte(), 0x0A.toByte())
        return buffer.contentEquals(pngHeader)
    }

}

