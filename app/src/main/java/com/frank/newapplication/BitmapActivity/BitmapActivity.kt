package com.frank.newapplication.BitmapActivity

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.frank.newapplication.BaseActivity
import com.frank.newapplication.R
import com.frank.newapplication.databinding.ActivityBitmapBinding

class BitmapActivity : BaseActivity() {

    private lateinit var binding: ActivityBitmapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBitmapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initClick()
    }

    private fun initClick() {
        binding.startH.setOnClickListener {
            val target = object : CustomTarget<Bitmap?>() {
                override fun onLoadStarted(placeholder: Drawable?) {
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {

                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(
                    resource: Bitmap, transition: Transition<in Bitmap?>?,
                ) {
                    Log.i("FrankTest", "$logTag onResourceReady# startH resource:${resource.byteCount}")
                }
            }
            Glide.with(this).asBitmap().load(R.mipmap.ic_launcher).into(target)
        }

        binding.startxxhdpi.setOnClickListener {
            val target = object : CustomTarget<Bitmap?>() {
                override fun onLoadStarted(placeholder: Drawable?) {
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {

                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(
                    resource: Bitmap, transition: Transition<in Bitmap?>?,
                ) {
                    Log.i("FrankTest", "$logTag onResourceReady# startxxhdpi resource:${resource.byteCount}")
                }
            }
            Glide.with(this).asBitmap().load(R.mipmap.ic_launcher111).into(target)
        }
    }
}