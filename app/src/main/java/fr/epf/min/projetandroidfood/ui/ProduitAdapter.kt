package fr.epf.min.projetandroidfood.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.epf.min.projetandroidfood.R
import fr.epf.min.projetandroidfood.model.EcoscoreGrade
import fr.epf.min.projetandroidfood.model.NutriscoreGrade
import fr.epf.min.projetandroidfood.model.Produit
import kotlinx.android.synthetic.main.produit_view.view.*


class ProduitAdapter(val produits: List<Produit>) :
    RecyclerView.Adapter<ProduitAdapter.ProduitViewHolder>() {

    class ProduitViewHolder(val produitView: View) : RecyclerView.ViewHolder(produitView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProduitViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view =
            inflater.inflate(R.layout.produit_view, parent, false)
        return ProduitViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProduitViewHolder, position: Int) {
        val produit = produits[position]
        holder.produitView.nom_produit_textview.text =
            "${produit.nom}"
        holder.produitView.marque_produit_textview.text = produit.marque

        holder.produitView.produit_nutricscore_imageview.setImageResource(
            when (produit.nutriscoreGrade) {
                NutriscoreGrade.A -> R.drawable.ic_nutriscore_a
                NutriscoreGrade.B -> R.drawable.ic_nutriscore_b
                NutriscoreGrade.C -> R.drawable.ic_nutriscore_c
                NutriscoreGrade.D -> R.drawable.ic_nutriscore_d
                NutriscoreGrade.E -> R.drawable.ic_nutriscore_e
                NutriscoreGrade.UNKNOW -> R.drawable.ic_nutriscore_unknown
            }
        )

        holder.produitView.produit_ecoscore_imageview.setImageResource(
            when (produit.ecoscoreGrade) {
                EcoscoreGrade.A -> R.drawable.ic_ecoscore_a
                EcoscoreGrade.B -> R.drawable.ic_ecoscore_b
                EcoscoreGrade.C -> R.drawable.ic_ecoscore_c
                EcoscoreGrade.D -> R.drawable.ic_ecoscore_d
                EcoscoreGrade.E -> R.drawable.ic_ecoscore_e
                EcoscoreGrade.UNKNOW -> R.drawable.ic_ecoscore_unknown
            }
        )

        Glide.with(holder.produitView)
            .load(produit.image_url)
            .into(holder.produitView.produit_imageview)


    }

    override fun getItemCount() = produits.size


}