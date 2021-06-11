package fr.epf.min.projetandroidfood.ui.fragment.scanner

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.google.zxing.Result
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import fr.epf.min.projetandroidfood.ProductDetailsActivity
import fr.epf.min.projetandroidfood.ProduitService
import fr.epf.min.projetandroidfood.data.ProductDataBase
import fr.epf.min.projetandroidfood.model.EcoscoreGrade
import fr.epf.min.projetandroidfood.model.NutrientLevel
import fr.epf.min.projetandroidfood.model.NutriscoreGrade
import fr.epf.min.projetandroidfood.model.Produit
import kotlinx.android.synthetic.main.fragment_scanner.view.*
import kotlinx.coroutines.runBlocking
import me.dm7.barcodescanner.zxing.ZXingScannerView
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class SimpleScannerFragment : Fragment(), ZXingScannerView.ResultHandler {
    private var mScannerView: ZXingScannerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mScannerView = ZXingScannerView(getActivity())
        return mScannerView as ZXingScannerView
    }

    override fun onResume() {
        super.onResume()
        mScannerView!!.setResultHandler(this)
        mScannerView!!.startCamera()
    }

    override fun handleResult(rawResult: Result) {


        if ( rawResult!= null) {
            if (rawResult.text != null) {
                val scannedResult = rawResult.text.toString()
                Log.d("epf_good", "$scannedResult")

                val productFromApi = getProductByBarCode(scannedResult)
                if(productFromApi !=null){
                    val intent = Intent(this.requireContext(), ProductDetailsActivity::class.java)
                    intent.putExtra("product", productFromApi)
                    this.requireContext().startActivity(intent)
                }else{
                    Toast.makeText(
                        getActivity(), "Le produit est introuvable", Toast.LENGTH_SHORT
                    ).show()
                }




            } else {
                Toast.makeText(
                    getActivity(), "Contents = " + rawResult.text +
                            ", Format = " + rawResult.barcodeFormat.toString(), Toast.LENGTH_SHORT
                ).show()
                // Note:
                // * Wait 2 seconds to resume the preview.
                // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
                // * I don't know why this is the case but I don't have the time to figure out.

                val handler = Handler()
                handler.postDelayed(
                    { mScannerView!!.resumeCameraPreview(this@SimpleScannerFragment) },
                    2000
                )

            }
        }
    }

    override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera()
    }

    private fun getProductByBarCode(barCode: String): Produit {
        try {
            var product: Produit
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://world.openfoodfacts.org/")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

            val service = retrofit.create(ProduitService::class.java)

            runBlocking {
                val result = service.getProduitByBarCode(barCode)
                val productApi = result.product
                product = Produit(
                    0,
                    productApi.product_name_fr,
                    productApi.brands,
                    productApi.quantity,
                    productApi.image_url,
                    when (productApi.nutriscore_grade) {
                        "a" -> NutriscoreGrade.A
                        "b" -> NutriscoreGrade.B
                        "c" -> NutriscoreGrade.C
                        "d" -> NutriscoreGrade.D
                        "e" -> NutriscoreGrade.E
                        else -> NutriscoreGrade.UNKNOW
                    },
                    when (productApi.ecoscore_grade) {
                        "a" -> EcoscoreGrade.A
                        "b" -> EcoscoreGrade.B
                        "c" -> EcoscoreGrade.C
                        "d" -> EcoscoreGrade.D
                        "e" -> EcoscoreGrade.E
                        else -> EcoscoreGrade.UNKNOW
                    },
                    productApi.ingredients_text_fr,
                    productApi.nutriments,
                    productApi.nutrient_levels?.mapValues { (nom, level) ->
                        when (level) {
                            "high" -> NutrientLevel.high
                            "moderate" -> NutrientLevel.moderate
                            "low" -> NutrientLevel.low
                            else -> NutrientLevel.unknow
                        }
                    },
                    productApi._id
                )

                saveProductInHistory(product)


            }

            return product
        } catch (e: Exception) {
            val dialogBuilder = AlertDialog.Builder(this.requireActivity())

            // set message of alert dialog
            dialogBuilder.setMessage(e.message)
                // if the dialog is cancelable
                .setCancelable(false)
                // positive button text and action
                .setPositiveButton("Ok", null)
            // negative button text and action


            // create dialog box
            val alert = dialogBuilder.create()

            // show alert dialog
            alert.show()
            alert.setOnShowListener {
                val okButton = alert.getButton(AlertDialog.BUTTON_POSITIVE)
                okButton.setOnClickListener {
                    // dialog won't close by default
                    alert.dismiss()
                }
            }
        }

        return Produit(0)


    }

    private fun saveProductInHistory(produit: Produit) {
        val database = Room.databaseBuilder(
            this.requireContext(), ProductDataBase::class.java, "HistoryProductFinal2-db"
        ).build()

        val productDao = database.getProductDao()

        runBlocking {
            productDao.addProduct(produit)
        }
    }

}