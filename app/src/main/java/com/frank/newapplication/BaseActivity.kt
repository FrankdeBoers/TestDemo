package com.frank.newapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    val logTag by lazy {
        this.javaClass.simpleName + "#"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("FrankTest", "$logTag onCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.i("FrankTest", "$logTag onStart")
        initClickEvent()
    }

    override fun onResume() {
        super.onResume()
        Log.i("FrankTest", "$logTag onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i("FrankTest", "$logTag onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i("FrankTest", "$logTag onStop")
    }

    open fun initClickEvent() {}
}