package tn.esprit.resqeatsandroid.model

import androidx.room.Entity
import androidx.room.PrimaryKey

import androidx.room.ColumnInfo


@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "productId")
    val productId: String,

    @ColumnInfo(name = "productName")
    val productName: String,

    @ColumnInfo(name = "productCategory")
    val productCategory: String,

    @ColumnInfo(name = "productPrice")
    val productPrice: Int,

    @ColumnInfo(name = "productImage")
    val productImage: String,

    @ColumnInfo(name = "quantity")
    var quantity: Int

)
 {
    fun totalItemPrice(): Double {
        return quantity * productPrice.toDouble()
    }
}
