package fr.epf.min.projetandroidfood.ui.fragment.searcher

import android.app.ProgressDialog
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import fr.epf.min.projetandroidfood.ProduitService
import fr.epf.min.projetandroidfood.R
import fr.epf.min.projetandroidfood.data.ProductDataBase
import fr.epf.min.projetandroidfood.model.EcoscoreGrade
import fr.epf.min.projetandroidfood.model.NutrientLevel
import fr.epf.min.projetandroidfood.model.NutriscoreGrade
import fr.epf.min.projetandroidfood.model.Product
import fr.epf.min.projetandroidfood.ui.adapter.ProduitAdapter
import kotlinx.android.synthetic.main.fragment_searcher.*
import kotlinx.android.synthetic.main.fragment_searcher.view.*
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class SearcherFragment : Fragment() {

    lateinit var products: MutableList<Product>
    lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_searcher, container, false)

        progressDialog = ProgressDialog(this.requireContext())
        progressDialog.show()
        progressDialog.setContentView(R.layout.progress_dialog)
        progressDialog.window?.setBackgroundDrawableResource(
            android.R.color.transparent
        )



        products = emptyList<Product>().toMutableList()

        view.products_recyclerview.layoutManager =
            LinearLayoutManager(
                this.context,
                LinearLayoutManager.VERTICAL,
                false
            )

        GlobalScope.launch(Dispatchers.Default) {
            products = getResultOfSearch().toMutableList()

            withContext(Dispatchers.Main) { //switch to main thread

                view.products_recyclerview.adapter = ProduitAdapter(products)
                progressDialog.dismiss()

            }

        }

        view.products_recyclerview.adapter = ProduitAdapter(products)

        setHasOptionsMenu(true)


        return view
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.search_menu, menu)
        val manager = this.activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu?.findItem(R.id.app_bar_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.setSearchableInfo(manager.getSearchableInfo(this.activity?.componentName))
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

        super.onCreateOptionsMenu(menu, inflater)
    }


    suspend fun getResultOfSearch(searchTerms: String = ""): List<Product> {
        var productRecup: List<Product>

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


        productRecup = produitsApi.map {
            Product(
                null,
                it.product_name_fr,
                it.brands,
                it.quantity,
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
                },
                it._id


            )
        }




        return productRecup;

    }

    private fun updateSearch(searchTerms: String = "") {
        progressDialog.show()

        GlobalScope.launch(Dispatchers.Default) {
            val productsSearch = getResultOfSearch(searchTerms);
            if (productsSearch.isNotEmpty()) {
                products.clear()
                productsSearch.map {
                    products.add(it)
                }
            }
            withContext(Dispatchers.Main) { //switch to main thread

                products_recyclerview.adapter?.notifyDataSetChanged()
                progressDialog.dismiss()

            }

        }


    }


    private fun saveProductInHistory(product: Product) {
        val database = Room.databaseBuilder(
            this.requireContext(), ProductDataBase::class.java, "HistoryProductFinal2-db"
        ).build()

        val productDao = database.getProductDao()

        runBlocking {
            productDao.addProduct(product)
        }
    }


}