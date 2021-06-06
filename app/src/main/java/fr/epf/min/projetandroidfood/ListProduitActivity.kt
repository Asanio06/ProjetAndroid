package fr.epf.min.projetandroidfood

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import fr.epf.min.projetandroidfood.data.ProductDataBase
import fr.epf.min.projetandroidfood.model.EcoscoreGrade
import fr.epf.min.projetandroidfood.model.NutrientLevel
import fr.epf.min.projetandroidfood.model.NutriscoreGrade
import fr.epf.min.projetandroidfood.model.Produit
import fr.epf.min.projetandroidfood.ui.ProduitAdapter
import kotlinx.android.synthetic.main.activity_list_produit.*
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class ListProduitActivity : AppCompatActivity() {
    lateinit var produits: MutableList<Produit>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_produit)
        runBlocking {

            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://world.openfoodfacts.org/")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

            val service = retrofit.create(ProduitService::class.java)

            val result = service.getProduitsBySearch()

            val produitsApi = result.products;

            produits = produitsApi.map {
                Produit(
                    0,
                    it.product_name_fr,
                    it.brands,
                    "0",
                    it.image_url,
                    when (it.nutriscore_grade) {
                        "a" -> NutriscoreGrade.A
                        "b" -> NutriscoreGrade.B
                        "c" -> NutriscoreGrade.C
                        "d" -> NutriscoreGrade.D
                        "e" -> NutriscoreGrade.E
                        else -> NutriscoreGrade.UNKNOW
                    },
                    when (it.ecoscore_grade) {
                        "a" -> EcoscoreGrade.A
                        "b" -> EcoscoreGrade.B
                        "c" -> EcoscoreGrade.C
                        "d" -> EcoscoreGrade.D
                        "e" -> EcoscoreGrade.E
                        else -> EcoscoreGrade.UNKNOW
                    },
                    it.ingredients_text_fr,
                    it.nutriments,
                    it.nutrient_levels?.mapValues { (nom, level) ->
                        when (level) {
                            "high" -> NutrientLevel.high
                            "moderate" -> NutrientLevel.moderate
                            "low" -> NutrientLevel.low
                            else -> NutrientLevel.unknow
                        }
                    }


                )
            }
                .toMutableList()



        }




        produits_recyclerview.layoutManager =
            LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )


        produits_recyclerview.adapter = ProduitAdapter(produits)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu?.findItem(R.id.app_bar_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.setQuery("$query", false)
                searchItem.collapseActionView()
                searchView.clearFocus()
                if (query != null) {
                    updateSearch(query)
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    /*private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.product_list_fragment, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }*/


    private fun saveProductInFavorite(produit: Produit) {
        val database = Room.databaseBuilder(
            this, ProductDataBase::class.java, "favoriteProduct-db"
        ).build()

        val productDao = database.getProductDao()

        runBlocking {
            productDao.addProduct(produit)
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
    private fun getProductByBarCode(barCode: String) {
        try {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://world.openfoodfacts.org/")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

            val service = retrofit.create(ProduitService::class.java)

            runBlocking {
                val result = service.getProduitByBarCode(barCode)
            }
        }catch (e:Exception){
            val dialogBuilder = AlertDialog.Builder(this)

            // set message of alert dialog
            dialogBuilder.setMessage(e.message)
                // if the dialog is cancelable
                .setCancelable(false)
                // positive button text and action
                .setPositiveButton("Ok", null)
            // negative button text and action


            // create dialog box
            val alert = dialogBuilder.create()

            // show alert dialog
            alert.show()
            alert.setOnShowListener {
                val okButton = alert.getButton(AlertDialog.BUTTON_POSITIVE)
                okButton.setOnClickListener {
                    // dialog won't close by default
                    alert.dismiss()
                }
            }
        }


    }



    private fun getAllFavoriteProduct(): List<Produit> {
        val database = Room.databaseBuilder(
            this, ProductDataBase::class.java, "favoriteProduct-db"
        ).build()

        val productDao = database.getProductDao()
        var favoriteProducts: List<Produit>;
        runBlocking {
            favoriteProducts = productDao.getAllProduct();
        }

        return favoriteProducts

    }


    private fun updateSearch(searchTerms: String) {

        runBlocking {

            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://world.openfoodfacts.org/")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

            val service = retrofit.create(ProduitService::class.java)

            val result2 = service.getProduitsBySearch(searchTerms)

            val produitsApi = result2.products;


            val produitRecup = produitsApi.map {
                Produit(
                    0,
                    it.product_name_fr,
                    it.brands,
                    "0",
                    it.image_url,
                    when (it.nutriscore_grade) {
                        "a" -> NutriscoreGrade.A
                        "b" -> NutriscoreGrade.B
                        "c" -> NutriscoreGrade.C
                        "d" -> NutriscoreGrade.D
                        "e" -> NutriscoreGrade.E
                        else -> NutriscoreGrade.UNKNOW
                    },
                    when (it.ecoscore_grade) {
                        "a" -> EcoscoreGrade.A
                        "b" -> EcoscoreGrade.B
                        "c" -> EcoscoreGrade.C
                        "d" -> EcoscoreGrade.D
                        "e" -> EcoscoreGrade.E
                        else -> EcoscoreGrade.UNKNOW
                    },
                    it.ingredients_text_fr,
                    it.nutriments,
                    it.nutrient_levels?.mapValues { (nom, level) ->
                        when (level) {
                            "high" -> NutrientLevel.high
                            "moderate" -> NutrientLevel.moderate
                            "low" -> NutrientLevel.low
                            else -> NutrientLevel.unknow
                        }
                    }


                )
            }
            if (produitRecup.isNotEmpty()) {
                produits.clear()
                produitRecup.map {
                    produits.add(it)
                }
            }


        }

        produits_recyclerview.adapter?.notifyDataSetChanged()


    }
}