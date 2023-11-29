package tn.esprit.resqeatsandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tn.esprit.resqeatsandroid.api.ApiService
import tn.esprit.resqeatsandroid.model.HomeItem
import tn.esprit.resqeatsandroid.model.Restaurant

class RestaurantViewModel(val apiService: ApiService) : ViewModel() {

    // LiveData pour les restaurants
    private val _restaurants = MutableLiveData<List<HomeItem.RestaurantItem>>()
    val restaurants: LiveData<List<HomeItem.RestaurantItem>> get() = _restaurants

    // Fonction pour obtenir tous les restaurants
    fun getAllRestaurants() {
        viewModelScope.launch {
            try {
                /*// Appel à l'API pour récupérer la liste de tous les restaurants
                val restaurantList = apiService.getAllRestaurants()

                // Conversion des restaurants en liste d'items HomeItem
                val homeItems = restaurantList.map { HomeItem.RestaurantItem(it) }

                // Mise à jour du LiveData avec la liste d'items HomeItem
                _restaurants.value = homeItems*/
                val restaurantList = apiService.getAllRestaurants()
                val homeItems = mutableListOf<HomeItem.RestaurantItem>()
                for (restaurant in restaurantList) {
                    homeItems.add(HomeItem.RestaurantItem(restaurant))
                }
                _restaurants.value = homeItems

            } catch (e: Exception) {
                // Gérer l'erreur ici
                e.printStackTrace()
            }
        }
    }
}
