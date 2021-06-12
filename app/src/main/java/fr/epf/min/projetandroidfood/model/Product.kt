package fr.epf.min.projetandroidfood.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.epf.min.projetandroidfood.R
import java.io.Serializable

enum class NutriscoreGrade {
    A, B, C, D, E, UNKNOW
}

enum class EcoscoreGrade {
    A, B, C, D, E, UNKNOW
}

enum class NutrientLevel {
    low, moderate, high, unknow
}

@Entity(tableName = "produits")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val nom: String? = "none",
    val marque: String? = "none",
    val masse: String? = "none",
    val image_url: String? = "null",
    val nutriscoreGrade: NutriscoreGrade? = NutriscoreGrade.UNKNOW,
    val ecoscoreGrade: EcoscoreGrade? = EcoscoreGrade.UNKNOW,
    val ingredientsText: String? = "",
    val nutriments: Map<String, String>? = null,
    val nutrients: Map<String, NutrientLevel>? = null,
    val codeBarre:String?="0"


    ) : Serializable {
    companion object {
        fun all(size: Int = 30) = (1..size).map {
            Product(
                null,
                "nom$it",
                "marque$it",
                "80g",
                "https://static.openfoodfacts.org/images/products/312/334/901/4822/front_fr.14.400.jpg",
                if (it % 2 == 0) NutriscoreGrade.UNKNOW else NutriscoreGrade.B,
                if (it % 2 == 0) EcoscoreGrade.E else EcoscoreGrade.UNKNOW,
                "Il y'a de l'eau ahah",
                hashMapOf("calcium" to "30", "carbohydrates" to "7", "energy" to "6668"),
                hashMapOf(
                    "fat" to NutrientLevel.high,
                    "salt" to NutrientLevel.low,
                    "sugars" to NutrientLevel.moderate
                )


            )
        }


    }

    fun getImageFromNutriScoreGrade() = when (this.nutriscoreGrade) {
        NutriscoreGrade.A -> R.drawable.ic_nutriscore_a
        NutriscoreGrade.B -> R.drawable.ic_nutriscore_b
        NutriscoreGrade.C -> R.drawable.ic_nutriscore_c
        NutriscoreGrade.D -> R.drawable.ic_nutriscore_d
        NutriscoreGrade.E -> R.drawable.ic_nutriscore_e
        NutriscoreGrade.UNKNOW -> R.drawable.ic_nutriscore_unknown
        else -> R.drawable.ic_nutriscore_unknown
    }

    fun getImageFromEcoscoreGrade() = when (this.ecoscoreGrade) {
        EcoscoreGrade.A -> R.drawable.ic_ecoscore_a
        EcoscoreGrade.B -> R.drawable.ic_ecoscore_b
        EcoscoreGrade.C -> R.drawable.ic_ecoscore_c
        EcoscoreGrade.D -> R.drawable.ic_ecoscore_d
        EcoscoreGrade.E -> R.drawable.ic_ecoscore_e
        EcoscoreGrade.UNKNOW -> R.drawable.ic_ecoscore_unknown
        else -> R.drawable.ic_ecoscore_unknown

    }

}