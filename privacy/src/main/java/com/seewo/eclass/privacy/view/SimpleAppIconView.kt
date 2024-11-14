package com.seewo.eclass.privacy.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.seewo.eclass.privacy.R

class SimpleAppIconView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {

    private val cleanPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val layerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val backgroundBitmap by lazy {
        ResourcesCompat.getDrawable(context.resources, R.drawable.privacy_app_bg, null)!!.toBitmap(width, height)
    }
    private val completeBitmap by lazy {
        ResourcesCompat.getDrawable(context.resources, R.drawable.privacy_app_bg_alpha, null)!!.toBitmap(width, height)
    }
    private val dstInFerMode by lazy {
        PorterDuffXfermode(PorterDuff.Mode.DST_IN)
    }
    private val bound by lazy {
        RectF(0.0f, 0.0f, width.toFloat(), height.toFloat())
    }

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, cleanPaint)
    }

    override fun onDraw(canvas: Canvas) {
        drawable?.let {
            canvas.drawColor(Color.WHITE)
            val count = canvas.saveLayer(bound, layerPaint)
            canvas.drawBitmap(backgroundBitmap, 0F, 0F, cleanPaint)
            val scaleBitmap = it.toBitmap(width, width)
            scaleBitmap.density = resources.displayMetrics.densityDpi
            canvas.drawBitmap(scaleBitmap, 0F, 0F, cleanPaint)
            cleanPaint.xfermode = dstInFerMode
            backgroundBitmap.density = resources.displayMetrics.densityDpi
            canvas.drawBitmap(backgroundBitmap, 0F, 0F, cleanPaint)
            completeBitmap.density = resources.displayMetrics.densityDpi
            canvas.drawBitmap(completeBitmap, 0F, 0F, cleanPaint)
            cleanPaint.xfermode = null
            canvas.restoreToCount(count)
        }
    }
}