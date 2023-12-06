package tn.esprit.resqeatsandroid.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tn.esprit.resqeatsandroid.R
import tn.esprit.resqeatsandroid.database.AppDatabase
import tn.esprit.resqeatsandroid.databinding.FragmentHomeBinding
import tn.esprit.resqeatsandroid.model.CartItem
import tn.esprit.resqeatsandroid.model.Product
import tn.esprit.resqeatsandroid.ui.activities.RestaurantProductsActivity
import tn.esprit.resqeatsandroid.ui.adapters.ProductAdapter
import tn.esprit.resqeatsandroid.ui.adapters.RestaurantAdapter
import tn.esprit.resqeatsandroid.network.RetrofitClient
import tn.esprit.resqeatsandroid.ui.activities.HomeActivity
import tn.esprit.resqeatsandroid.viewmodel.ProductViewModel
import tn.esprit.resqeatsandroid.viewmodel.ProductViewModelFactory
import tn.esprit.resqeatsandroid.viewmodel.RestaurantViewModel
import tn.esprit.resqeatsandroid.viewmodel.RestaurantViewModelFactory

class HomeFragment : Fragment() {

    private lateinit var restaurantViewModel: RestaurantViewModel
    private lateinit var productViewModel: ProductViewModel
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var productAdapter: ProductAdapter
    private lateinit var binding: FragmentHomeBinding
    private val database: AppDatabase by lazy {
        (requireActivity() as HomeActivity).database
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiService = RetrofitClient.create()

        // Restaurant ViewModel
        restaurantViewModel = ViewModelProvider(this, RestaurantViewModelFactory(apiService))
            .get(RestaurantViewModel::class.java)

        // Restaurant Adapter
        restaurantAdapter = RestaurantAdapter()

        // RecyclerView Setup for Restaurants
        binding.restaurantRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.restaurantRecyclerView.adapter = restaurantAdapter

        // Observing restaurant data
        restaurantViewModel.restaurants.observe(viewLifecycleOwner) { restaurants ->
            restaurantAdapter.submitList(restaurants)
        }

        // Fetching restaurant data
        restaurantViewModel.getAllRestaurants()

        // Product ViewModel
        productViewModel = ViewModelProvider(this, ProductViewModelFactory(apiService))
            .get(ProductViewModel::class.java)

        // Product Adapter
        productAdapter = ProductAdapter { product ->
            onAddToCartClicked(product.product)
        }


        // RecyclerView Setup for Products
        val productLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.productRecyclerView.layoutManager = productLayoutManager
        binding.productRecyclerView.adapter = productAdapter

        // Observing product data
        productViewModel.products.observe(viewLifecycleOwner) { products ->
            productAdapter.submitList(products)
        }

        // Fetching product data
        productViewModel.getAllProducts()

        // Ajouter un écouteur de clic sur un élément de la liste des restaurants
        restaurantAdapter.setOnItemClickListener { restaurant ->
            // Naviguer vers l'activité des produits du restaurant avec l'ID du restaurant sélectionné
            val intent = Intent(requireContext(), RestaurantProductsActivity::class.java)
            intent.putExtra("restaurantId", restaurant._id)
            startActivity(intent)
        }
    }
        // Fonction onAddToCartClicked
        private fun onAddToCartClicked(product: Product) {
            // Remplacez R.id.votre_id_textview par l'ID réel de votre TextView eachCartItemQuantity
            val quantityTextView: TextView = view?.findViewById(R.id.eachCartItemQuantity) ?: return

            // Obtenez la valeur de quantité de l'élément TextView
            val selectedQuantity: Int = quantityTextView.text.toString().toIntOrNull() ?: 0

            val cartItem = CartItem(
                productId = product._id,
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

        // Fonction insertOrUpdateCartItem
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
