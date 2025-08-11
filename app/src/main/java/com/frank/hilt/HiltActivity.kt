package com.frank.hilt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.frank.newapplication.databinding.ActivityHiltBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HiltActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHiltBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHiltBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }



}