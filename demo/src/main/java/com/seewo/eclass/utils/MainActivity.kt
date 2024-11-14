package com.seewo.eclass.utils

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        this.observePowerSaveModeActive(this) {
//            printChange(it)
//        }
//        showAgreementsDialog()
    }

    private fun printChange(isActive: Boolean) {
        Log.v("MainActivity", "printChange: $isActive")
    }
}