package fr.epf.min.projetandroidfood


import fr.epf.min.projetandroidfood.model.NutriscoreGrade
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProduitService {
    @GET("/cgi/search.pl?search_simple=1&action=process&json=true")
    suspend fun getProduitsBySearch(
        @Query("search_terms") search_terms: String,
        @Query("page") page:Int = 1

        ): GetProduitsResult

    @GET("/api/v0/product/{code}.json")
    suspend fun getProduitByCodeBarre(
        @Path("code") code: String
    ): GetProduitResult
}

data class GetProduitResult(val code: String, val product: APIResponse)

data class GetProduitsResult(val products: List<APIResponse>)

data class APIResponse(
    val brands: String?="none",
    val product_name_fr: String,
    val image_url: String? = "null",
    val ingredients_text_fr: String?="",
    val nutrient_levels: Map<String, String>,
    val nutriments: Map<String, String>,
    val nutriscore_grade: String? = "unknown",
    val ecoscore_grade: String? = "unknown"
)
