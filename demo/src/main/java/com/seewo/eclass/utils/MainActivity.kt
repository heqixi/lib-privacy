package com.seewo.eclass.utils

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.seewo.student.libutils.extension.observePowerSaveModeActive

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.observePowerSaveModeActive(this) {
            printChange(it)
        }
    }

    private fun printChange(isActive: Boolean) {
        Log.v("MainActivity", "printChange: $isActive")
    }
}