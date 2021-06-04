package fr.epf.min.projetandroidfood

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
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
            Log.d("EPF", "$service")
            //val result = service.getProduitByCodeBarre("3123349014822")

            val result2 = service.getProduitsBySearch("ch")

            val produitsApi = result2.products;

            produits = produitsApi.map {
                Produit(
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
                    it.nutrient_levels.mapValues { (nom, level) ->
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
        menuInflater.inflate(R.menu.search_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    /*private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.product_list_fragment, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }*/


    private fun synchro() {

        runBlocking {

            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://world.openfoodfacts.org/")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

            val service = retrofit.create(ProduitService::class.java)
            Log.d("EPF", "$service")
            //val result = service.getProduitByCodeBarre("3123349014822")

            val result2 = service.getProduitsBySearch("chocolat")

            val produitsApi = result2.products;

            produitsApi.map {
                Produit(
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
                    it.nutrient_levels.mapValues { (nom, level) ->
                        when (level) {
                            "high" -> NutrientLevel.high
                            "moderate" -> NutrientLevel.moderate
                            "low" -> NutrientLevel.low
                            else -> NutrientLevel.unknow
                        }
                    }


                )
            }.map {
                produits.add(it)
            }


        }

    }
}