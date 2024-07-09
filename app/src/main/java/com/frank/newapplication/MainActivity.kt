package com.frank.newapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.frank.newapplication.databinding.ActivityMainBinding
import com.frank.newapplication.glide.GlideHelper
import com.github.chrisbanes.photoview.PhotoView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.glideLoadGif.setOnClickListener {
            GlideHelper().loadGif(this, "/data/data/com.frank.newapplication/files/1720081461864.gif", binding.glideImg)
        }
    }
}