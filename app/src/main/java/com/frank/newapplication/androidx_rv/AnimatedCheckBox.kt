package com.frank.newapplication.androidx_rv

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.CheckBox

/**
 * 自定义 CheckBox，当状态发生变化时执行缩放和透明度动画
 */
class AnimatedCheckBox @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.checkboxStyle
) : CheckBox(context, attrs, defStyleAttr) {

    companion object {
        private const val ANIMATION_DURATION = 200L // 动画时长 200ms
        private const val SCALE_FACTOR = 0.85f // 缩放因子
        private const val ALPHA_FACTOR = 0.7f // 透明度因子
    }

    private var isAnimating = false // 防止动画重复执行
    private var currentAnimator: AnimatorSet? = null // 当前执行的动画

    /**
     * 重写 setChecked 方法，添加动画效果
     */
    override fun setChecked(checked: Boolean) {
        if (isChecked == checked) {
            // 状态没有变化，直接调用父类方法
            super.setChecked(checked)
            return
        }

        if (isAnimating) {
            // 如果正在动画中，取消当前动画
            currentAnimator?.cancel()
            isAnimating = false
        }

        // 执行动画
        performAnimation(checked)
    }

    /**
     * 执行缩放和透明度动画
     */
    private fun performAnimation(checked: Boolean) {
        isAnimating = true
        Log.d("AnimatedCheckBox", "开始动画，目标状态: $checked")

        // 取消之前的动画
        currentAnimator?.cancel()

        // 创建动画集合
        val animatorSet = AnimatorSet()
        currentAnimator = animatorSet

        // 前半段动画：缩小和透明度变化
        val scaleDownAnimator = ObjectAnimator.ofFloat(this, "scaleX", 1.0f, SCALE_FACTOR)
        val scaleDownYAnimator = ObjectAnimator.ofFloat(this, "scaleY", 1.0f, SCALE_FACTOR)
        val alphaDownAnimator = ObjectAnimator.ofFloat(this, "alpha", 1.0f, ALPHA_FACTOR)

        // 后半段动画：恢复大小和透明度
        val scaleUpAnimator = ObjectAnimator.ofFloat(this, "scaleX", SCALE_FACTOR, 1.0f)
        val scaleUpYAnimator = ObjectAnimator.ofFloat(this, "scaleY", SCALE_FACTOR, 1.0f)
        val alphaUpAnimator = ObjectAnimator.ofFloat(this, "alpha", ALPHA_FACTOR, 1.0f)

        // 设置动画时长
        val halfDuration = ANIMATION_DURATION / 2
        scaleDownAnimator.duration = halfDuration
        scaleDownYAnimator.duration = halfDuration
        alphaDownAnimator.duration = halfDuration
        scaleUpAnimator.duration = halfDuration
        scaleUpYAnimator.duration = halfDuration
        alphaUpAnimator.duration = halfDuration

        // 设置插值器
        val interpolator = AccelerateDecelerateInterpolator()
        scaleDownAnimator.interpolator = interpolator
        scaleDownYAnimator.interpolator = interpolator
        alphaDownAnimator.interpolator = interpolator
        scaleUpAnimator.interpolator = interpolator
        scaleUpYAnimator.interpolator = interpolator
        alphaUpAnimator.interpolator = interpolator

        // 组合动画
        animatorSet.playTogether(scaleDownAnimator, scaleDownYAnimator, alphaDownAnimator)
        animatorSet.addListener(object : android.animation.Animator.AnimatorListener {
            override fun onAnimationStart(animation: android.animation.Animator) {}
            
            override fun onAnimationEnd(animation: android.animation.Animator) {
                // 前半段动画结束，设置新的状态
                super@AnimatedCheckBox.setChecked(checked)
                
                // 执行后半段动画
                val restoreAnimatorSet = AnimatorSet()
                restoreAnimatorSet.playTogether(scaleUpAnimator, scaleUpYAnimator, alphaUpAnimator)
                restoreAnimatorSet.addListener(object : android.animation.Animator.AnimatorListener {
                    override fun onAnimationStart(animation: android.animation.Animator) {}
                    
                    override fun onAnimationEnd(animation: android.animation.Animator) {
                        isAnimating = false
                        currentAnimator = null
                        Log.d("AnimatedCheckBox", "动画完成，当前状态: $isChecked")
                    }
                    
                    override fun onAnimationCancel(animation: android.animation.Animator) {
                        isAnimating = false
                        currentAnimator = null
                    }
                    
                    override fun onAnimationRepeat(animation: android.animation.Animator) {}
                })
                restoreAnimatorSet.start()
            }
            
            override fun onAnimationCancel(animation: android.animation.Animator) {
                isAnimating = false
                currentAnimator = null
            }
            
            override fun onAnimationRepeat(animation: android.animation.Animator) {}
        })

        // 开始动画
        animatorSet.start()
    }

    /**
     * 强制设置状态（不执行动画）
     */
    fun setCheckedWithoutAnimation(checked: Boolean) {
        // 取消当前动画
        currentAnimator?.cancel()
        isAnimating = false
        currentAnimator = null
        
        // 重置动画属性
        scaleX = 1.0f
        scaleY = 1.0f
        alpha = 1.0f
        
        super.setChecked(checked)
    }

    /**
     * 在 View 销毁时清理动画
     */
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        currentAnimator?.cancel()
        currentAnimator = null
        isAnimating = false
    }
}
