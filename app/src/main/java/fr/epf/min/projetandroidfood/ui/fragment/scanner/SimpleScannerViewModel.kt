package fr.epf.min.projetandroidfood.ui.fragment.scanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.epf.min.projetandroidfood.ui.scan.SimpleScannerActivity


class SimpleScannerViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is scan Fragment"
    }


    var handled: Boolean = false
        private set

    open fun handle(activity: SimpleScannerActivity) {
        handled = true
    }

    val text: LiveData<String> = _text
}