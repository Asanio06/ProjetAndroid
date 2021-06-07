package fr.epf.min.projetandroidfood.ui.fragment.scanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.integration.android.IntentIntegrator
import fr.epf.min.projetandroidfood.R
import fr.epf.min.projetandroidfood.databinding.FragmentScannerBinding
import fr.epf.min.projetandroidfood.ui.scan.ScanActivity
import kotlinx.android.synthetic.main.activity_scan.*
import android.content.Intent
import android.util.Log
import com.google.zxing.integration.android.IntentResult
import kotlinx.android.synthetic.main.activity_scan.btnScanMe
import kotlinx.android.synthetic.main.activity_scan.txtValue
import kotlinx.android.synthetic.main.fragment_scanner.*
import kotlinx.android.synthetic.main.fragment_scanner.view.*


class ScannerFragment : Fragment() {

    var scannedResult: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_scanner, container, false)

        view.btnScanFragment.setOnClickListener { view->
            run {
                IntentIntegrator.forSupportFragment(this).initiateScan();
            }
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        var result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)


        if (result != null) {
            if (result.contents != null) {
                scannedResult = result.contents
                Log.d("epf_good" , "$scannedResult")

                this.requireView().txtValueFragment.text = scannedResult


            } else {
                this.requireView().txtValueFragment.text = "scan failed"

            }

        }
        else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }

}