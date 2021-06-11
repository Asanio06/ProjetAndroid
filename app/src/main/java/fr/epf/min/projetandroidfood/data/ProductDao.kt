package fr.epf.min.projetandroidfood.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import fr.epf.min.projetandroidfood.model.Produit


@Dao
interface ProductDao {

    @Query("select * from produits")
    suspend fun getAllProduct(): List<Produit>

    @Query("select * from produits where codeBarre=:barcode")
    suspend fun getProduct(barcode: String): List<Produit>

    @Query("delete from produits where codeBarre=:barcode")
    suspend fun deleteProduct(barcode:String)

    @Insert
    suspend fun addProduct(produit: Produit)

}