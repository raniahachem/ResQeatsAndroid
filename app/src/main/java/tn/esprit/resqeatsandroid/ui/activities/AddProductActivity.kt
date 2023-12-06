package tn.esprit.resqeatsandroid.ui.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tn.esprit.resqeatsandroid.R
import tn.esprit.resqeatsandroid.api.ApiService
import tn.esprit.resqeatsandroid.model.Product
import tn.esprit.resqeatsandroid.network.RetrofitClient

class AddProductActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        apiService = RetrofitClient.create()

        val spinnerCategory: Spinner = findViewById(R.id.spinnerCategory)
        val btnAddProduct: Button = findViewById(R.id.btnAddProduct)

        btnAddProduct.setOnClickListener {
            // Récupérer les valeurs des champs et créer un objet Product
            val product = Product(
                title = findViewById<EditText>(R.id.editTextTitle).text.toString(),
                category = spinnerCategory.selectedItem.toString(),
                description = findViewById<EditText>(R.id.editTextDescription).text.toString(),
                price = findViewById<EditText>(R.id.editTextPrice).text.toString().toInt(),
                image = findViewById<EditText>(R.id.editTextImage).text.toString(),
                quantity = findViewById<EditText>(R.id.editTextQuantity).text.toString().toInt(),
                restaurant = "65594e93fb8b75c44f353fb5",
                _id = null
            )

            // Appel de l'API avec Retrofit
            addProduct(product)
        }
    }

    private fun addProduct(product: Product) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.addProduct(product)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        // Gérer le succès
                        Toast.makeText(this@AddProductActivity, "Product added successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        // Gérer l'échec
                        Toast.makeText(this@AddProductActivity, "Failed to add product", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                // Gérer les erreurs réseau ou autres
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddProductActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
