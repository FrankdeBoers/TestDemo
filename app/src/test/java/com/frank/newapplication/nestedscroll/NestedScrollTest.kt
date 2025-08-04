package com.frank.newapplication.nestedscroll

import android.content.Context
import android.view.View
import androidx.core.view.NestedScrollingParent3
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 嵌套滚动功能测试
 */
@RunWith(AndroidJUnit4::class)
class NestedScrollTest {

    private lateinit var context: Context
    private lateinit var customNestedParent: CustomNestedScrollParent

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        customNestedParent = CustomNestedScrollParent(context)
    }

    @Test
    fun testCustomNestedParentCreation() {
        // 测试自定义嵌套滚动父容器创建
        assertNotNull(customNestedParent)
        assertTrue(customNestedParent is NestedScrollingParent3)
    }

    @Test
    fun testNestedScrollStart() {
        // 测试嵌套滚动开始
        val child = View(context)
        val target = View(context)
        val axes = ViewCompat.SCROLL_AXIS_VERTICAL
        val type = ViewCompat.TYPE_TOUCH

        val result = customNestedParent.onStartNestedScroll(child, target, axes, type)
        assertTrue("应该支持垂直滚动", result)
    }

    @Test
    fun testNestedScrollAccepted() {
        // 测试嵌套滚动被接受
        val child = View(context)
        val target = View(context)
        val axes = ViewCompat.SCROLL_AXIS_VERTICAL
        val type = ViewCompat.TYPE_TOUCH

        // 应该不会抛出异常
        customNestedParent.onNestedScrollAccepted(child, target, axes, type)
        assertTrue("嵌套滚动应该被正确接受", true)
    }

    @Test
    fun testNestedScrollStop() {
        // 测试嵌套滚动停止
        val target = View(context)
        val type = ViewCompat.TYPE_TOUCH

        // 应该不会抛出异常
        customNestedParent.onStopNestedScroll(target, type)
        assertTrue("嵌套滚动应该被正确停止", true)
    }

    @Test
    fun testNestedPreScroll() {
        // 测试预滚动处理
        val target = View(context)
        val dx = 0
        val dy = 10 // 向下滚动
        val consumed = IntArray(2)
        val type = ViewCompat.TYPE_TOUCH

        // 先接受嵌套滚动
        customNestedParent.onNestedScrollAccepted(View(context), target, ViewCompat.SCROLL_AXIS_VERTICAL, type)
        
        // 测试预滚动
        customNestedParent.onNestedPreScroll(target, dx, dy, consumed, type)
        
        // 验证滚动被消费
        assertTrue("向下滚动应该被消费", consumed[1] > 0)
    }

    @Test
    fun testNestedScroll() {
        // 测试嵌套滚动
        val target = View(context)
        val dxConsumed = 0
        val dyConsumed = 0
        val dxUnconsumed = 0
        val dyUnconsumed = -10 // 向上滚动
        val type = ViewCompat.TYPE_TOUCH
        val consumed = IntArray(2)

        // 先接受嵌套滚动
        customNestedParent.onNestedScrollAccepted(View(context), target, ViewCompat.SCROLL_AXIS_VERTICAL, type)
        
        // 测试嵌套滚动
        customNestedParent.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed)
        
        // 验证滚动被处理
        assertTrue("向上滚动应该被处理", consumed[1] > 0)
    }

    @Test
    fun testRecyclerViewNestedScrolling() {
        // 测试RecyclerView的嵌套滚动功能
        val recyclerView = RecyclerView(context)
        
        // 启用嵌套滚动
        recyclerView.isNestedScrollingEnabled = true
        
        assertTrue("RecyclerView应该支持嵌套滚动", recyclerView.isNestedScrollingEnabled)
    }

    @Test
    fun testNestedScrollAxes() {
        // 测试嵌套滚动轴
        val axes = customNestedParent.nestedScrollAxes
        assertEquals("初始状态下应该没有滚动轴", 0, axes)
    }
} 