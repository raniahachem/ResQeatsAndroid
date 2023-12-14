package resqeatsandroid.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import resqeatsandroid.ui.adapters.ProductAdapter
import resqeatsandroid.ui.adapters.RestaurantAdapter
import resqeatsandroid.viewmodel.CartViewModel
import resqeatsandroid.viewmodel.CartViewModelFactory
import resqeatsandroid.viewmodel.ProductViewModel
import resqeatsandroid.viewmodel.ProductViewModelFactory
import resqeatsandroid.viewmodel.RestaurantViewModel
import resqeatsandroid.viewmodel.RestaurantViewModelFactory
import tn.esprit.resqeatsandroid.databinding.FragmentHomeBinding
import tn.esprit.resqeatsandroid.model.CartItem
import tn.esprit.resqeatsandroid.model.Product
import tn.esprit.resqeatsandroid.network.RetrofitClient
import tn.esprit.resqeatsandroid.repository.CartRepository
import tn.esprit.resqeatsandroid.ui.activities.HomeActivity

class HomeFragment : Fragment() {

    private lateinit var restaurantViewModel: RestaurantViewModel
    private lateinit var productViewModel: ProductViewModel
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var productAdapter: ProductAdapter
    private lateinit var binding: FragmentHomeBinding

    private val cartViewModel: CartViewModel by activityViewModels {
        CartViewModelFactory(CartRepository((requireActivity() as HomeActivity).database.cartItemDao()))
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
        productAdapter = ProductAdapter(
            addToCartClickListener = { productItem ->
                onAddToCartClicked(productItem.product)
            }
        )

        restaurantAdapter.setOnItemClickListener { restaurantItem ->
            navigateToRestaurantProducts(restaurantItem._id)
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
    }

    private fun navigateToRestaurantProducts(restaurantId: String) {
        val action =
            HomeFragmentDirections.actionFragmentHomeToRestaurantProductsFragment()
        findNavController().navigate(action)
    }

    private fun onAddToCartClicked(product: Product) {
        // Update the shared ViewModel
        val cartItems = cartViewModel.cartItems.value ?: emptyList()
        val updatedCartItems = cartItems + CartItem(
            productId = product._id.toString(),
            productName = product.title,
            productCategory = product.category,
            productPrice = product.price,
            productImage = product.image,
            quantity = 1
        )
        // Notez que setCartItems n'est plus nécessaire, car cartItems est maintenant un LiveData direct du repository

        // Save the product to Room database
        saveProductToRoom(product)

        Toast.makeText(requireContext(), "Item added to cart successfully", Toast.LENGTH_SHORT).show()
    }

    private fun saveProductToRoom(product: Product) {
        // Save the product to Room database
        val cartItem = CartItem(
            productId = product._id.toString(),
            productName = product.title,
            productCategory = product.category,
            productPrice = product.price,
            productImage = product.image,
            quantity = 1
        )
        // CoroutineScope is necessary here to perform database operations in a background thread
        CoroutineScope(Dispatchers.IO).launch {
            // Insert the cart item into Room database
            (requireActivity() as HomeActivity).database.cartItemDao().insertCartItem(cartItem)
        }
    }


}
