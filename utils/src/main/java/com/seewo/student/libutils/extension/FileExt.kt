package com.seewo.student.libutils.extension

import java.io.File

fun File.ensureParentDirExist() {
    val parentDir = this.parentFile
    if (parentDir?.exists() == false) {
        parentDir.mkdir()
    }
}

fun File.ensureDirExist() {
    if (!this.exists()) {
        this.mkdir()
    }
}

fun String.ensureThePathParentDirExist() {
    File(this).ensureParentDirExist()
}

fun String.ensureThePathDirExist() {
    File(this).ensureDirExist()
}