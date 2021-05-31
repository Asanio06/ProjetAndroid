package fr.epf.min.projetandroidfood.model

enum class NutriscoreGrade {
    A, B, C, D, E, UNKNOW
}

enum class EcoscoreGrade {
    A, B, C, D, E, UNKNOW
}

enum class NutrientLevel {
    low, moderate, high, unknow
}

data class Produit(
    val nom: String,
    val marque: String,
    val masse: String,
    val image_url: String,
    val nutriscoreGrade: NutriscoreGrade,
    val ecoscoreGrade: EcoscoreGrade,
    val ingredientsText: String,
    val nutriments: HashMap<String, String>,
    val nutrientLevelsTtag: HashMap<String, NutrientLevel>,


    ) {
    companion object {
        fun all(size: Int = 30) = (1..size).map {
            Produit(
                "nom$it",
                "marque$it",
                "80g",
                "https://static.openfoodfacts.org/images/products/312/334/901/4822/front_fr.14.400.jpg",
                if (it % 2 == 0) NutriscoreGrade.UNKNOW else NutriscoreGrade.B,
                if (it % 2 == 0) EcoscoreGrade.E else EcoscoreGrade.UNKNOW,
                "",
                hashMapOf("calcium" to "30", "carbohydrates" to "7", "energy" to "zz"),
                hashMapOf(
                    "fat" to NutrientLevel.high,
                    "salt" to NutrientLevel.low,
                    "sugars" to NutrientLevel.moderate
                )


            )
        }
    }

}