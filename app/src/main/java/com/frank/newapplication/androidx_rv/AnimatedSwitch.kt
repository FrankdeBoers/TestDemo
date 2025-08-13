package com.frank.newapplication.androidx_rv

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.view.animation.BounceInterpolator
import androidx.appcompat.widget.SwitchCompat

/**
 * 自定义 Switch，当状态切换时执行左右开关动画
 */
class AnimatedSwitch @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.switchStyle
) : SwitchCompat(context, attrs, defStyleAttr) {

    companion object {
        private const val ANIMATION_DURATION = 1000L // 动画时长 1000ms
        private const val SCALE_FACTOR = 0.85f // 缩放因子
        private const val TRANSLATION_FACTOR = 15f // 平移因子
        private const val ROTATION_FACTOR = 5f // 旋转因子
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
     * 执行开关切换动画
     */
    private fun performAnimation(checked: Boolean) {
        isAnimating = true
        Log.d("AnimatedSwitch", "开始动画，目标状态: $checked")

        // 取消之前的动画
        currentAnimator?.cancel()

        // 创建动画集合
        val animatorSet = AnimatorSet()
        currentAnimator = animatorSet

        // 确定滑动和旋转方向
        val translationDirection = if (checked) TRANSLATION_FACTOR else -TRANSLATION_FACTOR
        val rotationDirection = if (checked) ROTATION_FACTOR else -ROTATION_FACTOR

        // 第一阶段：缩小、滑动和轻微旋转
        val scaleDownAnimator = ObjectAnimator.ofFloat(this, "scaleX", 1.0f, SCALE_FACTOR)
        val scaleDownYAnimator = ObjectAnimator.ofFloat(this, "scaleY", 1.0f, SCALE_FACTOR)
        val translateAnimator = ObjectAnimator.ofFloat(this, "translationX", 0f, translationDirection)
        val rotateAnimator = ObjectAnimator.ofFloat(this, "rotation", 0f, rotationDirection)

        // 第二阶段：恢复大小、位置和旋转
        val scaleUpAnimator = ObjectAnimator.ofFloat(this, "scaleX", SCALE_FACTOR, 1.0f)
        val scaleUpYAnimator = ObjectAnimator.ofFloat(this, "scaleY", SCALE_FACTOR, 1.0f)
        val translateBackAnimator = ObjectAnimator.ofFloat(this, "translationX", translationDirection, 0f)
        val rotateBackAnimator = ObjectAnimator.ofFloat(this, "rotation", rotationDirection, 0f)

        // 设置动画时长
        val firstPhaseDuration = ANIMATION_DURATION * 3 / 5
        val secondPhaseDuration = ANIMATION_DURATION * 2 / 5

        scaleDownAnimator.duration = firstPhaseDuration
        scaleDownYAnimator.duration = firstPhaseDuration
        translateAnimator.duration = firstPhaseDuration
        rotateAnimator.duration = firstPhaseDuration
        scaleUpAnimator.duration = secondPhaseDuration
        scaleUpYAnimator.duration = secondPhaseDuration
        translateBackAnimator.duration = secondPhaseDuration
        rotateBackAnimator.duration = secondPhaseDuration

        // 设置插值器
        val firstPhaseInterpolator = AccelerateDecelerateInterpolator()
        val secondPhaseInterpolator = BounceInterpolator()

        scaleDownAnimator.interpolator = firstPhaseInterpolator
        scaleDownYAnimator.interpolator = firstPhaseInterpolator
        translateAnimator.interpolator = firstPhaseInterpolator
        rotateAnimator.interpolator = firstPhaseInterpolator
        scaleUpAnimator.interpolator = secondPhaseInterpolator
        scaleUpYAnimator.interpolator = secondPhaseInterpolator
        translateBackAnimator.interpolator = secondPhaseInterpolator
        rotateBackAnimator.interpolator = secondPhaseInterpolator

        // 组合第一阶段动画
        animatorSet.playTogether(scaleDownAnimator, scaleDownYAnimator, translateAnimator, rotateAnimator)
        animatorSet.addListener(object : android.animation.Animator.AnimatorListener {
            override fun onAnimationStart(animation: android.animation.Animator) {}
            
            override fun onAnimationEnd(animation: android.animation.Animator) {
                // 第一阶段动画结束，设置新的状态
                super@AnimatedSwitch.setChecked(checked)
                
                // 执行第二阶段动画
                val restoreAnimatorSet = AnimatorSet()
                restoreAnimatorSet.playTogether(scaleUpAnimator, scaleUpYAnimator, translateBackAnimator, rotateBackAnimator)
                restoreAnimatorSet.addListener(object : android.animation.Animator.AnimatorListener {
                    override fun onAnimationStart(animation: android.animation.Animator) {}
                    
                    override fun onAnimationEnd(animation: android.animation.Animator) {
                        isAnimating = false
                        currentAnimator = null
                        Log.d("AnimatedSwitch", "动画完成，当前状态: $isChecked")
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
        translationX = 0f
        rotation = 0f
        
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
