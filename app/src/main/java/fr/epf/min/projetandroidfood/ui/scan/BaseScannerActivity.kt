package fr.epf.min.projetandroidfood.ui.scan

import android.R
import android.content.pm.PackageManager
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_scan.*


open class BaseScannerActivity : AppCompatActivity() {



    fun setupToolbar() {
        val toolbar: Toolbar = findViewById<View>(fr.epf.min.projetandroidfood.R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val ab: ActionBar? = supportActionBar
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}