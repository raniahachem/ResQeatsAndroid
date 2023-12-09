package tn.esprit.resqeatsandroid.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tn.esprit.resqeatsandroid.R
import tn.esprit.resqeatsandroid.database.AppDatabase
import tn.esprit.resqeatsandroid.model.CartItem
import tn.esprit.resqeatsandroid.model.Product
import tn.esprit.resqeatsandroid.network.RetrofitClient
import tn.esprit.resqeatsandroid.ui.adapters.ProductAdapter
import tn.esprit.resqeatsandroid.viewmodel.CartViewModel
import tn.esprit.resqeatsandroid.viewmodel.ProductViewModel
import tn.esprit.resqeatsandroid.viewmodel.ProductViewModelFactory
import androidx.fragment.app.activityViewModels
import tn.esprit.resqeatsandroid.repository.CartRepository
import tn.esprit.resqeatsandroid.ui.activities.HomeActivity
import tn.esprit.resqeatsandroid.viewmodel.CartViewModelFactory

class RestaurantProductsFragment : Fragment() {

    private lateinit var productViewModel: ProductViewModel
    private lateinit var productAdapter: ProductAdapter

    private val database: AppDatabase by lazy {
        AppDatabase.getInstance(requireActivity())
    }

    private val cartViewModel: CartViewModel by activityViewModels {
        CartViewModelFactory(CartRepository((requireActivity() as HomeActivity).database.cartItemDao()))
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_restaurant_products, container, false)
        // Récupérer l'ID du restaurant depuis les arguments
        val restaurantId = arguments?.getString("restaurantId")

        // Vérifier si l'ID du restaurant est présent
        if (!restaurantId.isNullOrBlank()) {
            // Utiliser l'ID du restaurant pour charger les produits
            productViewModel.getAllProductsByRestaurantId(restaurantId)
        }
        // Initialize ViewModel
        val apiService = RetrofitClient.create()
        productViewModel = ViewModelProvider(
            requireActivity(),
            ProductViewModelFactory(apiService)
        ).get(ProductViewModel::class.java)

        // Initialize RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.restaurantProductsRecyclerView)
        val layoutManager = GridLayoutManager(requireActivity(), 2)
        recyclerView.layoutManager = layoutManager

        productAdapter = ProductAdapter(
            addToCartClickListener = { productItem ->
                onAddToCartClicked(productItem.product)
            }
        )

        recyclerView.adapter = productAdapter

        // Observe the products
        productViewModel.products.observe(viewLifecycleOwner) { products ->
            productAdapter.submitList(products)
        }

        return view
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


        Toast.makeText(requireContext(), "Item added to cart successfully", Toast.LENGTH_SHORT).show()
    }


}
