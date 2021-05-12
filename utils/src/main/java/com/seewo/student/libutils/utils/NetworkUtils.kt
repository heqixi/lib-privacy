package com.seewo.student.libutils.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import java.net.NetworkInterface

object NetworkUtils {
    private const val TAG = "NetworkUtils"

    @SuppressLint("MissingPermission")
    fun isConnected(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo?.isAvailable ?: false
    }

    @JvmStatic
    fun getMac(): String {
        var macSerial = ""
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces() ?: return macSerial
            while (interfaces.hasMoreElements()) {
                val iF = interfaces.nextElement()
                val address = iF.hardwareAddress
                if (address == null || address.isEmpty()) {
                    continue
                }
                val buf = StringBuilder()
                for (b in address) {
                    buf.append(String.format("%02X:", b))
                }
                if (buf.isNotEmpty()) {
                    buf.deleteCharAt(buf.length - 1)
                }
                val mac = buf.toString()
                Log.d(TAG, "interfaceName=" + iF.name + ", mac=" + mac)
                if (iF.name.equals("wlan0", ignoreCase = true)) {
                    macSerial = mac
                    break
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "SocketException e=" + e.message)
            e.printStackTrace()
        }

        return macSerial
    }
}
