package com.frank.newapplication.startup

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

abstract class AndroidStartup<T> : StartUp<T>{
    override fun create(): T {
        TODO("Not yet implemented")
    }


    override fun dependencies(): MutableList<Class<out StartUp<*>>> {
        GlobalScope.launch(Dispatchers.IO) {


        }


        return mutableListOf()
    }

    private suspend fun add() {

    }

    override fun getDependenciesCount(): Int {
        val dependencies = mutableListOf<Class<out StartUp<*>>>()
        return dependencies.size
    }
}