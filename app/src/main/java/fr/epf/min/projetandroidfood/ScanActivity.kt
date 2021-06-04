package fr.epf.min.projetandroidfood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.android.synthetic.main.activity_scan.*

class ScanActivity : AppCompatActivity() {


    var scannedResult: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        btnScanMe.setOnClickListener {
            run {
                IntentIntegrator(this@ScanActivity).initiateScan();
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        var result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if(result != null){

            if(result.contents != null){
                scannedResult = result.contents
                txtValue.text = scannedResult
            } else {
                txtValue.text = "scan failed"
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {

        outState?.putString("scannedResult", scannedResult)
        if (outState != null) {
            super.onSaveInstanceState(outState)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            super.onRestoreInstanceState(savedInstanceState)
        }

        savedInstanceState?.let {
            scannedResult = it.getString(String.toString(),"scannedResult")
            txtValue.text = scannedResult
        }
    }

}