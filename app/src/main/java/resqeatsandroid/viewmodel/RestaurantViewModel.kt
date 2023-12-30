package tn.esprit.resqeatsandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import resqeatsandroid.api.ApiService
import tn.esprit.resqeatsandroid.model.Restaurant

class RestaurantViewModel(private val apiService: ApiService) : ViewModel() {

    private val _restaurants = MutableLiveData<List<Restaurant>>()
    val restaurants: LiveData<List<Restaurant>> get() = _restaurants

    fun getAllRestaurants() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val restaurantList = apiService.getAllRestaurants()
                _restaurants.postValue(restaurantList)
            } catch (e: Exception) {
                // Gérer les erreurs
            }
        }
    }
}
