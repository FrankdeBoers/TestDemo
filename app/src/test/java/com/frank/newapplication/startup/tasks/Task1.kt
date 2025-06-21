package com.frank.newapplication.startup.tasks

import android.content.Context
import android.util.Log
import com.frank.newapplication.startup.AndroidStartup

class Task1 : AndroidStartup<Unit>() {
    override fun create() {
        println("Startup# Task1 is running")
    }
    // Task1 没有依赖
    override fun dependencies(): MutableList<Class<out com.frank.newapplication.startup.StartUp<*>>> {
        return mutableListOf()
    }

    override fun hashCode(): Int {
        return "task1".hashCode()
    }
}