package fr.epf.min.projetandroidfood.ui.fragment.history

import android.os.Bundle
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
import kotlinx.android.synthetic.main.fragment_history.view.*
import kotlinx.coroutines.runBlocking

class HistoryFragment : Fragment() {

    lateinit var products: MutableList<Produit>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_history, container, false)

        view.productsinhistory_recyclerview.layoutManager =
            LinearLayoutManager(
                this.context,
                LinearLayoutManager.VERTICAL,
                false
            )

        runBlocking {
            products = getAllProductInHistory().toMutableList()
        }
        view.productsinhistory_recyclerview.adapter = ProduitAdapter(products)


        return view
    }




    private fun getAllProductInHistory(): List<Produit> {
        val database = Room.databaseBuilder(
            this.requireContext(), ProductDataBase::class.java, "HistoryProductFinal2-db"
        ).build()

        val productDao = database?.getProductDao()
        var productsInHistory: List<Produit>;
        runBlocking {
            productsInHistory = productDao!!.getAllProduct();
        }

        return productsInHistory

    }



}

