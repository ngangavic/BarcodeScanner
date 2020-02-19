package com.example.barcodescanner.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.barcodescanner.R

class ScannerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)
    }
}
