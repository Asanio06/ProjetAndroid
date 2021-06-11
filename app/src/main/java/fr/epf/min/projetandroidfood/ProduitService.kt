package fr.epf.min.projetandroidfood


import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProduitService {
    @GET("/cgi/search.pl?search_simple=1&action=process&json=true&page_size=100&sort_by=unique_scans_n")
    suspend fun getProduitsBySearch(
        @Query("search_terms") search_terms: String = "",
        @Query("page") page: Int = 1

    ): GetProduitsResult

    @GET("/api/v0/product/{code}.json")
    suspend fun getProduitByBarCode(
        @Path("code") code: String
    ): GetProduitResult
}

data class GetProduitResult(val code: String, val product: APIResponse)

data class GetProduitsResult(val products: List<APIResponse>)

data class APIResponse(
    val brands: String? = "none",
    val product_name_fr: String? = "none",
    val image_url: String? = "none",
    val quantity: String? = "none",
    val ingredients_text_fr: String? = "none",
    val nutrient_levels: Map<String, String>? = null,
    val nutriments: Map<String, String>? = null,
    val nutriscore_grade: String? = "unknown",
    val ecoscore_grade: String? = "unknown",
    val _id:Int =0
)
