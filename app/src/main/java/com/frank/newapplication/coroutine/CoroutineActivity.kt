package com.frank.newapplication.coroutine

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.frank.newapplication.BaseActivity
import com.frank.newapplication.databinding.ActivityCoroutineBinding
import com.frank.newapplication.utils.CommonUtils.getFileSHA256
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import com.frank.newapplication.coroutine.CoroutineUtils
import com.frank.newapplication.coroutine.CoroutinePerformanceAnalyzer
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class CoroutineActivity : BaseActivity() {

    private lateinit var binding: ActivityCoroutineBinding

    @Volatile
    var a = true
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext: CoroutineContext, throwable: Throwable ->
        Log.e("FrankTest", "$logTag coroutine# exceptionHandler:${throwable}")
    }

    private val jobScope = CoroutineScope(Job() + exceptionHandler)
    private val supervisorScope = CoroutineScope(SupervisorJob() + exceptionHandler)

    // 协程性能监控相关
    private val performanceHelper = CoroutinePerformanceHelper.getInstance()
    private val performanceInterceptor = CoroutinePerformanceInterceptor()
    private val advancedInterceptor = AdvancedCoroutineInterceptor()

    override fun onCreate(savedInstanceState: Bundle?) {
        Int.MAX_VALUE
        super.onCreate(savedInstanceState)
        binding = ActivityCoroutineBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initClick()
        
        // 演示获取调用方类名
        demonstrateCallerDetection()
    }
    
    /**
     * 演示获取调用方类名的功能
     */
    private fun demonstrateCallerDetection() {
        Log.i("FrankTest", "$logTag 演示获取调用方类名")
        
        // 使用原始的launch1函数
        GlobalScope.launch1 {
            Log.i("FrankTest", "$logTag launch1协程执行")
        }
        
        // 使用新的工具类
        val callerName = CoroutineUtils.getCallerClassName()
        Log.i("FrankTest", "$logTag 当前调用方类名: $callerName")
        
        val callerInfo = CoroutineUtils.getCallerInfo()
        Log.i("FrankTest", "$logTag 调用方详细信息: $callerInfo")
        
        // 使用新的扩展函数
        GlobalScope.launchWithCaller {
            Log.i("FrankTest", "$logTag launchWithCaller协程执行")
        }
        
        GlobalScope.launchWithCallerName {
            Log.i("FrankTest", "$logTag launchWithCallerName协程执行")
        }
        
        GlobalScope.launchWithDetailedInfo {
            Log.i("FrankTest", "$logTag launchWithDetailedInfo协程执行")
        }
    }

    private fun initClick() {
        Log.i("FrankTest", "$logTag coroutine# name0:${Thread.currentThread().name}")
        binding.startCoroutine.setOnClickListener {
            lifecycleScope.launch {
                Log.i("FrankTest", "$logTag coroutine# name1:${Thread.currentThread().name}")
                val startTime = System.currentTimeMillis()
                val file1 = "/data/data/com.frank.newapplication/files/HoYowave_mac_x64_1.13.0-alpha(0117.094509.dev.1a1ee434ac).dmg"
                val file2 = "/data/data/com.frank.newapplication/files/HoYowave_mac_x64_1.13.0-alpha(0117.094509.dev.1a1ee434ac)_副本2.dmg"
                val hash1 = async {
                    delay(300L)
                    Log.i("FrankTest", "$logTag coroutine# hash1 start name:${Thread.currentThread().name}")
                    getHash(file1)
                }
                val hash2 = async {
                    delay(100L)
                    getHash(file2)
                    Log.i("FrankTest", "$logTag coroutine# hash2 start name:${Thread.currentThread().name}")
                }

                coroutineScope {

                }

                delay(2000L)
                Log.i("FrankTest", "$logTag coroutine# delay 2000ms")
                delay(100L)
                val result = "hash1:${hash1.await()}, hash2:${hash2.await()}"
                Log.i("FrankTest", "$logTag coroutine# cost:${System.currentTimeMillis() - startTime},result:$result")
            }

            val scope = CoroutineScope(EmptyCoroutineContext)
            scope.launch {
                Log.i("FrankTest", "$logTag coroutine# name3:${Thread.currentThread().name}")
            }
        }

        // 普通Job
        binding.jobCoroutine.setOnClickListener {
            jobScope.launch {
                delay(100L)
                Log.i("FrankTest", "$logTag jobCoroutine1")
                throw Exception("jobCoroutine1 failed!!")
            }

            jobScope.launch {
                delay(200L)
                Log.i("FrankTest", "$logTag jobCoroutine2")
            }

            jobScope.launch {
                delay(300L)
                Log.i("FrankTest", "$logTag jobCoroutine3")
            }
        }

        // SupervisorJob
        binding.supervisorJobCoroutine.setOnClickListener {
            supervisorScope.launch {
                delay(100L)
                Log.i("FrankTest", "$logTag supervisorJobCoroutine1")
                throw Exception("supervisorJobCoroutine1 failed!!")
            }

            supervisorScope.launch {
                delay(200L)
                Log.i("FrankTest", "$logTag supervisorJobCoroutine2")
            }

            supervisorScope.launch {
                delay(300L)
                Log.i("FrankTest", "$logTag supervisorJobCoroutine3")
                throw Exception("supervisorJobCoroutine3 failed!!")
            }
        }

        binding.multiThreadVisible.setOnClickListener {
            var i = 0

            for (j in 0 until 1000) {
                Thread({
                    i++
                }).start()
                Thread({
                    i++
                }).start()
            }


            Thread.sleep(200)
            Log.i("FrankTest", "$logTag multiThreadVisible i:$i")
        }

        // 协程性能监控演示
        binding.performanceMonitor.setOnClickListener {
            Log.i("FrankTest", "$logTag 开始协程性能监控演示")

            // 显示提示信息
            Toast.makeText(this, "协程性能监控已启动，请查看Logcat输出", Toast.LENGTH_LONG).show()

            // 演示1: 使用ContinuationInterceptor监控协程
//            demonstrateContinuationInterceptor()

            // 演示2: 使用性能监控辅助类
            demonstratePerformanceHelper()

            // 演示3: 使用高级协程拦截器
//            demonstrateAdvancedInterceptor()
        }

                // 查看性能报告
        binding.performanceReport.setOnClickListener {
            Log.i("FrankTest", "$logTag 查看性能报告")
            
            // 基础性能报告
            val basicReport = performanceHelper.getPerformanceReport()
            Log.i("FrankTest", "$logTag 基础性能报告:\n$basicReport")
            
            // 高级协程统计
            val advancedStats = AdvancedCoroutineInterceptor.getGlobalStats()
            Log.i("FrankTest", "$logTag 高级协程统计:\n$advancedStats")
            
            // 协程执行统计
            val coroutineStats = advancedInterceptor.getCoroutineStats()
            Log.i("FrankTest", "$logTag 协程执行统计:\n$coroutineStats")
            
            // 启动getCallerClassName性能分析
            startPerformanceAnalysis()
            
            // 显示提示信息
            Toast.makeText(this, "性能报告已输出到Logcat，请查看控制台", Toast.LENGTH_LONG).show()
        }
    }

    private suspend fun getHash(path: String) = withContext(Dispatchers.IO) {
        Log.i("FrankTest", "$logTag coroutine# name2:${Thread.currentThread().name}")
        getFileSHA256(this@CoroutineActivity, path)
    }

    /**
     * 演示ContinuationInterceptor的使用
     */
    private fun demonstrateContinuationInterceptor() {
        Log.i("FrankTest", "$logTag 演示ContinuationInterceptor监控")

        // 创建带性能监控的协程作用域
        val performanceScope = CoroutineScope(
            SupervisorJob() +
                    performanceInterceptor +
                    Dispatchers.Default
        )

        // 启动多个协程进行监控
        performanceScope.launch {
            Log.i("FrankTest", "$logTag 协程1开始执行")
            delay(500L) // 模拟耗时操作
            Log.i("FrankTest", "$logTag 协程1执行完成")
        }

        performanceScope.launch {
            Log.i("FrankTest", "$logTag 协程2开始执行")
            delay(300L) // 模拟耗时操作
            Log.i("FrankTest", "$logTag 协程2执行完成")
        }

//        performanceScope.launch {
//            Log.i("FrankTest", "$logTag 协程3开始执行")
//            delay(100L) // 模拟耗时操作
//            // 模拟异常
//            throw Exception("协程3模拟异常")
//        }
    }

    /**
     * 演示性能监控辅助类的使用
     */
    private fun demonstratePerformanceHelper() {
        Log.i("FrankTest", "$logTag 演示性能监控辅助类")

        // 启动带性能监控的协程
        performanceHelper.launchWithPerformance("网络请求协程") {
            Log.i("FrankTest", "$logTag 开始网络请求")
            simulateSuspendOperation("网络请求", 800L)
            Log.i("FrankTest", "$logTag 网络请求完成")
        }

        performanceHelper.launchWithPerformance("数据库操作协程") {
            Log.i("FrankTest", "$logTag 开始数据库操作")
            simulateSuspendOperation("数据库查询", 200L)
            simulateSuspendOperation("数据库写入", 300L)
            Log.i("FrankTest", "$logTag 数据库操作完成")
        }

        performanceHelper.launchWithPerformance("文件操作协程") {
            Log.i("FrankTest", "$logTag 开始文件操作")
            simulateSuspendOperation("文件读取", 150L)
            simulateSuspendOperation("文件处理", 250L)
            simulateSuspendOperation("文件保存", 180L)
            Log.i("FrankTest", "$logTag 文件操作完成")
        }

        // 启动一个会异常的协程
//        performanceHelper.launchWithPerformance("异常协程") {
//            Log.i("FrankTest", "$logTag 开始异常协程")
//            simulateSuspendOperation("正常操作", 100L)
//            throw RuntimeException("模拟业务异常")
//        }
    }

    /**
     * 演示高级协程拦截器
     */
    private fun demonstrateAdvancedInterceptor() {
        Log.i("FrankTest", "$logTag 演示高级协程拦截器")

        // 创建带高级监控的协程作用域
        val advancedScope = CoroutineScope(
            SupervisorJob() +
                    advancedInterceptor +
                    Dispatchers.Default
        )

        // 启动正常协程
        advancedScope.launch {
            Log.i("FrankTest", "$logTag 高级协程1: 正常执行")
            delay(200L)
            Log.i("FrankTest", "$logTag 高级协程1: 执行完成")
        }

        // 启动慢协程
        advancedScope.launch {
            Log.i("FrankTest", "$logTag 高级协程2: 慢协程开始")
            delay(1500L) // 超过1秒，会被标记为慢协程
            Log.i("FrankTest", "$logTag 高级协程2: 慢协程完成")
        }

        // 启动异常协程
//        advancedScope.launch {
//            Log.i("FrankTest", "$logTag 高级协程3: 异常协程开始")
//            delay(100L)
//            throw RuntimeException("高级协程3模拟异常")
//        }

        // 启动多挂起协程
        advancedScope.launch {
            Log.i("FrankTest", "$logTag 高级协程4: 多挂起协程开始")
            repeat(3) { index ->
                Log.i("FrankTest", "$logTag 高级协程4: 第${index + 1}次挂起")
                delay(150L)
            }
            Log.i("FrankTest", "$logTag 高级协程4: 多挂起协程完成")
        }
    }

    /**
     * 模拟挂起操作
     */
    private suspend fun simulateSuspendOperation(operationName: String, delayTime: Long) {
        Log.i("FrankTest", "$logTag 开始$operationName")
        delay(delayTime)
        Log.i("FrankTest", "$logTag 完成$operationName")
    }
    
    /**
     * 启动性能分析
     */
    private fun startPerformanceAnalysis() {
        lifecycleScope.launch {
            Log.i("FrankTest", "$logTag 开始getCallerClassName性能分析")
            
            try {
                // 分析getCallerClassName性能
                val result = CoroutinePerformanceAnalyzer.analyzeGetCallerClassNamePerformance(
                    iterations = 1000,
                    warmupIterations = 100
                )
                Log.i("FrankTest", "$logTag getCallerClassName性能分析结果:\n$result")
                
                // 对比不同方法的性能
                val comparisonResults = CoroutinePerformanceAnalyzer.compareMethodsPerformance(500)
                val report = CoroutinePerformanceAnalyzer.generatePerformanceReport(comparisonResults)
                Log.i("FrankTest", "$logTag 方法性能对比报告:\n$report")
                
                // 分析调用深度对性能的影响
                val depthResults = CoroutinePerformanceAnalyzer.analyzeCallDepthPerformance(
                    maxDepth = 5,
                    iterationsPerDepth = 100
                )
                val depthReport = CoroutinePerformanceAnalyzer.generatePerformanceReport(depthResults)
                Log.i("FrankTest", "$logTag 调用深度性能分析报告:\n$depthReport")
                
            } catch (e: Exception) {
                Log.e("FrankTest", "$logTag 性能分析失败", e)
            }
        }
    }
}

fun CoroutineScope.launch1(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
) {
    // 使用工具类获取调用方的类名
    val callerClassName = CoroutineUtils.getCallerClassName()
    val name = CoroutineName(callerClassName)
    Log.i("FrankTest", "launch1 callerClassName:$callerClassName")
    launch(
        name, start, block
    )
}