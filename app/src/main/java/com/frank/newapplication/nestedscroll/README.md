# 嵌套滚动示例

本示例展示了Android中嵌套滚动的多种实现方式，基于掘金文章《Android嵌套滚动机制详解》的知识点。

## 功能特性

### 1. 基础嵌套滚动 (NestedScrollActivity)
- **外层RecyclerView + 内层RecyclerView**：展示经典的嵌套滚动场景
- **自定义NestedScrollingParent**：实现自定义的嵌套滚动父容器
- **滚动协调**：内外层滚动视图的协调滚动效果

### 2. CoordinatorLayout嵌套滚动 (CoordinatorLayoutActivity)
- **AppBarLayout + RecyclerView**：使用Material Design的嵌套滚动组件
- **可折叠Toolbar**：展示CollapsingToolbarLayout的使用
- **滚动监听**：AppBarLayout的滚动偏移监听

## 技术实现

### 嵌套滚动核心接口

#### NestedScrollingParent3
```kotlin
// 开始嵌套滚动
override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean

// 嵌套滚动被接受
override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int)

// 预滚动处理
override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int)

// 嵌套滚动
override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, 
                          dxUnconsumed: Int, dyUnconsumed: Int, type: Int, consumed: IntArray)

// 停止嵌套滚动
override fun onStopNestedScroll(target: View, type: Int)
```

#### NestedScrollingChild3
```kotlin
// 开始嵌套滚动
fun startNestedScroll(axes: Int, type: Int): Boolean

// 停止嵌套滚动
fun stopNestedScroll(type: Int)

// 分发预滚动
fun dispatchNestedPreScroll(dx: Int, dy: Int, consumed: IntArray?, offsetInWindow: IntArray?, type: Int): Boolean

// 分发嵌套滚动
fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, 
                        offsetInWindow: IntArray?, type: Int): Boolean
```

### 关键配置

#### 启用嵌套滚动
```kotlin
// 在RecyclerView中启用嵌套滚动
recyclerView.isNestedScrollingEnabled = true

// 在ScrollView中启用嵌套滚动
scrollView.isNestedScrollingEnabled = true
```

#### CoordinatorLayout配置
```xml
<!-- 设置RecyclerView的滚动行为 -->
<androidx.recyclerview.widget.RecyclerView
    app:layout_behavior="@string/appbar_scrolling_view_behavior" />

<!-- 设置AppBarLayout的滚动标志 -->
<com.google.android.material.appbar.CollapsingToolbarLayout
    app:layout_scrollFlags="scroll|exitUntilCollapsed" />
```

## 使用场景

### 1. 电商商品详情页
- 外层：商品图片轮播
- 内层：商品详情、规格选择、评价列表

### 2. 新闻资讯页面
- 外层：新闻列表
- 内层：新闻详情、评论列表

### 3. 社交应用动态页
- 外层：动态列表
- 内层：动态详情、评论、点赞列表

### 4. 视频播放页面
- 外层：视频播放器
- 内层：视频信息、评论、推荐列表

## 性能优化建议

1. **避免过度嵌套**：嵌套层级不宜超过3层
2. **合理设置高度**：内层滚动视图高度要合理，避免内容过少
3. **使用RecyclerView**：优先使用RecyclerView而非ScrollView
4. **启用硬件加速**：在复杂嵌套滚动场景下启用硬件加速
5. **合理使用缓存**：对于大量数据的列表，合理使用ViewHolder缓存

## 常见问题

### 1. 滚动冲突
**问题**：内外层滚动视图出现滚动冲突
**解决**：确保正确实现NestedScrollingParent接口，合理分配滚动事件

### 2. 滚动卡顿
**问题**：嵌套滚动时出现卡顿现象
**解决**：检查是否有过度绘制，优化布局层级，使用硬件加速

### 3. 滚动不协调
**问题**：内外层滚动不协调，用户体验差
**解决**：调整滚动优先级，合理设置滚动阈值

## 参考资料

- [Android嵌套滚动机制详解](https://juejin.cn/post/7531649728302514217)
- [Android官方文档 - Nested Scrolling](https://developer.android.com/guide/topics/ui/ui-events#nested-scrolling)
- [Material Design - AppBarLayout](https://material.io/components/app-bars-top) 