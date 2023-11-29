package tn.esprit.resqeatsandroid.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import tn.esprit.resqeatsandroid.databinding.FragmentHomeBinding
import tn.esprit.resqeatsandroid.ui.adapters.ProductAdapter
import tn.esprit.resqeatsandroid.ui.adapters.RestaurantAdapter
import tn.esprit.resqeatsandroid.viewmodel.ProductViewModel
import tn.esprit.resqeatsandroid.viewmodel.RestaurantViewModel

class HomeFragment : Fragment() {
    private lateinit var restaurantViewModel: RestaurantViewModel
    private lateinit var productViewModel: ProductViewModel
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var productAdapter: ProductAdapter
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialiser le ViewModel pour les restaurants
        restaurantViewModel = ViewModelProvider(this).get(RestaurantViewModel::class.java)

        // Créer l'adaptateur pour les restaurants avec le service API et le ViewModelScope
        restaurantAdapter = RestaurantAdapter()

        // Configurer le RecyclerView pour les restaurants
        binding.restaurantRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.restaurantRecyclerView.adapter = restaurantAdapter

        // Initier l'appel pour obtenir les restaurants
        restaurantViewModel.getAllRestaurants()

        // Observer pour les restaurants
        restaurantViewModel.restaurants.observe(viewLifecycleOwner, Observer { restaurants ->
            restaurantAdapter.submitList(restaurants)
        })

        // Initialiser le ViewModel pour les produits
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        // Créer l'adaptateur pour les produits avec le service API et le ViewModelScope
        productAdapter = ProductAdapter()

        // Configurer le RecyclerView pour les produits
        binding.productRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.productRecyclerView.adapter = productAdapter

        // Initier l'appel pour obtenir les produits
        productViewModel.getAllProducts()

        // Observer pour les produits
        productViewModel.products.observe(viewLifecycleOwner, Observer { products ->
            productAdapter.submitList(products)
        })
    }
}
