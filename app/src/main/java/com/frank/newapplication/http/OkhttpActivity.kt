package com.frank.newapplication.http

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.frank.newapplication.BaseActivity
import com.frank.newapplication.R
import com.frank.newapplication.databinding.ActivityOkhttpBinding
import com.frank.newapplication.fragment.ViewPagerAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class OkhttpActivity : BaseActivity() {
    private lateinit var binding: ActivityOkhttpBinding

    private lateinit var okHttpClient: OkHttpClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOkhttpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        binding.root.postDelayed(
            {adapter.refreshData(listOf("111", "222"))},
            1000L)
    }


    override fun initClickEvent() {
        super.initClickEvent()
        okHttpClient = OkHttpClient.Builder().build()
        binding.startOkhttp.setOnClickListener {
            testGet()
        }
    }

    private fun testGet() {
        val request = Request.Builder().url("https://reqres.in/api/users?page=2").build()
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val response = okHttpClient.newCall(request).execute()
                Log.i("FrankTest", "$logTag 同步请求response#:${response.body?.string()}")
            }

            withContext(Dispatchers.IO) {
                val response = okHttpClient.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                    }

                    override fun onResponse(call: Call, response: Response) {
                        Log.i("FrankTest", "$logTag 异步请求response#:${response.body?.string()}")
                    }
                })
            }
        }
    }
}