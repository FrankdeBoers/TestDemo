package com.frank.newapplication.startup.tasks

import android.content.Context
import android.util.Log
import com.frank.newapplication.startup.AndroidStartup

class Task5 : AndroidStartup<Unit>() {
    override fun create() {
        println("Startup# Task5 is running, after Task3 and Task4")
    }
    // Task5 依赖 Task3 和 Task4
    override fun dependencies(): MutableList<Class<out com.frank.newapplication.startup.StartUp<*>>> {
        return mutableListOf(Task3::class.java, Task4::class.java)
    }
}