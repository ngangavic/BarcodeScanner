package com.example.barcodescanner.kotlin

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Vibrator
import android.view.Menu
import android.view.MenuItem
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.barcodescanner.R
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.io.IOException

class ScannerActivity : AppCompatActivity() {

    lateinit var surfaceView:SurfaceView
    private var barcodeDetector: BarcodeDetector? = null
    private var cameraSource: CameraSource? = null
    private val CAMERA_PERMISSION = 201
    var intentData = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        surfaceView=findViewById(R.id.surfaceView)

        barcodeDetector = BarcodeDetector.Builder(applicationContext)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build()

        cameraSource = CameraSource.Builder(applicationContext, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build()

        initialiseDetectorsAndSources()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.scan_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_done) { //display data
            if (intentData.length > 0) {
                val builder = AlertDialog.Builder(this)
                builder.setCancelable(false)
                        .setTitle("Barcode content")
                        .setMessage(intentData)
                        .setPositiveButton("OK") { dialog, which -> dialog.cancel() }
                        .setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
                val alertDialog = builder.create()
                alertDialog.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        cameraSource!!.release()
    }

    override fun onResume() {
        super.onResume()
        initialiseDetectorsAndSources()
    }

    private fun initialiseDetectorsAndSources() {

        Toast.makeText(applicationContext, "Barcode scanner started", Toast.LENGTH_SHORT).show()

        barcodeDetector = BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build()

        cameraSource = CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build()

        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(
                                    this@ScannerActivity,
                                    Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        cameraSource!!.start(surfaceView.holder)
                    } else {
                        ActivityCompat.requestPermissions(
                                this@ScannerActivity,
                                arrayOf(Manifest.permission.CAMERA),
                                CAMERA_PERMISSION
                        )
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun surfaceChanged(
                    holder: SurfaceHolder,
                    format: Int,
                    width: Int,
                    height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource!!.stop()
            }
        })

        barcodeDetector!!.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
//                Toast.makeText(
//                    applicationContext,
//                    "To prevent memory leaks barcode scanner has been stopped",
//                    Toast.LENGTH_SHORT
//                ).show()
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() != 0) {
                   val intentData = barcodes.valueAt(0).displayValue
                    vibratePhone()

                    barcodeDetector?.release()
                    finish()

                }
            }

        })
    }


    fun vibratePhone() {
        val vibrator =
                applicationContext?.getSystemService(android.content.Context.VIBRATOR_SERVICE) as Vibrator
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(
                    android.os.VibrationEffect.createOneShot(
                            100,
                            android.os.VibrationEffect.DEFAULT_AMPLITUDE
                    )
            )
        } else {
            vibrator.vibrate(100)
        }
    }
}
