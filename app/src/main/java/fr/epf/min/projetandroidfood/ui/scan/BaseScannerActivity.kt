package fr.epf.min.projetandroidfood.ui.scan

import android.R
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar


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