package fr.epf.min.projetandroidfood.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.epf.min.projetandroidfood.R
import kotlinx.android.synthetic.main.nutrient_view.view.*
import kotlinx.android.synthetic.main.nutriment_view.view.*

class NutrimentAdapter(val nutriments: Map<String, String>) :
    RecyclerView.Adapter<NutrimentAdapter.NutrimentViewHolder>() {

    class NutrimentViewHolder(val nutrimentView: View) : RecyclerView.ViewHolder(nutrimentView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NutrimentViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view =
            inflater.inflate(R.layout.nutriment_view, parent, false)
        return NutrimentViewHolder(view)
    }

    override fun onBindViewHolder(holder: NutrimentViewHolder, position: Int) {
        val keyOfPosition = nutriments.keys.toList()[position]
        val nutriment = nutriments[keyOfPosition]
        holder.nutrimentView.nutriment_name_textview.text = keyOfPosition
        holder.nutrimentView.nutriment_quantity_textview.text = nutriment.toString()
    }

    override fun getItemCount() = nutriments.size

}
