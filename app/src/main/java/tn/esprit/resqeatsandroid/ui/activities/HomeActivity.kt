package tn.esprit.resqeatsandroid.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import tn.esprit.resqeatsandroid.R
import tn.esprit.resqeatsandroid.database.AppDatabase
import tn.esprit.resqeatsandroid.viewmodel.ProductViewModel
import tn.esprit.resqeatsandroid.viewmodel.RestaurantViewModel


class HomeActivity : AppCompatActivity() {

    val database: AppDatabase by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "cart-database")
            .build()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }
}

