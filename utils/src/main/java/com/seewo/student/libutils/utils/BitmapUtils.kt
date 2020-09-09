package com.seewo.student.libutils.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import kotlin.math.ceil

/**
 * Created by linkaipeng on 2020/7/21.
 *
 */
class BitmapUtils {

    companion object {

        fun compressBitmap(bitmap: Bitmap, quality: Int = 70): Bitmap? {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos)
            val bytes = baos.toByteArray()
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
            // Calculate inSampleSize
            options.inSampleSize = computeSize(options.outWidth, options.outHeight)
            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
        }

        private fun computeSize(srcWidth: Int, srcHeight: Int): Int {
            var srcWidth = srcWidth
            var srcHeight = srcHeight

            srcWidth = if (srcWidth % 2 == 1) srcWidth + 1 else srcWidth
            srcHeight = if (srcHeight % 2 == 1) srcHeight + 1 else srcHeight
            srcWidth = if (srcWidth > srcHeight) srcHeight else srcWidth
            srcHeight = if (srcWidth > srcHeight) srcWidth else srcHeight

            val scale = srcWidth.toDouble() / srcHeight
            return if (scale <= 1 && scale > 0.5625) {
                if (srcHeight < 1664) {
                    1
                } else if (srcHeight in 1664..4989) {
                    2
                } else if (srcHeight in 4990..10239) {
                    4
                } else {
                    if (srcHeight / 1280 == 0) 1 else srcHeight / 1280
                }
            } else if (scale <= 0.5625 && scale > 0.5) {
                if (srcHeight / 1280 == 0) 1 else srcHeight / 1280
            } else {
                ceil(srcHeight / (1280.0 / scale)).toInt()
            }
        }

        fun saveBitmap(bm: Bitmap?, filePath: String?): Boolean {
            if (bm == null || filePath.isNullOrEmpty()) {
                return false
            }
            return try {
                val bos = BufferedOutputStream(FileOutputStream(filePath))
                bm.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                bos.flush()
                bos.close()
                true
            } catch (e: Exception) {
                false
            }
        }

    }

}