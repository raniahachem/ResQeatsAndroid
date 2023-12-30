package tn.esprit.resqeatsandroid.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.google.android.material.bottomnavigation.BottomNavigationView
import resqeatsandroid.database.AppDatabase
import resqeatsandroid.viewmodel.DonationViewModel
import tn.esprit.resqeatsandroid.R
import tn.esprit.resqeatsandroid.viewmodel.CartViewModel


class HomeActivity : AppCompatActivity() {
    private val viewModel: DonationViewModel by viewModels()

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
