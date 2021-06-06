package fr.epf.min.projetandroidfood

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import fr.epf.min.projetandroidfood.model.Produit
import fr.epf.min.projetandroidfood.ui.ProduitAdapter
import kotlinx.android.synthetic.main.activity_list_produit.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val PRODUCTS = "products"

/**
 * A simple [Fragment] subclass.
 * Use the [ProductListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var products: ArrayList<Produit>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

        products = ArrayList(Produit.all())
        if(products!=null){
            produits_recyclerview.adapter = ProduitAdapter(products!!.toList())

        }else{
            Log.d("EPF","Ca foncttionne pas")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_list, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param products Parameter 1.
         * @return A new instance of fragment ProductListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(products: List<Produit>) =
            ProductListFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}