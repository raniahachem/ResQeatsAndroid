package tn.esprit.resqeatsandroid.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import tn.esprit.resqeatsandroid.R
import tn.esprit.resqeatsandroid.databinding.ActivityHomeBinding
import tn.esprit.resqeatsandroid.ui.adapters.RestaurantAdapter
import tn.esprit.resqeatsandroid.viewmodel.RestaurantViewModel
import tn.esprit.resqeatsandroid.databinding.ItemRestaurantBinding


// Fichier : HomeActivity.kt
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }
}
