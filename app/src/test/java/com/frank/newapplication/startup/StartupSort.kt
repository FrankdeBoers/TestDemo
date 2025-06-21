package com.frank.newapplication.startup

import java.util.Deque
import java.util.LinkedList
import java.util.Queue

object StartupSort {
    // 拓扑排序
    fun sort(inputTasks: List<StartUp<*>>): List<StartUp<*>> {
        // 记录结果
        val resultList = mutableListOf<StartUp<*>>()

        val stack: Deque<Int> = LinkedList()
        stack.push(1)
        stack.pop()

        val queue: Queue<Int> = LinkedList()
        queue.offer(1)
        queue.poll()
        queue.peek()


        // 记录每个任务的入度，会实时递减
        val inDegreeMap = hashMapOf<Class<out StartUp<*>>, Int>()

        // 邻接表，前向的，记录key被哪些value依赖了
        // task1 -> [task2, task3]
        // task3 -> [task5]
        // task4 -> [task5]
        val forwardDependMap = hashMapOf<Class<out StartUp<*>>, MutableList<Class<out StartUp<*>>>>()

        // 初始数据，转成map，用于提升查找效率
        val inputMap = inputTasks.associateBy { it.javaClass }

        // 1. 初始化入度表和前向链接表
        for (task in inputTasks) {
            val taskClass = task.javaClass
            // 初始化入度表
            inDegreeMap[taskClass] = task.dependencies().size
            // 如果任务没有依赖，入度为0，则前向邻接表是空数组
            if (task.dependencies().isEmpty()) {
                if (forwardDependMap[taskClass] == null) {
                    forwardDependMap[taskClass] = mutableListOf()
                }
            } else {
                // 遍历当前任务的依赖，构建邻接表
                for (dependency in task.dependencies()) {
                    // 计算前向邻接表
                    var dependList = forwardDependMap[dependency]
                    if (dependList == null) {
                        dependList = mutableListOf()
                        forwardDependMap[dependency] = dependList
                    }
                    dependList.add(taskClass)
                }
            }

            forwardDependMap.forEach { key, value ->
                println("Frank# Task# key:$key, value:$value")
            }


            // 2. 将所有入度为0的任务加入队列
            val zeroQueue: Queue<Class<out StartUp<*>>> = LinkedList()
            inDegreeMap.filter { it.value <= 0 }.keys.forEach {
                println("Frank# Task# inDegreeMap it:$it")
                zeroQueue.offer(it)
            }

            // 3.算法核心逻辑
            while (zeroQueue.isNotEmpty()) {
                // 取出当前task
                val currentTask = zeroQueue.poll()
                println("Frank# Task# zeroQueue currentTask:$currentTask")
                inputMap[currentTask]?.let {
                    resultList.add(it)
                }

                // 当前任务已经完成，被依赖的任务入度-1
                // 遍历当前任务后面的所有后续任务
                forwardDependMap[currentTask!!]?.forEach {
                    println("Frank# Task# forwardDependMap it:$it")
                    inDegreeMap[it] = (inDegreeMap[it] ?: 0) - 1
                    // 判断是否入表
                    if ((inDegreeMap[it] ?: 0) <= 0) {
                        zeroQueue.offer(it)
                    }
                }
            }
        }
        return resultList
    }

    /**
     * 执行所有任务
     * @param context Context
     * @param startupList 所有任务
     */
    fun run(startupList: List<StartUp<*>>) {
        val sortedTasks = StartupSort.sort2(startupList)
        println("Startup# Sorted order:")
        sortedTasks.forEach { task ->
            println("Startup# -> ${task.javaClass.simpleName}")
            task.create()
        }
        println("Startup# All tasks finished.")
    }

    fun sort2(inputList: List<StartUp<*>>): List<StartUp<*>> {
        val resultList = mutableListOf<StartUp<*>>()
        // 入度map
        val inDegreeMap = hashMapOf<Class<out StartUp<*>>, Int>()
        // 前向依赖map 1指向了2、3；1消费后，2、3任务的入度需要减一
        // 1:[2,3]     3:[5]    4:[5]
        val forwardDependMap = hashMapOf<Class<out StartUp<*>>, MutableList<Class<out StartUp<*>>>>()
        // 查找提速
        val inputMap = inputList.associateBy { it.javaClass }

        // 入度为0的表
        val zeroQueue: Queue<Class<out StartUp<*>>> = LinkedList()

        // 1. 首先构建前向依赖map
        for (task in inputList) {
            inDegreeMap[task.javaClass] = task.dependenciesCount
            if (task.dependenciesCount <= 0) {
                if (forwardDependMap[task.javaClass] == null) {
                    forwardDependMap[task.javaClass] = mutableListOf()
                }
            } else {
                // 如果不为零，说明对其他任务有依赖，就要构建依赖map
                for (dependency in task.dependencies()) {
                    // 取出依赖，task = task5，dependency = [3,4] 会循环两次
                    var newList = forwardDependMap[dependency]
                    if (newList.isNullOrEmpty()) {
                        newList = mutableListOf<Class<out StartUp<*>>>()
                        forwardDependMap[dependency] = newList
                    }
                    // 3 指向了5， 当3移除时，5的入度减一
                    newList.add(task.javaClass)
                }
            }
        }

        // 2. 入度为0的入表
        inDegreeMap.filterValues { it <= 0 }.keys.forEach {
            zeroQueue.offer(it)
        }

        // 3. BFS
        while (zeroQueue.isNotEmpty()) {
            val currentTaskClass = zeroQueue.poll()
            // 入度为0，添加到结果列表
            if ((inDegreeMap[currentTaskClass] ?: 0) <= 0) {
                inputMap[currentTaskClass]?.let {
                    resultList.add(it)
                }
            }

            // 查找后续的task
            // 查找被影响的task
            forwardDependMap[currentTaskClass]?.forEach { nextTaskClass ->
                inDegreeMap[nextTaskClass] = (inDegreeMap[nextTaskClass] ?: 0) - 1
                if ((inDegreeMap[nextTaskClass] ?: 0) <= 0) {
                    zeroQueue.offer(nextTaskClass)
                }
            }
        }
        return resultList
    }
}