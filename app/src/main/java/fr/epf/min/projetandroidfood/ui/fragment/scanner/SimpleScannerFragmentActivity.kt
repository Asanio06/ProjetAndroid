package fr.epf.min.projetandroidfood.ui.fragment.scanner

import android.R
import android.os.Bundle
import fr.epf.min.projetandroidfood.ui.scan.BaseScannerActivity


class SimpleScannerFragmentActivity : BaseScannerActivity() {
    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        setContentView(fr.epf.min.projetandroidfood.R.layout.activity_simple_scanner_fragment)
        setupToolbar()
    }
}