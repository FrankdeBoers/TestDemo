package com.frank.newapplication.startup.tasks

import android.content.Context
import android.util.Log
import com.frank.newapplication.startup.AndroidStartup

class Task3 : AndroidStartup<Unit>() {
    override fun create() {
        println("Startup# Task3 is running, after Task1")
    }
    // Task3 依赖 Task1
    override fun dependencies(): MutableList<Class<out com.frank.newapplication.startup.StartUp<*>>> {
        return mutableListOf(Task1::class.java)
    }

    override fun hashCode(): Int {
        return "task3".hashCode()
    }
}