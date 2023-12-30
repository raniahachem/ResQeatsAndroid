package tn.esprit.resqeatsandroid.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import resqeatsandroid.repository.CartRepository
import tn.esprit.resqeatsandroid.model.CartItem

class CartViewModel(private val cartRepository: CartRepository) : ViewModel() {
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

    suspend fun updateCartItem(cartItem: CartItem): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                cartRepository.updateCartItem(cartItem)
                true
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error updating cart item quantity", e)
                false
            }
        }
    }

    suspend fun clearCart() {
        withContext(Dispatchers.IO) {
            cartRepository.clearCart()
        }
    }
}
