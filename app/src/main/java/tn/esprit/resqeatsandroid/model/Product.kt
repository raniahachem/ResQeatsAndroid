package tn.esprit.resqeatsandroid.model

data class Product(
    val title: String,
    val category: String,
    val description: String,
    val price: Double,
    val image: String,
    val quantity: Int,
    val restaurant: String,
    val createdAt: String,
    val updatedAt: String,
    val _id: String
)
