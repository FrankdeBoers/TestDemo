package com.frank.newapplication

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

/**
 * MainActivity相关的工具类
 * 包含文件类型检测、日志输出等工具方法
 */
object MainUtils {
    
    private const val TAG = "MainUtils"
    
    /**
     * 检测输入流是否为GIF格式
     * @param inputStream 输入流
     * @return 是否为GIF格式
     */
    fun isGif(inputStream: InputStream): Boolean {
        val signature = ByteArray(6)
        inputStream.mark(6)  // 标记当前位置，以便在读取后重置流
        val bytesRead = inputStream.read(signature, 0, 6)
        inputStream.reset()  // 重置流到标记的位置
        Log.i(TAG, "isGif# bytesRead:$bytesRead, signature:$signature")
        if (bytesRead < 6) {
            return false
        }

        val gif87a = byteArrayOf('G'.toByte(), 'I'.toByte(), 'F'.toByte(), '8'.toByte(), '7'.toByte(), 'a'.toByte())
        val gif89a = byteArrayOf('G'.toByte(), 'I'.toByte(), 'F'.toByte(), '8'.toByte(), '9'.toByte(), 'a'.toByte())

        return signature.contentEquals(gif87a) || signature.contentEquals(gif89a)
    }

    /**
     * 检测文件是否为GIF格式
     * @param filePath 文件路径
     * @return 是否为GIF格式
     */
    fun isGifFile(filePath: String): Boolean {
        val signature = ByteArray(6)
        var fileInputStream: FileInputStream? = null
        return try {
            fileInputStream = FileInputStream(filePath)
            val bytesRead = fileInputStream.read(signature, 0, 6)
            Log.i(TAG, "isGif# bytesRead:$bytesRead, signature:$signature")
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

    /**
     * 使用Glide检测文件是否为GIF格式（异步方式）
     * @param context 上下文
     * @param filePath 文件路径
     * @param callback 回调函数
     */
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
                    Log.i(TAG, "isGifFile# onResourceReady isGif:$isGif, resource:${resource}")
                    return false
                }
            })
            .submit()
    }

    /**
     * 获取文件的MIME类型
     * @param file 文件对象
     * @return MIME类型字符串
     */
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
            Log.i(TAG, "getMimeType# onResourceReady buffer:${buffer[i]}")
        }

        return when {
            isGif(buffer) -> "image/gif"
            isJpeg(buffer) -> "image/jpeg"
            isPng(buffer) -> "image/png"
            else -> null
        }
    }

    /**
     * 检测字节数组是否为GIF格式
     * @param buffer 字节数组
     * @return 是否为GIF格式
     */
    fun isGif(buffer: ByteArray): Boolean {
        val gifHeader87a = byteArrayOf('G'.toByte(), 'I'.toByte(), 'F'.toByte(), '8'.toByte(), '7'.toByte(), 'a'.toByte())
        val gifHeader89a = byteArrayOf('G'.toByte(), 'I'.toByte(), 'F'.toByte(), '8'.toByte(), '9'.toByte(), 'a'.toByte())
        return buffer.copyOfRange(0, 6).contentEquals(gifHeader87a) || buffer.copyOfRange(0, 6).contentEquals(gifHeader89a)
    }

    /**
     * 检测字节数组是否为JPEG格式
     * @param buffer 字节数组
     * @return 是否为JPEG格式
     */
    fun isJpeg(buffer: ByteArray): Boolean {
        val jpegHeader = byteArrayOf(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte())
        return buffer.copyOfRange(0, 3).contentEquals(jpegHeader)
    }

    /**
     * 检测字节数组是否为PNG格式
     * @param buffer 字节数组
     * @return 是否为PNG格式
     */
    fun isPng(buffer: ByteArray): Boolean {
        val pngHeader = byteArrayOf(0x89.toByte(), 0x50.toByte(), 0x4E.toByte(), 0x47.toByte(), 0x0D.toByte(), 0x0A.toByte(), 0x1A.toByte(), 0x0A.toByte())
        return buffer.contentEquals(pngHeader)
    }

    /**
     * 输出系统信息和调试日志
     * @param logTag 日志标签
     */
    fun logSystemInfo(logTag: String) {
        Log.i("Main##", "model:${Build.MODEL}")
        Log.i("FrankTest", "$logTag classLoader## String: " + String.Companion::class.java.classLoader)
        Log.i("FrankTest", "$logTag classLoader## Activity: " + android.app.Activity::class.java.classLoader)
        Log.i("FrankTest", "$logTag classLoader## MainActivity: " + MainActivity::class.java.classLoader)
    }
} 