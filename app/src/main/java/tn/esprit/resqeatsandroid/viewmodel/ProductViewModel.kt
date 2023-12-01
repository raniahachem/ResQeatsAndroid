package tn.esprit.resqeatsandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tn.esprit.resqeatsandroid.api.ApiService
import tn.esprit.resqeatsandroid.model.HomeItem

class ProductViewModel(private val apiService: ApiService) : ViewModel() {

    private val _products = MutableLiveData<List<HomeItem.ProductItem>>()
    val products: LiveData<List<HomeItem.ProductItem>> get() = _products

    fun getAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val productList = apiService.getAllProducts()
                val homeItems = productList.map { HomeItem.ProductItem(it) }

                // Utilisation de postValue pour mettre à jour LiveData sur le thread principal
                _products.postValue(homeItems)

            } catch (e: Exception) {
                e.printStackTrace()
                // Gestion des erreurs si nécessaire
            }
        }
    }

    // Méthode pour récupérer les produits par ID de restaurant
    fun getAllProductsByRestaurantId(restaurantId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val productList = apiService.getAllProductsByRestaurantId(restaurantId)
                val homeItems = productList.map { HomeItem.ProductItem(it) }

                // Utilisation de postValue pour mettre à jour LiveData sur le thread principal
                _products.postValue(homeItems)

            } catch (e: Exception) {
                e.printStackTrace()
                // Gestion des erreurs si nécessaire
            }
        }
    }
}

