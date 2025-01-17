package com.frank.newapplication

import android.os.Bundle
import android.util.Log
import com.frank.newapplication.databinding.ActivitySecondBinding

class SecondActivity : BaseActivity() {

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onPause() {
        super.onPause()
        var index = 0L
        for (i in 0 until 999999999) {
            index += i * (i - 1) * 3
        }
        Log.i("FrankTest", "$logTag onPause index:$index")
    }

}

