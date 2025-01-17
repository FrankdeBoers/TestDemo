package com.frank.newapplication.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object CommonUtils {

    /**
     * 获取文件的SHA256
     * 支持输入：
     * 1、文件绝对路径，如 /sdcard/DCIM/Camera/IMG_20220802160228119.jpg
     * 2、文件Uri路径，如 content://com.android.providers.media.documents/document/video%3A607
     *
     * */
    fun getFileSHA256(context: Context, filePath: String): String? {
        val inputStream: InputStream? =
            try {
                if (filePath.startsWith("content")) {
                    context.contentResolver.openInputStream(Uri.parse(filePath))
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
