package tn.esprit.resqeatsandroid.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tn.esprit.resqeatsandroid.R
import tn.esprit.resqeatsandroid.model.HomeItem
import tn.esprit.resqeatsandroid.network.RetrofitClient
import tn.esprit.resqeatsandroid.ui.adapters.ProductSupplierAdapter

class HomeSupplierActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductSupplierAdapter // Assurez-vous d'avoir votre propre adaptateur

    private val apiService = RetrofitClient.create()
    private val restaurantId = "65594e93fb8b75c44f353fb5" // ID de restaurant statique

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_supplier)

        // Initialisez votre RecyclerView et votre adaptateur
        recyclerView = findViewById(R.id.restaurantProductsRecyclerView)
        productAdapter = ProductSupplierAdapter() // Assurez-vous d'initialiser votre adaptateur avec les données appropriées

        // Utilisez un GridLayoutManager avec un spanCount de 2
        val layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = productAdapter

        // Appelez la méthode pour obtenir les produits par ID de restaurant
        getProductsByRestaurantId()
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_supplier)

        // Initialisez votre RecyclerView et votre adaptateur
        recyclerView = findViewById(R.id.restaurantProductsRecyclerView)
        productAdapter = ProductSupplierAdapter() // Assurez-vous d'initialiser votre adaptateur avec les données appropriées

        // Utilisez un GridLayoutManager avec un spanCount de 2
        val layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = productAdapter

        // Obtenez la référence de la barre d'outils
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        // Initialisez la barre d'outils avec la barre d'actions
        setSupportActionBar(toolbar)

        // Appelez la méthode pour obtenir les produits par ID de restaurant
        getProductsByRestaurantId()
    }

    private fun getProductsByRestaurantId() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val products = apiService.getAllProductsByRestaurantId(restaurantId)

                // Convertissez la liste de Product en liste de HomeItem.ProductItem
                val productList = products.map { HomeItem.ProductItem(it) }

                withContext(Dispatchers.Main) {
                    // Mettez à jour l'adaptateur avec les produits obtenus
                    productAdapter.submitList(productList)
                    productAdapter.updateList(productList)


                }
            } catch (e: Exception) {
                // Gérer les erreurs
            }
        }
    }

    // Fonction appelée lors du clic sur le bouton Add Product
    fun onAddProductButtonClick(view: View) {
        val intent = Intent(this, AddProductActivity::class.java)
        startActivity(intent)
    }
}

