package com.seewo.student.libutils.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.Log
import androidx.core.content.ContextCompat
import com.seewo.student.libutils.utils.BitmapUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.roundToInt

// 图片缩放比例
private const val BITMAP_SCALE = 0.3f

// 最大模糊度(在0.0到25.0之间)
private const val BLUR_RADIUS = 20f

suspend fun Bitmap.saveToFile(
    filePath: String,
    quality: Int = 100,
    compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
) = withContext(Dispatchers.IO) {
    val imageFile = File(filePath)
    val parentDir = imageFile.parentFile
    if (!parentDir.exists()) {
        parentDir.mkdir()
    }

    var fos: FileOutputStream? = null
    try {
        fos = FileOutputStream(imageFile)
        compress(compressFormat, quality, fos)
        fos.close()
    } catch (e: IOException) {
        Log.e("saveToFile", e.message)
        if (fos != null) {
            try {
                fos.close()
            } catch (e1: IOException) {
                e1.printStackTrace()
            }
        }
    }
}

suspend fun Bitmap.compress(quality: Int = 80) = withContext(Dispatchers.IO) {
    BitmapUtils.compressBitmap(this@compress, quality)
}

suspend fun Bitmap.toByteArray(): ByteArray = withContext(Dispatchers.IO) {
    val stream = ByteArrayOutputStream()
    this@toByteArray.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    stream.toByteArray()
}

suspend fun Bitmap.blur(context: Context): Bitmap = withContext(Dispatchers.IO) {
    val translatedBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && this@blur.config == Bitmap.Config.HARDWARE) {
        this@blur.copy(Bitmap.Config.ARGB_8888, false)
    } else {
        this@blur
    }
    // 计算图片缩小后的长宽
    val width = (translatedBitmap.width * BITMAP_SCALE).roundToInt()
    val height = (translatedBitmap.height * BITMAP_SCALE).roundToInt()

    // 将缩小后的图片做为预渲染的图片。
    val inputBitmap: Bitmap = Bitmap.createScaledBitmap(translatedBitmap, width, height, false)
    // 创建一张渲染后的输出图片。
    val outputBitmap: Bitmap = Bitmap.createBitmap(inputBitmap)

    // 创建RenderScript内核对象
    val rs: RenderScript = RenderScript.create(context)
    // 创建一个模糊效果的RenderScript的工具对象
    val blurScript: ScriptIntrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))

    // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间。
    // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去。
    val tmpIn: Allocation = Allocation.createFromBitmap(rs, inputBitmap)
    val tmpOut: Allocation = Allocation.createFromBitmap(rs, outputBitmap)

    // 设置渲染的模糊程度, 25f是最大模糊度
    blurScript.setRadius(BLUR_RADIUS)
    // 设置blurScript对象的输入内存
    blurScript.setInput(tmpIn)
    // 将输出数据保存到输出内存中
    blurScript.forEach(tmpOut)

    // 将数据填充到Allocation中
    tmpOut.copyTo(outputBitmap)
    outputBitmap
}

fun getBitmapFromVectorDrawable(context: Context, drawableId: Int, width: Int, height: Int): Bitmap {
    val drawable = ContextCompat.getDrawable(context, drawableId)
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable?.setBounds(0, 0, canvas.width, canvas.height)
    drawable?.draw(canvas)
    return bitmap
}