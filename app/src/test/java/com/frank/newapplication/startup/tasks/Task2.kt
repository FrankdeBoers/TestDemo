package com.frank.newapplication.startup.tasks

import android.content.Context
import android.util.Log
import com.frank.newapplication.startup.AndroidStartup
import com.frank.newapplication.startup.StartUp

class Task2 : AndroidStartup<Unit>() {

    override fun create() {
        println("Startup# Task2 is running, after Task1")
    }

    override fun dependencies(): MutableList<Class<out StartUp<*>>> {
        return mutableListOf(Task1::class.java)
    }
}