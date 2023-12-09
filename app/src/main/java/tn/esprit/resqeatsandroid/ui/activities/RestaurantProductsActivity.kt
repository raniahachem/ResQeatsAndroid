package tn.esprit.resqeatsandroid.ui.activities
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tn.esprit.resqeatsandroid.R
import tn.esprit.resqeatsandroid.database.AppDatabase
import tn.esprit.resqeatsandroid.model.CartItem
import tn.esprit.resqeatsandroid.model.Product
import tn.esprit.resqeatsandroid.network.RetrofitClient
import tn.esprit.resqeatsandroid.ui.adapters.ProductAdapter
import tn.esprit.resqeatsandroid.viewmodel.ProductViewModel
import tn.esprit.resqeatsandroid.viewmodel.ProductViewModelFactory

class RestaurantProductsActivity : AppCompatActivity() {

    private lateinit var productViewModel: ProductViewModel
    private lateinit var productAdapter: ProductAdapter

    private val database: AppDatabase by lazy {
        AppDatabase.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_products)

        val restaurantId = intent.getStringExtra("restaurantId")

        val apiService = RetrofitClient.create()
        productViewModel = ViewModelProvider(this, ProductViewModelFactory(apiService))
            .get(ProductViewModel::class.java)
        productAdapter = ProductAdapter { productItem ->
            onAddToCartClicked(productItem.product)
        }

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



    }
    private fun onAddToCartClicked(product: Product) {
        // Remplacez R.id.votre_id_textview par l'ID réel de votre TextView eachCartItemQuantity
        val quantityTextView: TextView = findViewById(R.id.eachCartItemQuantity) ?: return

        // Obtenez la valeur de quantité de l'élément TextView
        val selectedQuantity: Int = quantityTextView.text.toString().toIntOrNull() ?: 0

        val cartItem = CartItem(
            productId = product._id.toString(),
            productName = product.title,
            productCategory = product.category,
            productPrice = product.price,
            productImage = product.image,
            quantity = selectedQuantity
        )

        // Insert or update the cart item in the Room database
        insertOrUpdateCartItem(cartItem)

        // Notify the user or update UI accordingly
    }

    private fun insertOrUpdateCartItem(cartItem: CartItem) {
        // Use the database instance initialized earlier
        CoroutineScope(Dispatchers.IO).launch {
            val existingCartItem = database.cartItemDao().getCartItemById(cartItem.productId)

            if (existingCartItem != null) {
                // Update the quantity if the item already exists in the cart
                existingCartItem.quantity += cartItem.quantity
                database.cartItemDao().updateCartItem(existingCartItem)
            } else {
                // Insert a new cart item
                database.cartItemDao().insertCartItem(cartItem)
            }
        }
    }
}