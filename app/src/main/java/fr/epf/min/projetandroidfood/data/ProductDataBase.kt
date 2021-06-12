package fr.epf.min.projetandroidfood.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fr.epf.min.projetandroidfood.model.NutrientLevel
import fr.epf.min.projetandroidfood.model.NutriscoreGrade
import fr.epf.min.projetandroidfood.model.Product
import java.lang.reflect.Type


@Database(entities = [Product::class], version = 1)
@TypeConverters(ProductConverter::class)
abstract class ProductDataBase: RoomDatabase() {

    abstract fun getProductDao() : ProductDao
}

class ProductConverter {
    @TypeConverter
    fun stringtoNutriscoreGrade(value: String) = NutriscoreGrade.valueOf(value)
    @TypeConverter
    fun nutriscoreGradetoString(value: NutriscoreGrade) = value.name

    @TypeConverter
    fun stringtoStringMap(value: String?): Map<String?, String?>? {
        val mapType: Type = object : TypeToken<Map<String?, String?>?>() {}.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    fun stringMaptoString(map: Map<String?, String?>?): String? {
        val gson = Gson()
        return gson.toJson(map)
    }


    @TypeConverter
    fun stringtoNutrientLevelMap(value: String): Map<String?, NutrientLevel?>? {
        val mapType = object : TypeToken<Map<String, NutrientLevel>>() {}.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    fun nutrientLevelMaptoString(map: Map<String, NutrientLevel>): String {
        val gson = Gson()
        return gson.toJson(map)
    }
}
