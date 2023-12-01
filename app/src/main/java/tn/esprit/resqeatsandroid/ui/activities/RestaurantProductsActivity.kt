package tn.esprit.resqeatsandroid.ui.activities
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tn.esprit.resqeatsandroid.R
import tn.esprit.resqeatsandroid.network.RetrofitClient
import tn.esprit.resqeatsandroid.ui.adapters.ProductAdapter
import tn.esprit.resqeatsandroid.viewmodel.ProductViewModel
import tn.esprit.resqeatsandroid.viewmodel.ProductViewModelFactory

class RestaurantProductsActivity : AppCompatActivity() {

    private lateinit var productViewModel: ProductViewModel
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_products)

        val restaurantId = intent.getStringExtra("restaurantId")

        val apiService = RetrofitClient.create()
        productViewModel = ViewModelProvider(this, ProductViewModelFactory(apiService))
            .get(ProductViewModel::class.java)
        productAdapter = ProductAdapter()

        val recyclerView: RecyclerView = findViewById(R.id.restaurantProductsRecyclerView)

        // Utiliser un GridLayoutManager avec un spanCount de 2 pour obtenir 2 produits par ligne
        val layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = productAdapter

        restaurantId?.let {
            productViewModel.getAllProductsByRestaurantId(it)
        }

        productViewModel.products.observe(this) { products ->
            productAdapter.submitList(products)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Ajoutez un écouteur de clic pour le bouton de retour
        val btnBack: ImageView = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            onBackPressed()
        }
    }
}
