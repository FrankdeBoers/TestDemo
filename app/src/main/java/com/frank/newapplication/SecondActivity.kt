package com.frank.newapplication

import android.os.Bundle
import com.frank.newapplication.databinding.ActivitySecondBinding

class SecondActivity : BaseActivity() {

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}

