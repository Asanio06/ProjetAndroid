package fr.epf.min.projetandroidfood

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.bumptech.glide.Glide
import fr.epf.min.projetandroidfood.data.ProductDataBase
import fr.epf.min.projetandroidfood.model.Produit
import fr.epf.min.projetandroidfood.ui.NutrientAdapter
import fr.epf.min.projetandroidfood.ui.NutrimentAdapter
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.coroutines.runBlocking


class ProductDetailsActivity : AppCompatActivity() {

    lateinit var product: Produit
    var favoris: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        product = intent.extras?.get("product") as Produit
        Glide.with(this)
            .load(product.image_url)
            .into(product_image_imageview)

        name_product_textview.text = product.nom
        brands_quantity_textview.text = "${product.nom} - ${product.masse}"
        product_nutriscore_imageview.setImageResource(product.getImageFromNutriScoreGrade())
        product_ecoscore_imageview.setImageResource(product.getImageFromEcoscoreGrade())
        ingredients_text_textview.text = product.ingredientsText

        nutrients_recyclerview.layoutManager =
            LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
        nutrients_recyclerview.adapter = product.nutrients?.let { NutrientAdapter(it) }

        nutriments_recyclerview.layoutManager =
            LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )

        nutriments_recyclerview.adapter = product.nutriments?.let { NutrimentAdapter(it) }



    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.favorite_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        favoris = isInFavoriteList(product.codeBarre)

        if (favoris) {
            menu.getItem(0).setIcon(R.drawable.ic_baseline_favorite_24)

        } else {
            menu.getItem(0).setIcon(R.drawable.ic_baseline_favorite_border_24)

        }

        super.onPrepareOptionsMenu(menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            R.id.app_bar_favorite -> {
                if (favoris) {
                    runBlocking {
                        deleteInFavoriteList(product.codeBarre)

                    }

                    item.setIcon(R.drawable.ic_baseline_favorite_border_24);

                } else {
                    runBlocking {
                        saveProductInFavorite(product)
                    }
                    item.setIcon(R.drawable.ic_baseline_favorite_24);
                }

                favoris = !favoris

                Log.d("EPF","$favoris")

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun saveProductInFavorite(produit: Produit) {
        val database = Room.databaseBuilder(
            this, ProductDataBase::class.java, "favoriteProductFinal2-db"
        ).build()

        val productDao = database.getProductDao()

        runBlocking {
            productDao.addProduct(produit)
        }
    }

    private fun isInFavoriteList(barCode: String?): Boolean {
        val database = Room.databaseBuilder(
            this, ProductDataBase::class.java, "favoriteProductFinal2-db"
        ).build()

        val productDao = database.getProductDao()
        var product: List<Produit>

        runBlocking {
            product = barCode?.let { productDao.getProduct(it) }!!
        }

        if (product.isNotEmpty()) {
            return true
        }

        return false


    }

    private fun deleteInFavoriteList(barCode: String?) {
        val database = Room.databaseBuilder(
            this, ProductDataBase::class.java, "favoriteProductFinal2-db"
        ).build()

        val productDao = database.getProductDao()

       runBlocking {
            product.codeBarre?.let { productDao.deleteProduct(it) }
        }

    }
}