package com.frank.newapplication.startup

import android.content.Context

abstract class AndroidStartup<T> : StartUp<T>{
    override fun create(): T {
        TODO("Not yet implemented")
    }

    override fun dependencies(): MutableList<Class<out StartUp<*>>> {
        return mutableListOf()
    }

    override fun getDependenciesCount(): Int {
        val dependencies = mutableListOf<Class<out StartUp<*>>>()
        return dependencies.size
    }
}