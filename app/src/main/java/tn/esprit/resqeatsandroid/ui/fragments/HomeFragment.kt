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
import tn.esprit.resqeatsandroid.ui.adapters.RestaurantAdapter
import tn.esprit.resqeatsandroid.viewmodel.RestaurantViewModel

class HomeFragment: Fragment() {
    private lateinit var viewModel: RestaurantViewModel
    private lateinit var restaurantAdapter: RestaurantAdapter
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

        // Initialiser le ViewModel
        viewModel = ViewModelProvider(this).get(RestaurantViewModel::class.java)

        // Observer pour les restaurants
        viewModel.restaurants.observe(viewLifecycleOwner, Observer { restaurants ->
            restaurantAdapter = RestaurantAdapter(restaurants)
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerView.adapter = restaurantAdapter
        })

        // Initier l'appel pour obtenir les restaurants
        viewModel.getAllRestaurants()
    }
}