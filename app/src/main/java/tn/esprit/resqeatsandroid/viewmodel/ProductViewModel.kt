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

                viewModelScope.launch(Dispatchers.Main) {
                    _products.value = homeItems
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
