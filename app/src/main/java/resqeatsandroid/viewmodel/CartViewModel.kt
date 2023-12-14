package resqeatsandroid.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tn.esprit.resqeatsandroid.model.CartItem
import tn.esprit.resqeatsandroid.repository.CartRepository

class CartViewModel(private val cartRepository: CartRepository) : ViewModel() {

    /*private val _cartItems: MutableLiveData<List<CartItem>> = MutableLiveData()
    val cartItems: LiveData<List<CartItem>> get() = _cartItems

    fun setCartItems(updatedCartItems: List<CartItem>) {
        viewModelScope.launch {
            _cartItems.postValue(updatedCartItems)
        }
    }



    suspend fun deleteCartItem(cartItem: CartItem): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                cartRepository.deleteCartItem(cartItem)
                true
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error deleting cart item", e)
                false
            }
        }
    }

    suspend fun calculateTotalPrice(): Double {
        return cartRepository.calculateTotalPrice()
    }*/

    // LiveData directement alimenté par le repository
    val cartItems: LiveData<List<CartItem>> = cartRepository.getAllCartItemsLiveData()

    // Fonction pour supprimer un article du panier
    suspend fun deleteCartItem(cartItem: CartItem): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                cartRepository.deleteCartItem(cartItem)
                true
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error deleting cart item", e)
                false
            }
        }
    }


    // Fonction pour calculer le prix total des articles dans le panier
    suspend fun calculateTotalPrice(): Double {
        return cartRepository.calculateTotalPrice()
    }

}


