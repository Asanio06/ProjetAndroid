package fr.epf.min.projetandroidfood.ui.fragment.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import fr.epf.min.projetandroidfood.R
import fr.epf.min.projetandroidfood.data.ProductDataBase
import fr.epf.min.projetandroidfood.databinding.FragmentFavoriteBinding
import fr.epf.min.projetandroidfood.model.Produit
import fr.epf.min.projetandroidfood.ui.ProduitAdapter
import kotlinx.android.synthetic.main.fragment_favorite.view.*
import kotlinx.android.synthetic.main.fragment_history.view.*
import kotlinx.android.synthetic.main.fragment_history.view.productsinhistory_recyclerview
import kotlinx.coroutines.runBlocking

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

        runBlocking {
            products = getAllFavoriteProduct().toMutableList()
        }
        view.favoriteproducts_recyclerview.adapter = ProduitAdapter(products)


        return view
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