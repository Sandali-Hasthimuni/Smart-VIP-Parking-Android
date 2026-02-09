package com.example.smartvipparking.ui.dashboard

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.smartvipparking.databinding.ActivityQrScannerBinding
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class QrScannerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQrScannerBinding

    // Gallery Picker Launcher
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { decodeQrFromUri(it) }
    }

    // Camera Scanner Launcher
    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            handleScanResult(result.contents)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnScanCamera.setOnClickListener {
            val options = ScanOptions()
            options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            options.setPrompt("Scan a Parking QR Code")
            options.setCameraId(0)
            options.setBeepEnabled(true)
            options.setBarcodeImageEnabled(true)
            barcodeLauncher.launch(options)
        }

        binding.btnScanGallery.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.btnBack.setOnClickListener { finish() }
    }

    private fun decodeQrFromUri(uri: Uri) {
        try {
            val inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val width = bitmap.width
            val height = bitmap.height
            val pixels = IntArray(width * height)
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
            
            val source = RGBLuminanceSource(width, height, pixels)
            val binarizer = HybridBinarizer(source)
            val binaryBitmap = BinaryBitmap(binarizer)
            
            val reader = MultiFormatReader()
            val result = reader.decode(binaryBitmap)
            handleScanResult(result.text)
            
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to decode QR from image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleScanResult(qrData: String) {
        // Here you would typically verify the UID in Firebase
        Toast.makeText(this, "Scanned UID: $qrData", Toast.LENGTH_LONG).show()
        
        // Pass result back or navigate to details
        val resultIntent = Intent()
        resultIntent.putExtra("SCAN_RESULT", qrData)
        setResult(RESULT_OK, resultIntent)
        // finish()
    }
}
