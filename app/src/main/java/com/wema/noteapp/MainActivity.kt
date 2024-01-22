package com.wema.noteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        snackBarImplementation()
    }

    private fun snackBarImplementation(){

        try {
            val redId = findViewById<LinearLayout>(androidx.appcompat.R.id.content)

            Snackbar.make(this,redId, "Welcome to Class", Snackbar.LENGTH_SHORT).show()
        }catch (e:Exception){
            Log.e(getString(R.string.snackbar_msg), e.localizedMessage?.toString().toString())
        }
    }
}