package com.frank.newapplication.handler

import android.os.*
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.frank.newapplication.R
import kotlinx.coroutines.launch
import java.lang.reflect.Method

class BarrierActivity2 : AppCompatActivity() {

    private var syncBarrierToken: Int = -1
    private var mQueue: Any? = null
    private var removeSyncBarrierMethod: Method? = null
    private var postSyncBarrierMethod: Method? = null
    private var isBarrierInserted = false
    private lateinit var tvStatus: TextView
    private lateinit var btnInsert: Button
    private lateinit var btnDetect: Button
    private lateinit var btnRemove: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.barrier)

        btnInsert = findViewById(R.id.btnBlock)
        tvStatus = findViewById(R.id.tvStatus)
        btnInsert.text = "插入同步屏障（模拟假死）"

        // 新增检测和移除按钮
        btnDetect = Button(this).apply { text = "检测假死" }
        btnRemove = Button(this).apply { text = "移除同步屏障" }
        (tvStatus.parent as? android.widget.LinearLayout)?.apply {
            addView(btnDetect)
            addView(btnRemove)
        }

        btnInsert.setOnClickListener {
            tvStatus.text = "插入同步屏障，页面即将假死"
            insertSyncBarrier()
        }
        btnDetect.setOnClickListener {
            detectMainThreadBlock()
        }
        btnRemove.setOnClickListener {
            removeSyncBarrier()
        }

        lifecycleScope.launch {  }

        // 定时更新UI，验证同步消息是否还能被处理
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable {
            override fun run() {
                tvStatus.append("\n" + System.currentTimeMillis())
                handler.postDelayed(this, 1000)
            }
        }, 1000)

        // 异步消息测试
        val asyncHandler = Handler(Looper.getMainLooper()) {
            tvStatus.append("\n异步消息收到")
            true
        }
        val asyncMsg = Message.obtain()
        asyncMsg.isAsynchronous = true
        btnInsert.postDelayed({ asyncHandler.sendMessage(asyncMsg) }, 3000)
    }

    /**
     * 通过反射插入同步屏障
     */
    private fun insertSyncBarrier() {
        try {
            val queueField = Looper.getMainLooper().javaClass.getDeclaredField("mQueue")
            queueField.isAccessible = true
            mQueue = queueField.get(Looper.getMainLooper())
            if (postSyncBarrierMethod == null) {
                postSyncBarrierMethod = mQueue!!.javaClass.getDeclaredMethod("postSyncBarrier")
                postSyncBarrierMethod!!.isAccessible = true
            }
            if (removeSyncBarrierMethod == null) {
                removeSyncBarrierMethod = mQueue!!.javaClass.getDeclaredMethod("removeSyncBarrier", Int::class.javaPrimitiveType)
                removeSyncBarrierMethod!!.isAccessible = true
            }
            syncBarrierToken = postSyncBarrierMethod!!.invoke(mQueue) as Int
            isBarrierInserted = true
        } catch (e: Exception) {
            tvStatus.append("\n插入屏障失败: ${e.message}")
        }
    }

    /**
     * 检测主线程是否假死（通过Handler消息延迟检测）
     */
    private fun detectMainThreadBlock() {
        val start = System.currentTimeMillis()
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            val delay = System.currentTimeMillis() - start
            if (delay > 1500) {
                tvStatus.append("\n检测到主线程假死，延迟: ${delay}ms")
            } else {
                tvStatus.append("\n主线程正常，延迟: ${delay}ms")
            }
        }
    }

    /**
     * 通过反射移除同步屏障，恢复主线程响应
     */
    private fun removeSyncBarrier() {
        try {
            if (isBarrierInserted && mQueue != null && removeSyncBarrierMethod != null) {
                removeSyncBarrierMethod!!.invoke(mQueue, syncBarrierToken)
                tvStatus.append("\n已移除同步屏障，主线程恢复响应")
                isBarrierInserted = false
            } else {
                tvStatus.append("\n未检测到可移除的同步屏障")
            }
        } catch (e: Exception) {
            tvStatus.append("\n移除屏障失败: ${e.message}")
        }
    }
} 