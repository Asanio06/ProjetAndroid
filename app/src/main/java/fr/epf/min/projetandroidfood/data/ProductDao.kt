package fr.epf.min.projetandroidfood.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import fr.epf.min.projetandroidfood.model.Produit


@Dao
interface ProductDao {

    @Query("select * from produits")
    suspend fun getAllProduct() : List<Produit>

    @Insert
    suspend fun addProduct(produit: Produit)

}