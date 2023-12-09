package tn.esprit.resqeatsandroid.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.google.android.material.bottomnavigation.BottomNavigationView
import tn.esprit.resqeatsandroid.R
import tn.esprit.resqeatsandroid.database.AppDatabase
import tn.esprit.resqeatsandroid.repository.CartRepository
import tn.esprit.resqeatsandroid.viewmodel.CartViewModel
import tn.esprit.resqeatsandroid.viewmodel.CartViewModelFactory
import tn.esprit.resqeatsandroid.viewmodel.ProductViewModel
import tn.esprit.resqeatsandroid.viewmodel.RestaurantViewModel


class HomeActivity : AppCompatActivity() {
    val cartViewModel: CartViewModel by viewModels()
    val database: AppDatabase by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "cart-database")
            .build()
    }
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Configurez le NavController avec le fragment NavHost
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController

        // Configurez la BottomNavigationView avec le NavController
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setupWithNavController(navController)
    }

    // Pour gérer le retour arrière avec la barre de navigation
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
