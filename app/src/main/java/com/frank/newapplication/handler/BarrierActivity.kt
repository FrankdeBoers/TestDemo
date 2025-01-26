package com.frank.newapplication.handler

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Handler.Callback
import android.os.Looper
import android.os.Message
import android.util.Log
import com.frank.newapplication.BaseActivity
import com.frank.newapplication.databinding.ActivityBarrierBinding
import kotlin.random.Random
import kotlin.random.nextInt

class BarrierActivity : BaseActivity() {
    private lateinit var binding: ActivityBarrierBinding

    private var index = 0

    private val handler = Handler(Looper.getMainLooper(), object : Callback {
        override fun handleMessage(msg: Message): Boolean {
            when (msg.what) {
                1 -> {
                    kotlin.runCatching {
                        val randomColor = "#" + Random.nextInt(9) + Random.nextInt(9)
                            .toString() + "0020"
//                        binding.rootView.setBackgroundColor(Color.parseColor(randomColor))
                        index ++
                        Log.i("FrankTest", "$logTag startBarrier index:$index, msg:${msg.isAsynchronous}")
                        start()
                    }
                }
            }
            return false
        }
    })

    private fun start() {
        handler.sendEmptyMessageDelayed(1, 200L)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarrierBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initClickEvent() {
        super.initClickEvent()
        binding.startBarrier.setOnClickListener {
            val barrierView = BarrierView(this)
            binding.root.addView(barrierView)
            Log.i("FrankTest", "$logTag startBarrier")
            handler.sendEmptyMessageDelayed(1, 200L)
            barrierView.start()
        }
    }
}