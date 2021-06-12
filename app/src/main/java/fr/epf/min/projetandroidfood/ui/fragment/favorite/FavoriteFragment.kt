package fr.epf.min.projetandroidfood.ui.fragment.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import fr.epf.min.projetandroidfood.R
import fr.epf.min.projetandroidfood.data.ProductDataBase
import fr.epf.min.projetandroidfood.model.Produit
import fr.epf.min.projetandroidfood.ui.adapter.ProduitAdapter
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlinx.android.synthetic.main.fragment_favorite.view.*
import kotlinx.coroutines.*

class FavoriteFragment : Fragment() {
    lateinit var products: MutableList<Produit>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_favorite, container, false)

        view.favoriteproducts_recyclerview.layoutManager =
            LinearLayoutManager(
                this.context,
                LinearLayoutManager.VERTICAL,
                false
            )
        products = emptyList<Produit>().toMutableList()


        GlobalScope.launch(Dispatchers.Default) {
            products = getAllFavoriteProduct().toMutableList()

            withContext(Dispatchers.Main) {
                view.favoriteproducts_recyclerview.adapter = ProduitAdapter(products)

            }
        }

        view.favoriteproducts_recyclerview.adapter = ProduitAdapter(products)





        return view
    }


    override fun onResume() {
        super.onResume()
      products.clear()

        favoriteproducts_recyclerview.adapter?.notifyDataSetChanged()

        GlobalScope.launch(Dispatchers.Default) {

            val favoriteProducts = getAllFavoriteProduct()
            if (favoriteProducts.isNotEmpty()) {
                products.clear()
                favoriteProducts.map {
                    products.add(it)
                }

            }
            withContext(Dispatchers.Main) { //switch to main thread

                favoriteproducts_recyclerview.adapter?.notifyDataSetChanged()
                Log.d("EPF","ON RESUME")

            }

        }



    }

    private fun getAllFavoriteProduct(): List<Produit> {
        val database = Room.databaseBuilder(
            this.requireContext(), ProductDataBase::class.java, "favoriteProductFinal2-db"
        ).build()

        val productDao = database.getProductDao()
        var favoriteProducts: List<Produit>;
        runBlocking {
            favoriteProducts = productDao.getAllProduct();
        }

        return favoriteProducts

    }


}