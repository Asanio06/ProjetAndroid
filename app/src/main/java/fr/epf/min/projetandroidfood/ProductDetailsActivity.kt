package fr.epf.min.projetandroidfood

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.app_bar_favorite -> {
                runBlocking {
                    saveProductInFavorite(product)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveProductInHistory(produit: Produit) {
        val database = Room.databaseBuilder(
            this, ProductDataBase::class.java, "HistoryProduct-db"
        ).build()

        val productDao = database.getProductDao()

        runBlocking {
            productDao.addProduct(produit)
        }
    }

    private fun saveProductInFavorite(produit: Produit) {
        val database = Room.databaseBuilder(
            this, ProductDataBase::class.java, "favoriteProduct-db"
        ).build()

        val productDao = database.getProductDao()

        runBlocking {
            productDao.addProduct(produit)
        }
    }
}