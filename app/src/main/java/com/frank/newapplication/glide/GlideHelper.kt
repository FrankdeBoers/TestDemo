package com.frank.newapplication.glide

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.chrisbanes.photoview.PhotoView

class GlideHelper {
    private val TAG = "GlideHelper"
    fun loadGif(context: Context, url: String, imageView: PhotoView) {
        Glide.with(context)
            .load(url)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?, model: Any?, target: Target<Drawable?>?, isFirstResource: Boolean,
                ): Boolean {
                    Log.e(TAG, "ImageLoader# load url failed, url: $url")
                    // Gif加载失败的回调
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean,
                ): Boolean {
                    Log.i(TAG, "ImageLoader# load url onResourceReady, url: $url")
                    // Gif加载成功的回调
                    return false
                }
            }).into(imageView) // SeamlessDrawableImageViewTarget 防止闪烁
    }

}