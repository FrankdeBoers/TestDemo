package com.frank.newapplication.coroutine

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.frank.newapplication.R
import com.frank.newapplication.coroutine.exception.*
import kotlinx.coroutines.*
import java.net.ConnectException
import java.net.SocketTimeoutException

/**
 * 简化的协程异常保护演示Activity
 */
class CoroutineExceptionDemoActivity : AppCompatActivity() {
    
    companion object {
        private const val TAG = "CoroutineExceptionDemo"
    }
    
    private lateinit var logTextView: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_exception_demo)
        
        initViews()
        setupButtons()
    }
    
    private fun initViews() {
        logTextView = findViewById(R.id.logTextView)
    }
    
    private fun setupButtons() {
        // 基础异常处理演示
        findViewById<Button>(R.id.btnBasicException).setOnClickListener {
            demonstrateBasicException()
        }

        // 测试lifecycleScope异常捕获
        findViewById<Button>(R.id.btnTestLifecycleScope).setOnClickListener {
            testLifecycleScopeException()
        }
        
        // 清除日志
        findViewById<Button>(R.id.btnClearLog).setOnClickListener {
            clearLog()
        }
    }

    /**
     * 测试lifecycleScope.launch是否能被全局异常处理器捕获
     */
    private fun testLifecycleScopeException() {
        addLog("=== 测试lifecycleScope.launch异常捕获 ===")

        // 测试1：直接使用lifecycleScope.launch（无法被全局异常处理器捕获）
        addLog("测试1：lifecycleScope.launch 直接启动")
        lifecycleScope.launch {
            addLog("lifecycleScope.launch 协程开始执行...")
            delay(500)
            throw RuntimeException("lifecycleScope.launch 抛出的异常")
        }

    }
    
    /**
     * 演示基础异常处理
     */
    private fun demonstrateBasicException() {
        addLog("=== 基础异常处理演示 ===")
        
        // 使用全局异常处理器启动协程
        lifecycleScope.launchWithExceptionHandler {
            addLog("启动协程，将抛出异常...")
            delay(500)
            throw RuntimeException("这是一个测试异常")
        }
        
        // 使用扩展函数启动协程
        lifecycleScope.launchWithExceptionHandler {
            addLog("启动另一个协程，将抛出空指针异常...")
            delay(300)
            val str: String? = null
            str!!.length // 故意抛出空指针异常
        }
    }

    /**
     * 添加日志
     */
    private fun addLog(message: String) {
        Log.d(TAG, message)
        runOnUiThread {
            logTextView.append("$message\n")
            // 自动滚动到底部
            val scrollAmount = logTextView.layout.getLineTop(logTextView.lineCount) - logTextView.height
            if (scrollAmount > 0) {
                logTextView.scrollTo(0, scrollAmount)
            }
        }
    }
    
    /**
     * 清除日志
     */
    private fun clearLog() {
        logTextView.text = ""
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Activity销毁")
    }
} 