package fr.epf.min.projetandroidfood.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

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
data class Produit(
    @field:PrimaryKey(autoGenerate = true) var id: Int,
    val nom: String? = "none",
    val marque: String? = "none",
    val masse: String? = "none",
    val image_url: String? = "null",
    val nutriscoreGrade: NutriscoreGrade? = NutriscoreGrade.UNKNOW,
    val ecoscoreGrade: EcoscoreGrade? = EcoscoreGrade.UNKNOW,
    val ingredientsText: String? = "",
    val nutriments: Map<String, String>? = null,
    val nutrients: Map<String, NutrientLevel>? = null,


    ) {
    companion object {
        fun all(size: Int = 30) = (1..size).map {
            Produit(
                0,
                "nom$it",
                "marque$it",
                "80g",
                "https://static.openfoodfacts.org/images/products/312/334/901/4822/front_fr.14.400.jpg",
                if (it % 2 == 0) NutriscoreGrade.UNKNOW else NutriscoreGrade.B,
                if (it % 2 == 0) EcoscoreGrade.E else EcoscoreGrade.UNKNOW,
                "",
               hashMapOf("calcium" to "30", "carbohydrates" to "7", "energy" to "6668"),
                hashMapOf(
                    "fat" to NutrientLevel.high,
                    "salt" to NutrientLevel.low,
                    "sugars" to NutrientLevel.moderate
                )


            )
        }
    }

}