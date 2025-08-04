package com.frank.newapplication

import android.app.Application

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startGCMonitoring()
    }

    private fun startGCMonitoring() {
        val GC_DURATION = 10
        var lastTime = System.currentTimeMillis()
        Runtime.getRuntime().addShutdownHook(object: Thread() {
            override fun run() {
                super.run()
                val duration = System.currentTimeMillis() - lastTime;
                if (duration > GC_DURATION) {
                    println("GC took " + duration + "ms, which is longer than threshold");
                }
            }
        })
    }
}