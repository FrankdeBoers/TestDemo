package com.frank.newapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frank.newapplication.BitmapActivity.BitmapActivity
import com.frank.newapplication.coroutine.CoroutineActivity
import com.frank.newapplication.coroutine.CoroutineExceptionDemoActivity
import com.frank.newapplication.handler.BarrierActivity2
import com.frank.newapplication.http.OkhttpActivity
import com.frank.newapplication.nestedscroll.CoordinatorLayoutActivity
import com.frank.newapplication.nestedscroll.NestedScrollActivity
import com.frank.newapplication.nestedscroll.NestedScrollDemoActivity
import com.frank.newapplication.rv.RecyclerViewActivity
import com.frank.newapplication.threadpool.ThreadPoolActivity
import com.frank.newapplication.weakreference.WeakReferenceActivity

class MainComposeActivity : ComponentActivity() {

    private val webSocket = SocketManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(webSocket)
                }
            }
        }
    }
}

@Composable
fun MainScreen(webSocket: SocketManager) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // 3. 功能按钮网格
        FunctionButtonsGrid(context, webSocket)
    }
}

@Composable
fun FunctionButtonsGrid(context: android.content.Context, webSocket: SocketManager) {
    val functionButtons = listOf(
        FunctionButton("glide加载Gif") {
            // 这里可以添加GIF加载逻辑
        },
        FunctionButton("长链接") {
            webSocket.connect()
        },
        FunctionButton("发消息") {
            webSocket.sendMessage("心跳包测试消息")
        },
        FunctionButton("启动第二Activity") {
            context.startActivity(Intent(context, SecondActivity::class.java))
        },
        FunctionButton("启动Weak") {
            context.startActivity(Intent(context, WeakReferenceActivity::class.java))
        },
        FunctionButton("线程池") {
            context.startActivity(Intent(context, ThreadPoolActivity::class.java))
        },
        FunctionButton("协程性能监控") {
            context.startActivity(Intent(context, CoroutineActivity::class.java))
        },
        FunctionButton("协程异常保护演示") {
            context.startActivity(Intent(context, CoroutineExceptionDemoActivity::class.java))
        },
        FunctionButton("图片加载") {
            context.startActivity(Intent(context, BitmapActivity::class.java))
        },
        FunctionButton("OkHttp") {
            context.startActivity(Intent(context, OkhttpActivity::class.java))
        },
        FunctionButton("Barrier") {
            context.startActivity(Intent(context, BarrierActivity2::class.java))
        },
        FunctionButton("RecyclerView测试") {
            context.startActivity(Intent(context, RecyclerViewActivity::class.java))
        },
        FunctionButton("安全页面（禁止截屏）") {
            try {
                context.startActivity(Intent(context, Class.forName("com.example.syncbarrierdemo.SecureActivity")))
            } catch (e: ClassNotFoundException) {
                // 处理类未找到的情况
            }
        },
        FunctionButton("KeyStore用法演示") {
            context.startActivity(Intent(context, KeyStoreDemoActivity::class.java))
        },
        FunctionButton("Fragment版生物识别演示") {
            context.startActivity(Intent(context, BiometricFragmentHostActivity::class.java))
        },
        FunctionButton("Thread状态演示") {
            context.startActivity(Intent(context, ThreadStateActivity::class.java))
        },
        FunctionButton("ServiceManager sCache查看") {
            context.startActivity(Intent(context, ServiceManagerCacheActivity::class.java))
        },
        FunctionButton("Canvas截图演示") {
            context.startActivity(Intent(context, CanvasScreenshotActivity::class.java))
        },
        FunctionButton("嵌套滚动演示") {
            context.startActivity(Intent(context, NestedScrollActivity::class.java))
        },
        FunctionButton("CoordinatorLayout演示") {
            context.startActivity(Intent(context, CoordinatorLayoutActivity::class.java))
        },
        FunctionButton("简化嵌套滚动演示") {
            context.startActivity(Intent(context, NestedScrollDemoActivity::class.java))
        }
    )

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 120.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(functionButtons) { button ->
            FunctionButtonItem(button)
        }
    }
}

@Composable
fun FunctionButtonItem(button: FunctionButton) {
    Button(
        onClick = button.onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Text(
            text = button.text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 2
        )
    }
}

data class FunctionButton(
    val text: String,
    val onClick: () -> Unit,
) 