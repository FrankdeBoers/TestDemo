package com.frank.newapplication

import android.util.Log
import com.frank.newapplication.WebSocketManager.Companion.SERVER_URL
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket


class SocketManager {
    private var socket: Socket? = null
    private var out: PrintWriter? = null
    private var `in`: BufferedReader? = null

    fun connect() {
        Thread {
            try {
                socket = Socket("tcpbin.com", 4242) // 替换为你的服务器地址和端口

                out = PrintWriter(socket!!.getOutputStream(), true)
                `in` = BufferedReader(InputStreamReader(socket!!.getInputStream()))

                // 读取服务器发送的数据
                var message: String
                while ((`in`!!.readLine().also { message = it }) != null) {
                    Log.i("Main##", "Received: $message")
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    fun sendMessage(message: String?) {
        Thread {
            if (out != null) {
                out!!.println(message)
            }
        }.start()
    }

    fun disconnect() {
        try {
            if (`in` != null) `in`!!.close()
            if (out != null) out!!.close()
            if (socket != null) socket!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}


class WebSocketManager {
    lateinit var webSocket: WebSocket
    private var client = OkHttpClient()

    fun connect() {
        client = OkHttpClient()

        val request: Request = Request.Builder()
            .url(SERVER_URL)
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Log.i("Main##", "WebSocket Connected!")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                Log.i("Main##", "Message Received: $text")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                Log.i("Main##", "WebSocket Error: " + t.message)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                Log.i("Main##", "WebSocket Closing: $reason")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                Log.i("Main##", "WebSocket Closed: $reason")
            }
        })
    }

    fun sendMessage(message: String?) {
        if (webSocket != null) {
            if (message != null) {
                webSocket.send(message)
            }
        }
    }

    fun disconnect() {
        if (webSocket != null) {
            webSocket.close(1000, "Normal Closure")
        }
        if (client != null) {
            client.dispatcher.executorService.shutdown()
        }
    }

    companion object {
        const val SERVER_URL = "wss://echo.websocket.events" // 替换为你的 WebSocket URL
    }
}
