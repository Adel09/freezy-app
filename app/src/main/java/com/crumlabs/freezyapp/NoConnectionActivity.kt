package com.crumlabs.freezyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class NoConnectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_connection)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }


}