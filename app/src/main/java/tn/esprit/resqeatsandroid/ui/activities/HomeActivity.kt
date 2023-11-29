package tn.esprit.resqeatsandroid.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import tn.esprit.resqeatsandroid.databinding.ActivityHomeBinding
import tn.esprit.resqeatsandroid.ui.adapters.RestaurantAdapter
import tn.esprit.resqeatsandroid.viewmodel.RestaurantViewModel

class HomeActivity : AppCompatActivity() {
    private lateinit var viewModel: RestaurantViewModel
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialiser le ViewModel
        viewModel = ViewModelProvider(this).get(RestaurantViewModel::class.java)

        // Observer pour les restaurants
        viewModel.restaurants.observe(this, Observer { restaurants ->
            // Mettez à jour votre RecyclerView ici
            restaurantAdapter = RestaurantAdapter(restaurants)
            binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerView.adapter = restaurantAdapter
        })

        // Initier l'appel pour obtenir les restaurants
        viewModel.getAllRestaurants()
    }
}