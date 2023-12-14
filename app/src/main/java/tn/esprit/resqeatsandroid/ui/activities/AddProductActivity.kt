package tn.esprit.resqeatsandroid.ui.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
    private var selectedImageUri: Uri? = null

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    selectedImageUri = uri
                    showToast("Image selected successfully")
                }
            } else {
                showToast("Image selection failed")
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        apiService = RetrofitClient.create()

        val spinnerCategory: Spinner = findViewById(R.id.spinnerCategory)
        val btnAddProduct: Button = findViewById(R.id.btnAddProduct)
        val btnSelectImage: Button = findViewById(R.id.btnSelectImage)

        btnSelectImage.setOnClickListener {
            // Lancer l'activité de sélection d'image
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            pickImageLauncher.launch(intent)
        }

        btnAddProduct.setOnClickListener {
            // Récupérer les valeurs des champs et créer un objet Product
            val product = Product(
                title = findViewById<EditText>(R.id.editTextTitle).text.toString(),
                category = spinnerCategory.selectedItem.toString(),
                description = findViewById<EditText>(R.id.editTextDescription).text.toString(),
                price = findViewById<EditText>(R.id.editTextPrice).text.toString().toInt(),
                //image = selectedImageUri?.toString() ?: "",
                image= findViewById<EditText>(R.id.editTextImage).text.toString(),
                quantity = findViewById<EditText>(R.id.editTextQuantity).text.toString().toInt(),
                restaurant = "65594e93fb8b75c44f353fb5",
                _id = null
            )

            // Appel de l'API avec Retrofit
            addProduct(product)
        }
    }

    private fun getImagePickerIntent(): Intent {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        return intent
    }

    private fun addProduct(product: Product) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.addProduct(product)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        // Gérer le succès
                        showToast("Product added successfully")
                    } else {
                        // Gérer l'échec
                        showToast("Failed to add product")
                    }
                }
            } catch (e: Exception) {
                // Gérer les erreurs réseau ou autres
                withContext(Dispatchers.Main) {
                    showToast("Error: ${e.message}")
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@AddProductActivity, message, Toast.LENGTH_SHORT).show()
    }
}

