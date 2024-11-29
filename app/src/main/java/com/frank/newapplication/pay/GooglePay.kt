package com.frank.newapplication.pay

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.frank.newapplication.databinding.ActivityMainBinding

class GooglePay : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}