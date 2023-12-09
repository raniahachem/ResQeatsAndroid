package tn.esprit.resqeatsandroid.repository

import android.util.Log
import androidx.lifecycle.LiveData
import tn.esprit.resqeatsandroid.database.CartItemDao
import tn.esprit.resqeatsandroid.model.CartItem

class CartRepository(private val cartItemDao: CartItemDao) {

    suspend fun deleteCartItem(cartItem: CartItem): Boolean {
        try {
            val rowsDeleted = cartItemDao.deleteCartItem(cartItem)
            return rowsDeleted > 0
        } catch (e: Exception) {
            Log.e("CartRepository", "Error deleting cart item", e)
            return false
        }
    }

    // Fonction pour obtenir tous les articles de la base de données
    fun getAllCartItemsLiveData(): LiveData<List<CartItem>> {
        return cartItemDao.getAllCartItemsLiveData()
    }

    suspend fun calculateTotalPrice(): Double {
        return cartItemDao.calculateTotalPrice() ?: 0.0
    }
}



