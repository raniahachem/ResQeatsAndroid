package tn.esprit.resqeatsandroid.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import tn.esprit.resqeatsandroid.model.CartItem

@Dao
interface CartItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCartItem(cartItem: CartItem): Long


    @Query("SELECT * FROM cart_items")
    fun getAllCartItems(): MutableList<CartItem>?


    @Query("SELECT * FROM cart_items WHERE productId = :productId LIMIT 1")
    fun getCartItemById(productId: String): CartItem?

    @Update
    fun updateCartItem(cartItem: CartItem)

    @Delete
    fun deleteCartItem(cartItem: CartItem)
}

