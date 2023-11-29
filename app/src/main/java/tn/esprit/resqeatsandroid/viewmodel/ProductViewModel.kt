import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tn.esprit.resqeatsandroid.api.ApiService
import tn.esprit.resqeatsandroid.model.HomeItem
import tn.esprit.resqeatsandroid.model.Product

class ProductViewModel(private val apiService: ApiService) : ViewModel() {

    // LiveData pour les produits
    private val _products = MutableLiveData<List<HomeItem.ProductItem>>()
    val products: LiveData<List<HomeItem.ProductItem>> get() = _products

    // Fonction pour obtenir tous les produits
    fun getAllProducts() {
        viewModelScope.launch {
            try {
                // Appel à l'API pour récupérer la liste de tous les produits
                val productList = apiService.getAllProducts()

                // Conversion des produits en liste d'items HomeItem
                val homeItems = productList.map { HomeItem.ProductItem(it) }

                // Mise à jour du LiveData avec la liste d'items HomeItem
                _products.value = homeItems
            } catch (e: Exception) {
                // Gérer l'erreur ici
                e.printStackTrace()
            }
        }
    }
}
