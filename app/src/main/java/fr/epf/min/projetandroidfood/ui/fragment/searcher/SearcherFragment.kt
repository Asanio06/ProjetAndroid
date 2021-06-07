package fr.epf.min.projetandroidfood.ui.fragment.searcher

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import fr.epf.min.projetandroidfood.ProduitService
import fr.epf.min.projetandroidfood.R
import fr.epf.min.projetandroidfood.model.EcoscoreGrade
import fr.epf.min.projetandroidfood.model.NutrientLevel
import fr.epf.min.projetandroidfood.model.NutriscoreGrade
import fr.epf.min.projetandroidfood.model.Produit
import fr.epf.min.projetandroidfood.ui.ProduitAdapter
import kotlinx.android.synthetic.main.fragment_searcher.view.*
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class SearcherFragment : Fragment() {

    lateinit var products: MutableList<Produit>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_searcher, container, false)

        view.products_recyclerview.layoutManager =
            LinearLayoutManager(
                this.context,
                LinearLayoutManager.VERTICAL,
                false
            )

        runBlocking {
            products = getResultOfSearch().toMutableList()
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
                    runBlocking {
                        updateSearch(query)

                    }
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        super.onCreateOptionsMenu(menu, inflater)
    }


    suspend fun getResultOfSearch(searchTerms: String = ""): List<Produit> {
        var produitRecup: List<Produit>
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


            produitRecup = produitsApi.map {
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


        }

        return produitRecup;

    }

    suspend fun updateSearch(searchTerms: String = "") {


        runBlocking {
            val productsSearch = getResultOfSearch(searchTerms);

            if (productsSearch.isNotEmpty()) {
                products.clear()
                productsSearch.map {
                    products.add(it)
                }
            }
        }
        this.requireView().products_recyclerview.adapter?.notifyDataSetChanged()


    }


}