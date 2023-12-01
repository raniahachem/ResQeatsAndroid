package tn.esprit.resqeatsandroid.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import tn.esprit.resqeatsandroid.databinding.FragmentHomeBinding
import tn.esprit.resqeatsandroid.ui.activities.RestaurantProductsActivity
import tn.esprit.resqeatsandroid.ui.adapters.ProductAdapter
import tn.esprit.resqeatsandroid.ui.adapters.RestaurantAdapter
import tn.esprit.resqeatsandroid.network.RetrofitClient
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
        productAdapter = ProductAdapter()

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
}
