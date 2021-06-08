package fr.epf.min.projetandroidfood.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.epf.min.projetandroidfood.R
import fr.epf.min.projetandroidfood.model.NutrientLevel
import kotlinx.android.synthetic.main.nutrient_view.view.*

class NutrientAdapter(val nutrients: Map<String, NutrientLevel>) :
    RecyclerView.Adapter<NutrientAdapter.NutrientViewHolder>() {

        class NutrientViewHolder(val nutrientView: View): RecyclerView.ViewHolder(nutrientView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):NutrientViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view =
            inflater.inflate(R.layout.nutrient_view, parent, false)
        return NutrientViewHolder(view)
    }


    override fun onBindViewHolder(holder: NutrientViewHolder, position: Int) {
        val keyOfPosition = nutrients.keys.toList()[position]
        val nutrient = nutrients[keyOfPosition]
        holder.nutrientView.nutrient_name_textview.text = keyOfPosition
        holder.nutrientView.nutrient_quantity_textview.text = nutrient.toString()


    }

    override fun getItemCount() = nutrients.size





}
