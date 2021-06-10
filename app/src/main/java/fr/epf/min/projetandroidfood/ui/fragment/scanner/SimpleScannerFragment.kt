package fr.epf.min.projetandroidfood.ui.fragment.scanner

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView


class SimpleScannerFragment : Fragment(), ZXingScannerView.ResultHandler {
    private var mScannerView: ZXingScannerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mScannerView = ZXingScannerView(getActivity())
        return mScannerView as ZXingScannerView
    }

    override fun onResume() {
        super.onResume()
        mScannerView!!.setResultHandler(this)
        mScannerView!!.startCamera()
    }

    override fun handleResult(rawResult: Result) {
        Toast.makeText(
            getActivity(), "Contents = " + rawResult.text +
                    ", Format = " + rawResult.barcodeFormat.toString(), Toast.LENGTH_SHORT
        ).show()
        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        val handler = Handler()
        handler.postDelayed(
            { mScannerView!!.resumeCameraPreview(this@SimpleScannerFragment) },
            2000
        )
    }

    override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera()
    }
}