package com.frank.newapplication.startup

import java.util.*
import kotlin.collections.HashMap

object StartupManager {

    /**
     * 对启动任务进行拓扑排序
     * @param startupList 所有需要执行的启动任务列表
     * @return 排序后的任务列表，保证了依赖关系
     */
    fun sort(startupList: List<StartUp<*>>): List<StartUp<*>> {
        val sortedList = mutableListOf<StartUp<*>>()
        // 记录的是每个任务的入度，会实时变化
        val inDegreeMap = HashMap<Class<out StartUp<*>>, Int>()
        // 记录的是任务依赖表，记录的是任务1被谁依赖了。任务1：[任务2，任务3]   任务3：[任务5]  Task4 -> [Task5]
        // 当任务1被移除后，查找任务1对应的列表，响应的任务入度-1
        val taskDependTasks = HashMap<Class<out StartUp<*>>, MutableList<Class<out StartUp<*>>>>()
        // 初始数据，记录的是所有的任务。里面有你依赖了谁
        val startupMap = startupList.associateBy { it.javaClass }

        // 1. 初始化入度和邻接表
        for (startup in startupList) {
            val startupClass = startup.javaClass
            inDegreeMap[startupClass] = startup.dependencies().size
            // 如果任务没有依赖，则直接加入邻接表
            if (startup.dependencies().isEmpty()) {
                // 复杂写法：先检查key是否存在，不存在则创建并放入
                if (taskDependTasks[startupClass] == null) {
                    taskDependTasks[startupClass] = mutableListOf()
                }
            } else {
                // 遍历当前任务的依赖，构建邻接表
                for (dependency in startup.dependencies()) {
                    // 复杂写法：
                    // 1. 从Map中获取List
                    var list = taskDependTasks[dependency]
                    // 2. 检查List是否存在
                    if (list == null) {
                        // 3. 如果不存在，创建一个新的List
                        list = mutableListOf()
                        // 4. 将新的List放回Map
                        taskDependTasks[dependency] = list
                    }
                    // 5. 向List中添加元素
                    list.add(startupClass)
                }
            }
        }

        // 2. 将所有入度为0的任务加入队列
        val zeroQueue: Queue<Class<out StartUp<*>>> = LinkedList()
        inDegreeMap.filterValues { it == 0 }.keys.forEach {
            zeroQueue.add(it)
        }

        // 3. Kahn算法核心逻辑
        while (zeroQueue.isNotEmpty()) {
            val current = zeroQueue.poll()
            startupMap[current]?.let { sortedList.add(it) }

            // 遍历当前任务的所有后续任务
            taskDependTasks[current]?.forEach { neighbor ->
                // 后续任务的入度减1
                inDegreeMap[neighbor] = (inDegreeMap[neighbor] ?: 0) - 1
                // 如果后续任务的入度变为0，则加入队列
                if (inDegreeMap[neighbor] == 0) {
                    zeroQueue.add(neighbor)
                }
            }
        }

        // 4. 检查是否有循环依赖
        if (sortedList.size != startupList.size) {
            val cyclicDependencies = startupList.filter { !sortedList.contains(it) }
            val cycleDetails = cyclicDependencies.joinToString { it.javaClass.simpleName }
            throw IllegalStateException("Startup tasks have a cycle dependency: [$cycleDetails]")
        }

        return sortedList
    }

    /**
     * 执行所有任务
     * @param context Context
     * @param startupList 所有任务
     */
    fun run(startupList: List<StartUp<*>>) {
        val sortedTasks = sort(startupList)
        println("Startup# Sorted order:")
        sortedTasks.forEach { task ->
            println("Startup# -> ${task.javaClass.simpleName}")
            task.create()
        }
        println("Startup# All tasks finished.")
    }


    fun sort2(inputTasks: List<StartUp<*>>) {

    }
} 