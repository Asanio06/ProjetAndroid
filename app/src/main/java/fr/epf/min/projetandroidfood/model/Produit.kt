package fr.epf.min.projetandroidfood.model

enum class NutriscoreGrade {
    A, B, C, D, E, UNKNOW
}

enum class EcoscoreGrade {
    A, B, C, D, E, UNKNOW
}

data class Produit(
    val nom: String,
    val marque: String,
    val masse: String,
    val image_url: String,
    val nutriscoreGrade: NutriscoreGrade,
    val ecoscoreGrade: EcoscoreGrade
) {
    companion object {
        fun all(size: Int = 30) = (1..size).map {
            Produit(
                "nom$it",
                "marque$it",
                "80g",
                "https://static.openfoodfacts.org/images/products/312/334/901/4822/front_fr.14.400.jpg",
                if (it % 2 == 0) NutriscoreGrade.UNKNOW else NutriscoreGrade.B,
                if (it % 2 == 0) EcoscoreGrade.E else EcoscoreGrade.UNKNOW

            )
        }
    }

}