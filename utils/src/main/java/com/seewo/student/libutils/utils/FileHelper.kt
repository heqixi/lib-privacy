package com.seewo.student.libutils.utils

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import com.seewo.student.libutils.extension.ensureThePathParentDirExist
import java.io.*

object FileHelper {
    fun readStringFromAssets(context: Context, path: String): String {
        val stringBuilder = StringBuilder()
        val assetManager: AssetManager = context.assets
        try {
            BufferedReader(InputStreamReader(assetManager.open(path), "utf-8")).use { bufferedReader ->
                var line: String?
                while (bufferedReader.readLine().also { line = it } != null) {
                    stringBuilder.append(line)
                }
                bufferedReader.close()
            }
        } catch (e: IOException) {
            Log.e("FileHelper", "catch a exception\n$e")
        }
        return stringBuilder.toString()
    }

    fun readByteArrayFromAssets(context: Context, path: String): ByteArray {
        try {
            val bufferedReader = BufferedInputStream(context.assets.open(path))
            return bufferedReader.readBytes()
        } catch (e: IOException) {
            Log.e("FileHelper", "catch a exception\n$e")
        }
        return byteArrayOf()
    }

    fun copyAssets(context: Context, assetPath: String, localPath: String) {
        try {
            val inputStream = context.assets.open(assetPath)
            localPath.ensureThePathParentDirExist()
            val os: OutputStream = FileOutputStream(localPath)
            var byteCount: Int
            val bytes = ByteArray(1024)
            while (inputStream.read(bytes).also { byteCount = it } != -1) {
                os.write(bytes, 0, byteCount)
            }
            os.close()
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun readByteArrayFromFile(filePath: String): ByteArray {
        try {
            val inputStream = BufferedInputStream(FileInputStream(filePath))
            return inputStream.readBytes()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return byteArrayOf()
    }
}