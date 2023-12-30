package tn.esprit.resqeatsandroid.model

data class Product(
    val title: String,
    val category: String,
    val description: String,
    val price: Int,
    val image: String,
    val quantity: Int,
    //val restaurant: String,
    val restaurant: String?,
    val _id: String?
)
