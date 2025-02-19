package com.frank.newapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.frank.newapplication.handler.BarrierActivity
import com.frank.newapplication.http.OkhttpActivity
import com.frank.newapplication.rv.RecyclerViewActivity
import com.frank.newapplication.threadpool.ThreadPoolActivity
import com.frank.newapplication.weakreference.WeakReferenceActivity
import okhttp3.*
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    private val webSocket = SocketManager()

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


        initClick()

        Log.i("FrankTest", "$logTag classLoader## String: ${String.Companion::class.java.classLoader}")
        Log.i("FrankTest", "$logTag classLoader## Activity: ${Activity::class.java.classLoader}")
        Log.i("FrankTest", "$logTag classLoader## MainActivity: ${MainActivity::class.java.classLoader}")
    }

    private fun initClick() {
        binding.startSecondActivity.setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
        }

        binding.startWeakActivity.setOnClickListener {
            startActivity(Intent(this, WeakReferenceActivity::class.java))
        }

        binding.startThread.setOnClickListener {
            startActivity(Intent(this, ThreadPoolActivity::class.java))
        }

        binding.startCoroutineActivity.setOnClickListener {
            startActivity(Intent(this, CoroutineActivity::class.java))
        }

        binding.startBmp.setOnClickListener {
            startActivity(Intent(this, BitmapActivity::class.java))
        }

        binding.startOkHttp.setOnClickListener {
            startActivity(Intent(this, OkhttpActivity::class.java))
        }

        binding.startBarrier.setOnClickListener {
            startActivity(Intent(this, BarrierActivity::class.java))
        }

        binding.startRV.setOnClickListener {
            startActivity(Intent(this, RecyclerViewActivity::class.java))
        }

        binding.longLink.setOnClickListener {
            webSocket.connect()
        }

        binding.sendMsg.setOnClickListener {
            webSocket.sendMessage("" +
                    "" +
                    "所谓的心跳包就是客户端定时放送简单的信息给服务器端，告诉它我还在而已。代码就是每隔几分钟发送一个固定信息给服务器端，服务器端回复一个固定信息。如果服务器端几分钟后没有收到客户端信息则视客户端断开。比如有些通信软件长时间不适用，要想知道它的状态是在线还是离线，就需要心跳包，定时发包收包。" +
                    "" +
                    "心跳包之所以叫心跳包是因为：它像心跳一样每隔固定时间发一次，以此来告诉服务器，这个客户端还活在。事实上这是为了保持长连接，至于这个包的内容，是没有什么特别规定的，不过一般都是很小的包，或者只包含包头的一个空包。" +
                    "" +
                    "在TCP机制里面，本身是存在有心跳包机制的，也就是TCP选项:SO_KEEPALIVE. 系统默认是设置的2小时的心跳频率。" +
                    "" +
                    "心跳包的机制，其实就是传统的长连接。或许有的人知道消息推送的机制，消息推送也是一种长连接 ，是将数据有服务器端推送到客户端这边从而改变传统的“拉”的请求方式。下面我来介绍一下安卓和客户端两个数据请求的方式" +
                    "" +
                    "    push 这个也就是有服务器推送到客户端这边 现在有第三方技术 比如极光推送。" +
                    "    pull 这种方式就是客户端向服务器发送请求数据（http请求）" +
                    "" +
                    "1、首先服务器和客户端有一次“握手”" +
                    "" +
                    "public void connect()  {  " +
                    "        LogUtil.e(TAG, \"准备链接...\");  " +
                    "        InetAddress serverAddr;  " +
                    "        try {  " +
                    "            socket = new Socket(Config.Host, Config.SockectPort);  " +
                    "            _connect = true;  " +
                    "            mReceiveThread = new ReceiveThread();  " +
                    "            receiveStop = false;  " +
                    "            mReceiveThread.start();  " +
                    "            LogUtil.e(TAG, \"链接成功.\");  " +
                    "" +
                    "        } catch (Exception e) {  " +
                    "            LogUtil.e(TAG, \"链接出错.\" + e.getMessage().toString());  " +
                    "            e.printStackTrace();  " +
                    "        }  " +
                    "}" +
                    "" +
                    "2、下面就要开启一个线程 去不断读取服务器那边传过来的数据 采用Thread去实现" +
                    "" +
                    "private class ReceiveThread extends Thread {  " +
                    "        private byte[] buf;  " +
                    "        private String str = null;  " +
                    "" +
                    "        @Override  " +
                    "        public void run() {  " +
                    "            while (true) {  " +
                    "                try {  " +
                    "                    // LogUtil.e(TAG, \"监听中...:\"+socket.isConnected());  " +
                    "                    if (socket!=null && socket.isConnected()) {  " +
                    "" +
                    "                        if (!socket.isInputShutdown()) {  " +
                    "                            BufferedReader inStream = new BufferedReader(  " +
                    "                                    new InputStreamReader(  " +
                    "                                            socket.getInputStream()));  " +
                    "                            String content = inStream.readLine();                              " +
                    "                            if (content == null)  " +
                    "                                continue;  " +
                    "                            LogUtil.e(TAG, \"收到信息:\" + content);  " +
                    "                            LogUtil.e(TAG, \"信息长度:\"+content.length());  " +
                    "                            if (!content.startsWith(\"CMD:\"))  " +
                    "                                continue;  " +
                    "                            int spacePos = content.indexOf(\" \");  " +
                    "                            if (spacePos == -1)  " +
                    "                                continue;  " +
                    "                            String cmd = content.substring(4, spacePos);  " +
                    "                            String body = content.substring(spacePos).trim();  " +
                    "                            LogUtil.e(TAG, \"收到信息(CMD):\" + cmd);  " +
                    "                            LogUtil.e(TAG, \"收到信息(BODY):\" + body);  " +
                    "                            if (cmd.equals(\"LOGIN\"))  " +
                    "                           {  " +
                    "                                // 登录  " +
                    "                                ReceiveLogin(body);  " +
                    "                                continue;  " +
                    "                            }  " +
                    "                              if (cmd.equals(\"KEEPLIVE\")) {  " +
                    "                                if (!body.equals(\"1\")) {  " +
                    "                                    Log.e(TAG, \"心跳时检测到异常，重新登录!\");  " +
                    "                                    socket = null;  " +
                    "                                    KeepAlive();  " +
                    "                                } else {  " +
                    "                                    Date now = Calendar.getInstance().getTime();  " +
                    "                                    lastKeepAliveOkTime = now;  " +
                    "                                }  " +
                    "                                continue;  " +
                    "                            }  " +
                    "                        }  " +
                    "                    } else {  " +
                    "                        if(socket!=null)  " +
                    "                            LogUtil.e(TAG, \"链接状态:\" + socket.isConnected());  " +
                    "                    }  " +
                    "" +
                    "                } catch (Exception e) {  " +
                    "                    LogUtil.e(TAG, \"监听出错:\" + e.toString());  " +
                    "                    e.printStackTrace();  " +
                    "                }  " +
                    "            }  " +
                    " }" +
                    "" +
                    "3 、 Socket 是否断开了 断开了 需要重新去连接" +
                    "" +
                    "public void KeepAlive() {  " +
                    "        // 判断socket是否已断开,断开就重连  " +
                    "        if (lastKeepAliveOkTime != null) {  " +
                    "            LogUtil.e(  " +
                    "                    TAG,  " +
                    "                    \"上次心跳成功时间:\"  " +
                    "                            + DateTimeUtil.dateFormat(lastKeepAliveOkTime,  " +
                    "                                    \"yyyy-MM-dd HH:mm:ss\"));  " +
                    "            Date now = Calendar.getInstance().getTime();  " +
                    "            long between = (now.getTime() - lastKeepAliveOkTime.getTime());// 得到两者的毫秒数  " +
                    "            if (between > 60 * 1000) {  " +
                    "                LogUtil.e(TAG, \"心跳异常超过1分钟,重新连接:\");  " +
                    "                lastKeepAliveOkTime = null;  " +
                    "                socket = null;  " +
                    "            }  " +
                    "" +
                    "        } else {  " +
                    "            lastKeepAliveOkTime = Calendar.getInstance().getTime();  " +
                    "        }  " +
                    "" +
                    "        if (!checkIsAlive()) {  " +
                    "            LogUtil.e(TAG, \"链接已断开,重新连接.\");  " +
                    "            connect();  " +
                    "            if (loginPara != null)  " +
                    "                Login(loginPara);  " +
                    "        }" +
                    "}  " +
                    "" +
                    "//此方法是检测是否连接  " +
                    "boolean checkIsAlive() {  " +
                    "        if (socket == null)  " +
                    "            return false;  " +
                    "            try {  " +
                    "            socket.sendUrgentData(0xFF);  " +
                    "        } catch (IOException e) {  " +
                    "            return false;  " +
                    "        }  " +
                    "        return true;  " +
                    "" +
                    "}  " +
                    "//然后发送数据的方法  " +
                    "public void sendmessage(String msg) {  " +
                    "        if (!checkIsAlive())  " +
                    "            return;  " +
                    "        LogUtil.e(TAG, \"准备发送消息:\" + msg);  " +
                    "        try {  " +
                    "            if (socket != null && socket.isConnected()) {  " +
                    "                if (!socket.isOutputShutdown()) {  " +
                    "                    PrintWriter outStream = new PrintWriter(new BufferedWriter(  " +
                    "                            new OutputStreamWriter(socket.getOutputStream())),  " +
                    "                            true);  " +
                    "" +
                    "                    outStream.print(msg + (char) 13 + (char) 10);  " +
                    "                    outStream.flush();  " +
                    "                }  " +
                    "            }  " +
                    "            LogUtil.e(TAG, \"发送成功!\");  " +
                    "        } catch (Exception e) {  " +
                    "            e.printStackTrace();  " +
                    "        }  " +
                    "}" +
                    "" +
                    "实现轮询" +
                    "" +
                    "原理" +
                    "其原理在于在android端的程序中，让一个SERVICE一直跑在后台，在规定时间之内调用服务器接口进行数据获取。" +
                    "" +
                    "这里的原理很简单，当然实现起来也不难；" +
                    "" +
                    "然后，这个类之中肯定要做网络了数据请求，所以我们在Service中建立一个线程（因为在android系统中网络请求属于长时间操作，不能放主线程，不然会导致异常），在线程中和服务器进行通信。" +
                    "" +
                    "最后，这个逻辑写完后，我们需要考虑一个问题，如何进行在规定时间内调用该服务器，当然可以用Thread+Handler(这个不是那么稳定),也可以使用AlamManager+Thread（比较稳定），因为我们需要其在后台一直运行，所以可以依靠系统的Alammanager这个类来实现，Alammanager是属于系统的一个闹钟提醒类，通过它我们能实现在规定间隔时间调用，并且也比较稳定，这个service被杀后会自己自动启动服务。")
        }
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

