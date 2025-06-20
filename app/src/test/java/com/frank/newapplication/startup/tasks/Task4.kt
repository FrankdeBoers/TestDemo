package com.frank.newapplication.startup.tasks

import android.content.Context
import android.util.Log
import com.frank.newapplication.startup.AndroidStartup

class Task4 : AndroidStartup<Unit>() {
    override fun create() {
        println("Startup# Task4 is running, after Task2")
    }
    // Task4 依赖 Task2
    override fun dependencies(): MutableList<Class<out com.frank.newapplication.startup.StartUp<*>>> {
        return mutableListOf(Task2::class.java)
    }
}