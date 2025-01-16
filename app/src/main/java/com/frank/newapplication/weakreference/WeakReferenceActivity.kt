package com.frank.newapplication.weakreference

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.frank.newapplication.BaseActivity
import com.frank.newapplication.databinding.ActivityWeakBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference

class WeakReferenceActivity : BaseActivity() {

    private lateinit var binding: ActivityWeakBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeakBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startGC.setOnClickListener {
            Log.i("FrankTest", "$logTag startGC")

            lifecycleScope.launch {
                val referenceQueue = ReferenceQueue<Apple>()
                val apple1 = WeakReference<Apple>(Apple("青苹果"), referenceQueue)
                val apple2 = WeakReference<Apple>(Apple("红苹果"), referenceQueue)
                Log.i("FrankTest", "$logTag before referenceQueue:${referenceQueue}")
                Log.i("FrankTest", "$logTag before apple1:${apple1}")
                Log.i("FrankTest", "$logTag before apple2:${apple2}")
                Log.i("FrankTest", "$logTag before apple1 get:${apple1.get()}")
                Log.i("FrankTest", "$logTag before apple2 get:${apple2.get()}")
                Log.i("FrankTest", "$logTag gc调用前")
                val reference1 = referenceQueue.poll()
                while (reference1 != null) {
                    Log.i("FrankTest", "$logTag reference before:${reference1}")
                }
                System.gc()
                delay(5000L)
                Log.i("FrankTest", "$logTag gc调用后")
                Log.i("FrankTest", "$logTag apple1 get:${apple1.get()}")
                Log.i("FrankTest", "$logTag apple2 get:${apple2.get()}")
                val reference = referenceQueue.poll()
                while (reference != null) {
                    Log.i("FrankTest", "$logTag reference after:${reference}")
                }
            }

        }
    }

}


