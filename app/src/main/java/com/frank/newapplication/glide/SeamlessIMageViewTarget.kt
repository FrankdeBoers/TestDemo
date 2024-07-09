package com.glide

import android.graphics.Bitmap
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
/**
 * 参考https://juejin.cn/post/7034417406244028423
 * 重复加载不闪烁的glideTarget
 * */
abstract class SeamlessImageViewTarget<Z : Any>(private val view: ImageView) : CustomViewTarget<ImageView, Z>(view),
    Transition.ViewAdapter {
    private var animatable: Animatable? = null

    override fun onStart() {
        animatable?.start()
    }

    override fun onStop() {
        animatable?.stop()
    }

    override fun getCurrentDrawable(): Drawable? = view.drawable

    override fun setDrawable(drawable: Drawable?) {
        view.setImageDrawable(drawable)
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        setResourceInternal(null)
        setDrawable(errorDrawable)
    }

    override fun onResourceLoading(placeholder: Drawable?) {
        super.onResourceLoading(placeholder)
        // placeholder 为空则不设置，会显示上一张图
        placeholder?.let { setDrawable(placeholder) }
    }

    override fun onResourceReady(resource: Z, transition: Transition<in Z>?) {
        if (transition == null || !transition.transition(resource, this)) {
            setResourceInternal(resource)
        } else {
            maybeUpdateAnimatable(resource)
        }
    }

    override fun onResourceCleared(placeholder: Drawable?) {
        animatable?.stop()
        setDrawable(placeholder)
    }

    private fun setResourceInternal(resource: Z?) {
        // Order matters here. Set the resource first to make sure that the Drawable has a valid and
        // non-null Callback before starting it.
        setResource(resource)
        maybeUpdateAnimatable(resource)
    }

    private fun maybeUpdateAnimatable(resource: Z?) {
        animatable = if (resource is Animatable) {
            resource.apply { start() }
        } else {
            null
        }
    }

    protected abstract fun setResource(resource: Z?)
}

class SeamlessBitmapImageViewTarget(view: ImageView) : SeamlessImageViewTarget<Bitmap>(view) {
    override fun setResource(resource: Bitmap?) {
        view.setImageBitmap(resource)
    }
}

class SeamlessDrawableImageViewTarget(view: ImageView) : SeamlessImageViewTarget<Drawable>(view) {
    override fun setResource(resource: Drawable?) {
        view.setImageDrawable(resource)
    }
}

