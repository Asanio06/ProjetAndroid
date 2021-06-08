package fr.epf.min.projetandroidfood.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.epf.min.projetandroidfood.ProductDetailsActivity
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
        holder.produitView.marque_produit_textview.text = "${produit.marque} - ${produit.masse}"

        holder.produitView.produit_nutricscore_imageview.setImageResource(
           produit.getImageFromNutriScoreGrade()
        )

        holder.produitView.produit_ecoscore_imageview.setImageResource(
            produit.getImageFromEcoscoreGrade()
        )

        Glide.with(holder.produitView)
            .load(produit.image_url)
            .into(holder.produitView.produit_imageview)

        holder.produitView.setOnClickListener{
            val intent = Intent(it.context,ProductDetailsActivity::class.java)
            intent.putExtra("product",produit)
            it.context.startActivity(intent)

        }


    }

    override fun getItemCount() = produits.size


}