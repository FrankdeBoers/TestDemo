package com.frank.newapplication

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.frank.newapplication.databinding.ActivityMainBinding
import com.frank.newapplication.glide.GlideHelper
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        GlideHelper().loadGif(this, "/data/data/com.frank.newapplication/files/1720081461864.gif", binding.glideImg)
        Log.i("Main##", "model:${Build.MODEL}")
        isGifFile("/data/data/com.frank.newapplication/files/happy-cat.gif")
        isGifFile(this, "/data/data/com.frank.newapplication/files/happy-cat_副本.jpg") {
            Log.i("Main##", "getMimeType# isGifFile#:$it Callback")
        }
        val startTime = System.currentTimeMillis()
        val mime = getMimeType(File("/data/data/com.frank.newapplication/files/happy-cat_副本.jpg"))
        Log.i("Main##", "getMimeType# MimeType:$mime, cost:${System.currentTimeMillis() - startTime}ms")
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
                    isFirstResource: Boolean
                ): Boolean {
                    callback(false)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
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