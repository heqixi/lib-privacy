package com.seewo.student.libutils.utils

/**
 * Created by linkaipeng on 2020/6/12.
 *
 */
class AnimationHelper {

    companion object {

        /**
         * scrollY 从 0 到 scrollThreshold 的变化 => alpha 从 0 到 1 变化
         */
        fun getScrollAlpha(scrollY: Int, scrollThreshold: Int): Float {
            var alpha = scrollY.toFloat() / scrollThreshold
            if (alpha < 0) {
                alpha = 0f
            } else if (alpha >= 1) {
                alpha = 1f
            }
            return alpha
        }
    }
}