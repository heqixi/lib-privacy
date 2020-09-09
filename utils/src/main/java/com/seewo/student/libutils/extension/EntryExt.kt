package com.seewo.student.libutils.extension

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.regex.Pattern

private val pattern = Pattern.compile("^[0-9A-Za-z]\$")
private const val PHONE_NUMBER_LENGTH = 11

fun md5(source: String): String {
    try {
        val instance: MessageDigest = MessageDigest.getInstance("MD5")
        val digest: ByteArray = instance.digest(source.toByteArray())
        val stringBuffer = StringBuffer()
        for (b in digest) {
            var hexString = Integer.toHexString(b.toInt() and 0xff)
            if (hexString.length < 2) {
                hexString = "0$hexString"
            }
            stringBuffer.append(hexString)
        }
        return stringBuffer.toString()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }
    return ""
}

fun String.isLegalPassword(): Boolean {
    if (this.length in 6..16) {
        val notFind = this.toCharArray().any {
            !pattern.matcher(it.toString()).find()
        }
        return !notFind
    }
    return false
}

fun String.removeSpace() = replace(" ", "")

fun String.hiddenPhoneNumber(): String {
    if (this.length < PHONE_NUMBER_LENGTH) return this
    return StringBuilder().apply {
        append(this@hiddenPhoneNumber.substring(0, 3))
        append("****")
        append(this@hiddenPhoneNumber.substring(7))
    }.toString()
}